package com.grunick.addresstagger.strategy.unknown;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;

import opennlp.maxent.BasicEventStream;
import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.maxent.PlainTextByLineDataStream;
import opennlp.maxent.io.GISModelWriter;
import opennlp.maxent.io.SuffixSensitiveGISModelWriter;
import opennlp.model.EventStream;
import opennlp.model.GenericModelReader;
import opennlp.model.MaxentModel;


public class MaximumEntropyUnknownStrategy implements UnknownStrategy {
	
	protected MaxentModel maxent;
	protected String entropyFile;
	protected String persistFile;
	protected boolean trainInit = false;
	protected boolean fetchInit = false;
	BufferedWriter writer;
	
	public MaximumEntropyUnknownStrategy(String entropyFile, String persistFile) {
		this.entropyFile = entropyFile;
		this.persistFile = persistFile;
	}
	
	@Override
	public void train(Address address) throws InputException {
		if (!trainInit) {
			try {
				writer = new BufferedWriter(new FileWriter(entropyFile));
			} catch (IOException e) {
				throw new InputException(e.getMessage());
			}
			trainInit = true;
		}
		if (address.size() == 0)
			return;

		for (int i=0; i<address.size(); i++) {
			try {
				writer.write(encodeAddress(address, i, null));
				writer.newLine();
			} catch (IOException e) {
				throw new InputException(e.getMessage());
			}
			
		}
		
	}

	@Override
	public double getProbability(Address address, int idx, AddressTag prediction) throws InputException {
		if (!fetchInit) {
			try {
				writer.close();
				EventStream es = new BasicEventStream(new PlainTextByLineDataStream(new FileReader(entropyFile)));
				GISModel tmpmodel = GIS.trainModel(es, 20, 0);
				GISModelWriter modelWriter = new SuffixSensitiveGISModelWriter(tmpmodel, new File(persistFile));
				modelWriter.persist();
				maxent = new GenericModelReader(new File(persistFile)).getModel();
				fetchInit = true;
			} catch (IOException i) {
				throw new InputException(i.getMessage());
			}
		}
		String line = encodeAddress(address, idx, prediction);
		double[] outcomes = maxent.eval(line.trim().split(" "));
		Map<String,Double> types = parseOutcomes(maxent.getAllOutcomes(outcomes));
		return types.containsKey(prediction) ? types.get(prediction) : 0.000001;
	}


	protected String encodeAddress(Address address, int idx, AddressTag prediction) {
		String text = address.getAddressTokens().get(idx).trim();
		
		StringBuilder builder = new StringBuilder();
		builder.append("current=").append(text);
		builder.append(" prev=").append(idx==0?null:address.getAddressTokens().get(idx-1).trim());
		builder.append(" next=").append(idx>=(address.size()-1)?null: address.getAddressTokens().get(idx+1).trim());
		builder.append(" first=").append(idx==0);
		builder.append(" last=").append(idx==(address.size()-1));		
		builder.append(" length=").append(text.length());

		if (prediction == null) // training set
			builder.append(" curtag=").append(address.getKnownTags().get(idx).toString());
		else
			builder.append(" curtag="+prediction);

		return builder.toString();
	
	}
	
	protected static Pattern pattern = Pattern.compile("(.*?)=(.*?)\\[(.*?)\\]"); 
	
	protected Map<String,Double> parseOutcomes(String outcomes)  {
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



