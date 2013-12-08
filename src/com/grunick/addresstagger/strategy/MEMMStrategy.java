package com.grunick.addresstagger.strategy;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
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
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public class MEMMStrategy implements TaggerStrategy {
	
	protected MaxentModel maxent;
	protected File entropyFile;
	protected File persistFile;
	protected int iterations;
	protected int cutoff;
	
	EnumSet<AddressTag> values = EnumSet.allOf(AddressTag.class);
	List<AddressTag> knownStates = Arrays.asList(values.toArray(new AddressTag[] {}));
	
	
	public MEMMStrategy(File entropyFile, File persistFile, int iterations, int cutoff) {
		this.entropyFile = entropyFile;
		this.persistFile = persistFile;
		this.iterations = iterations;
		this.cutoff = cutoff;
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
	        GISModel tmpmodel = GIS.trainModel(es, iterations, cutoff);
	    	GISModelWriter modelWriter = new SuffixSensitiveGISModelWriter(tmpmodel, persistFile);
	    	modelWriter.persist();
	    	maxent = new GenericModelReader(persistFile).getModel();
		} catch (IOException e) {
			throw new InputException("Unable to generate maxent classifier!: "+e.getMessage());
		}

	}

	@Override
	public void tagAddress(Address address) {
		List<AddressTag> tags = viterbi(address);
		
		for (int i=0; i< address.size(); i++) {
			address.setTag(i, tags.get(i));
		}

	}

	
	public List<AddressTag> viterbi(Address address) {
		List<String> observations = address.getAddressTokens();
		if (observations.size() == 0)
			return new ArrayList<AddressTag>();
		
		
		// Initialization step
		Map<AddressTag, ViterbiNode<AddressTag>> stateMap = new HashMap<AddressTag, ViterbiNode<AddressTag>>();
		List<Map<AddressTag, ViterbiNode<AddressTag>>> backPointer = new ArrayList<Map<AddressTag, ViterbiNode<AddressTag>>>();
		for (AddressTag state : AddressTag.values()) {
			double prob = predict(address, 0, state.toString());   
			stateMap.put(state, new ViterbiNode<AddressTag>(prob, state, prob));
		}
		backPointer.add(stateMap);

		// Iteration step
		for (int i=1; i < observations.size(); i++) {
			HashMap<AddressTag, ViterbiNode<AddressTag>> nextStates = new HashMap<AddressTag, ViterbiNode<AddressTag>>();
			for (AddressTag next : AddressTag.values()) {
				double stateTotal = 0.0;
				AddressTag maxArg = null;
				double stateMax = 0.0;

				for (AddressTag previous : AddressTag.values()) {
					ViterbiNode<AddressTag> node = stateMap.get(previous);

					double totalProb = node.getTotalScore() * predict(address, i, next.toString());
					stateTotal += totalProb;
					if (totalProb > stateMax) {
						maxArg = previous;
						stateMax = totalProb;
					}
				}
				nextStates.put(next, new ViterbiNode<AddressTag>(stateTotal, maxArg, stateMax)); 
				
			}
			backPointer.add(nextStates);
			stateMap = nextStates;
		}
		
		// Termination step
		HashMap<AddressTag, ViterbiNode<AddressTag>> nextStates = new HashMap<AddressTag, ViterbiNode<AddressTag>>();
		double stateTotal = 0.0;
		AddressTag maxArg = null;
		double stateMax = 0.0;

		for (AddressTag previous : AddressTag.values()) {
			ViterbiNode<AddressTag> node = stateMap.get(previous);
			double totalProb = node.getTotalScore() * predict(address, observations.size()-1, previous.toString());
			stateTotal += totalProb;
			if (totalProb > stateMax) {
				maxArg = previous;
				stateMax = totalProb;
			}
		}
		nextStates.put(AddressTag.STOP, new ViterbiNode<AddressTag>(stateTotal, maxArg, stateMax));

		backPointer.add(nextStates);
		stateMap = nextStates;
		
		List<AddressTag> maxStates = new ArrayList<AddressTag>();
		AddressTag nextState = AddressTag.STOP;
		
		while (backPointer.size() > 0) {
			
			maxStates.add(0, nextState);
			Map<AddressTag,ViterbiNode<AddressTag>>nodes = backPointer.remove(backPointer.size()-1);
			ViterbiNode<AddressTag> max = nodes.get(nextState);
			nextState = max.getMaxState();
		}
		
		return maxStates;
	}

	
	protected List<String> encodeAddress(Address address, String prediction) {
		List<String> terms = new ArrayList<String>();
		List<String> tokens = address.getAddressTokens();
		for (int i=0; i < address.size(); i++) {
			String text = tokens.get(i).trim();
			
			StringBuilder builder = new StringBuilder();

			builder.append("current=").append(text);

			if (prediction == null) // training set
				builder.append(" tag=").append(address.getKnownTags().get(i).toString());
			else
				builder.append(" tag="+prediction);
			terms.add(builder.toString());
		}
		return terms;
		
	}
	
	// TODO: Better unknown word model?
	public double predict(Address address, int idx, String prediction) {
		List<String> lines = encodeAddress(address, prediction);
		double[] outcomes = maxent.eval(lines.get(idx).trim().split(" "));
		Map<String,Double> types = parseOutcomes(maxent.getAllOutcomes(outcomes));
		return types.containsKey(prediction) ? types.get(prediction) : 0.0000001;
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
