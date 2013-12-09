package com.grunick.addresstagger.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.stat.Counter;
import com.grunick.addresstagger.stat.CounterMap;

public class InterpolatedHMMStrategy implements TaggerStrategy {

	private Map<String, Map<AddressTag, Double>> bigramTransProb;
	private Map<String, Map<AddressTag, Double>> trigramTransProb;
	private Counter<AddressTag> tagCounter;
	private Map<AddressTag, Map<String, Double>> emProb;
	
	// TODO: Better unknown state model
	protected double getTransitionProb(AddressTag prevPrevState, AddressTag prevState, AddressTag state) {
		if (prevPrevState != null ) {
			String trigramKey = getTrigramKey(prevPrevState, prevState);
			if (trigramTransProb.containsKey(trigramKey) && trigramTransProb.get(trigramKey).containsKey(state))
				return trigramTransProb.get(trigramKey).get(state);
		}
		String bigramKey = prevState.toString();
		if (bigramTransProb.containsKey(bigramKey) && bigramTransProb.get(bigramKey).containsKey(state))
			return bigramTransProb.get(bigramKey).get(state);

		return 0.0000001;
	}

	// TODO: Better unknown term model
	protected double getEmissionProb(AddressTag state, String observation) {
		if (emProb.containsKey(state) && emProb.get(state).containsKey(observation))
			return emProb.get(state).get(observation);
		return 0.0000001;
	}
	

	
	protected String getTrigramKey(AddressTag prevTag, AddressTag tag) {
		return prevTag.toString()+"-"+tag.toString();
	}
	
	@Override
	public void train(InputSource source) throws InputException {
		CounterMap<String, AddressTag> bigramTransitionCounts = new CounterMap<String, AddressTag>();
		CounterMap<String, AddressTag> trigramTransitionCounts = new CounterMap<String, AddressTag>();

		CounterMap<AddressTag, String> emissionCounts = new CounterMap<AddressTag, String>();
		
		while (source.hasNext()) {
			try {
				Address address = source.getNext();
				if (address.size() == 0)
					continue;
				
				AddressTag prevPrevState = null;
				AddressTag prevState = AddressTag.START;
				
				for (int i=0; i< address.size(); i++) {
					bigramTransitionCounts.increment(prevState.toString(), address.getKnownTags().get(i));
					if (prevPrevState != null)
						trigramTransitionCounts.increment(getTrigramKey(prevPrevState, prevState), address.getKnownTags().get(i));
					emissionCounts.increment(address.getKnownTags().get(i), address.getAddressTokens().get(i));
					prevPrevState = prevState;
					prevState = address.getKnownTags().get(i);
					tagCounter.increment(address.getKnownTags().get(i));
				}
				bigramTransitionCounts.increment(prevState.toString(), AddressTag.STOP);
				if (prevPrevState != null)
					trigramTransitionCounts.increment(getTrigramKey(prevPrevState, prevState), AddressTag.STOP);
			} catch (InputException ie) {}
		}

		bigramTransProb = bigramTransitionCounts.getProbabilityMaps();
		trigramTransProb = trigramTransitionCounts.getProbabilityMaps();
		emProb = emissionCounts.getProbabilityMaps();	
	}

	public List<AddressTag> viterbi(List<String> observations) {
		if (observations.size() == 0)
			return new ArrayList<AddressTag>();
		
		// Initialization step
		Map<String, ViterbiNode<String>> stateMap = new HashMap<String, ViterbiNode<String>>();
		List<Map<String, ViterbiNode<String>>> backPointer = new ArrayList<Map<String, ViterbiNode<String>>>();
		for (AddressTag state : AddressTag.values()) {
			double prob = getTransitionProb(null, AddressTag.START, state) * getEmissionProb(state, observations.get(0));
			String key = getTrigramKey(AddressTag.START,state);
			stateMap.put(key, new ViterbiNode<String>(prob, key, prob));
		}
		backPointer.add(stateMap);

		// Iteration step
		for (int i=1; i < observations.size(); i++) {
			String obs = observations.get(i);
			HashMap<String, ViterbiNode<String>> nextStates = new HashMap<String, ViterbiNode<String>>();
			for (AddressTag next : AddressTag.values()) {


				for (AddressTag previous : AddressTag.values()) {
					double stateTotal = 0.0;
					String maxArg = null;
					double stateMax = 0.0;
					for (AddressTag prevPrevious : AddressTag.values() ) {
						String key = getTrigramKey(prevPrevious, previous);
						ViterbiNode<String> node = stateMap.get(key);
						if (node == null)
							continue;
						double curProb = getEmissionProb(next, obs) * getTransitionProb(prevPrevious, previous, next);
						double totalProb = node.getTotalScore() * curProb;
						stateTotal += totalProb;
						if (totalProb > stateMax) {
							maxArg = key;
							stateMax = totalProb;
						}
					}
					nextStates.put(getTrigramKey(previous, next), new ViterbiNode<String>(stateTotal, maxArg, stateMax)); 
				}
				
				
			}
			backPointer.add(nextStates);
			stateMap = nextStates;
		}
		
		// Termination step
		HashMap<String, ViterbiNode<String>> nextStates = new HashMap<String, ViterbiNode<String>>();
		double stateTotal = 0.0;
		String maxArg = null;
		double stateMax = 0.0;

		for (AddressTag previous : AddressTag.values()) {

			for (AddressTag prevPrevious : AddressTag.values()) {
				String key = getTrigramKey(prevPrevious, previous);
				ViterbiNode<String> node = stateMap.get(key);
				if (node == null)
					continue;
				double totalProb = node.getTotalScore() * getTransitionProb(prevPrevious, previous, AddressTag.STOP);
				stateTotal += totalProb;
				if (totalProb > stateMax) {
					maxArg = key;
					stateMax = totalProb;
				}
			}
			
		}
		nextStates.put(AddressTag.STOP.toString(), new ViterbiNode<String>(stateTotal, maxArg, stateMax));

		backPointer.add(nextStates);
		stateMap = nextStates;
		
		List<AddressTag> maxStates = new ArrayList<AddressTag>();
		String nextState = AddressTag.STOP.toString();
		
		// Destroy the backpointer to get the list...
		while (backPointer.size() > 0) {
			if (!nextState.equals(AddressTag.STOP.toString())) {
				String key = nextState.split("-")[1];
				if (!key.equals(AddressTag.STOP.toString()))
					maxStates.add(0, AddressTag.valueOf(key));
			}
			Map<String,ViterbiNode<String>>nodes = backPointer.remove(backPointer.size()-1);
			ViterbiNode<String> max = nodes.get(nextState);
			nextState = max.getMaxState();
		}
		
		return maxStates;
	}

	@Override
	public void tagAddress(Address address) {
		List<AddressTag> tags = viterbi(address.getAddressTokens());
		
		for (int i=0; i< address.size(); i++) {
			address.setTag(i, tags.get(i));
		}
	}

}
