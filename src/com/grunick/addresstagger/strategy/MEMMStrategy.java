package com.grunick.addresstagger.strategy;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.maxent.BasicEventStream;
import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.maxent.PlainTextByLineDataStream;
import opennlp.maxent.io.GISModelWriter;
import opennlp.maxent.io.SuffixSensitiveGISModelWriter;
import opennlp.model.EventStream;
import opennlp.model.GenericModelReader;
import opennlp.model.MaxentModel;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public class MEMMStrategy implements TaggerStrategy {
	
	protected MaxentModel maxent;
	protected File entropyFile;
	protected File persistFile;
	
	
	public MEMMStrategy(File entropyFile, File persistFile) {
		this.entropyFile = entropyFile;
		this.persistFile = persistFile;
	}

	@Override
	public void train(InputSource source) throws InputException {
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(entropyFile));
			while (source.hasNext()) {
				try {
					Address address = source.getNext();
					if (address.size() == 0)
						continue;

					List<String> lines = encodeAddress(address, null); 
					for (String line : lines) {
						writer.write(line);
						writer.newLine();
					}

				} catch (InputException ie) {}
			}
			writer.close();
	        EventStream es = new BasicEventStream(new PlainTextByLineDataStream(new FileReader(entropyFile)));
	        GISModel tmpmodel = GIS.trainModel(es, 100, 4);
	    	GISModelWriter modelWriter = new SuffixSensitiveGISModelWriter(tmpmodel, persistFile);
	    	modelWriter.persist();
	    	maxent = new GenericModelReader(persistFile).getModel();
		} catch (IOException e) {
			throw new InputException("Unable to generate maxent classifier!: "+e.getMessage());
		}

	}

	@Override
	public void tagAddress(Address address) {
		// TODO Auto-generated method stub

	}

	
	protected List<String> encodeAddress(Address address, String prediction) {
		List<String> terms = new ArrayList<String>();
		List<String> tokens = address.getAddressTokens();
		for (int i=0; i < address.size(); i++) {
			String text = tokens.get(i).trim();
			
			StringBuilder builder = new StringBuilder();

			builder.append("current=").append(text);

			if (i > 0) {
				String prev = tokens.get(i-1).trim();
				builder.append(" previous=").append(prev);
			}

			if (prediction == null) // training set
				builder.append(" tag=").append(address.getKnownTags().get(i).toString());
			else
				builder.append(" tag="+prediction);
			terms.add(builder.toString());
		}
		return terms;
		
	}
	
	public double predict(Address address, int idx, String prediction) throws Exception {
		List<String> lines = encodeAddress(address, prediction);
		double[] outcomes = maxent.eval(lines.get(idx).trim().split(" "));
		Map<String,Double> types = parseOutcomes(maxent.getAllOutcomes(outcomes));
		return types.get(prediction);
	}
	
	protected static Pattern pattern = Pattern.compile("(.*?)=(.*?)\\[(.*?)\\]"); 
	
	protected Map<String,Double> parseOutcomes(String outcomes) throws Exception {
		HashMap<String,Double> map = new HashMap<String,Double>();
		Matcher matcher = pattern.matcher(outcomes);
		while(matcher.find())  {
			String key = matcher.group(2);
			double value = Double.parseDouble(matcher.group(3));
			map.put(key, value);
		}
		return map;
	}

}
