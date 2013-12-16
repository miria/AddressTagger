package com.grunick.addresstagger.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.stat.ContinuationCounter;
import com.grunick.addresstagger.stat.Counter;
import com.grunick.addresstagger.stat.CounterMap;
import com.grunick.addresstagger.strategy.unknown.UnknownStrategy;

public class KNInterpolatedHMMStrategy implements TaggerStrategy {

	CounterMap<AddressTag, AddressTag> bigramTransitionCounts = new CounterMap<AddressTag, AddressTag>();
	CounterMap<String, AddressTag> trigramTransitionCounts = new CounterMap<String, AddressTag>();
	private Counter<AddressTag> tagCounter = new Counter<AddressTag>();
	private Map<AddressTag, Map<String, Double>> emProb;
	private ContinuationCounter<AddressTag> unigramContCounts = new ContinuationCounter<AddressTag>();
	
	private ContinuationCounter<String> bigramContCounts = new ContinuationCounter<String>();
	private Map<AddressTag,Integer> bigramDistinctCounts = new HashMap<AddressTag, Integer>();
	private Map<String,Integer> trigramDistinctCounts = new HashMap<String, Integer>();
	
	protected UnknownStrategy emissionUnknowns;
	
	private static final double absoluteBigramDiscount = 0.75;
	private static final double absoluteTrigramDiscount = 0.75;
	
	public KNInterpolatedHMMStrategy(UnknownStrategy emissionUnknowns) {
		this.emissionUnknowns = emissionUnknowns;
	}
	
	protected double getTransitionProb(AddressTag prevPrevState, AddressTag prevState, AddressTag state) {
		// TODO: should we use continuation counts for bigram transition?
		Counter<AddressTag> bigramCounter = bigramTransitionCounts.getCounter(prevState);
		double bigramProb = bigramCounter != null ? bigramCounter.getProbability(state, absoluteBigramDiscount) : 0.000001;
		double bigramTotal = bigramCounter == null ? 1 : bigramCounter.getTotal();
		double distinctCounts = bigramDistinctCounts.containsKey(state) ? bigramDistinctCounts.get(state) : 0.000001;
		double remainingWeight = (absoluteBigramDiscount/bigramTotal) * distinctCounts; 
		double bigramKNProb = bigramProb + unigramContCounts.getContinuationProb(state) * remainingWeight;
		
		double trigramKNProb = 0.0;
		if (prevPrevState != null ) {
			String key = getTrigramKey(prevPrevState, prevState);
			Counter<AddressTag> trigramCounter = trigramTransitionCounts.getCounter(key);
			double trigramProb = trigramCounter != null ? trigramCounter.getProbability(state, absoluteTrigramDiscount) : 0.000001;
			double trigramTotal = trigramCounter == null ? 1 : trigramCounter.getTotal();
			distinctCounts = trigramDistinctCounts.containsKey(state) ? trigramDistinctCounts.get(state) : 0.000001;
			remainingWeight = (absoluteTrigramDiscount/trigramTotal) * distinctCounts; 
			trigramKNProb = trigramProb + bigramContCounts.getContinuationProb(key) * remainingWeight;
		}
		
		return trigramKNProb + bigramKNProb;

	}

	protected double getEmissionProb(Address address, int idx, AddressTag state) throws InputException {
		String observation = address.getAddressTokens().get(idx);
		if (emProb.containsKey(state) && emProb.get(state).containsKey(observation))
			return emProb.get(state).get(observation);
		return emissionUnknowns.getProbability(address, idx, state);
	}
	
	protected String getTrigramKey(AddressTag prevTag, AddressTag tag) {
		return prevTag.toString()+"-"+tag.toString();
	}
	
	@Override
	public void train(InputSource source) throws InputException {
		CounterMap<AddressTag, String> emissionCounts = new CounterMap<AddressTag, String>();
		
		while (source.hasNext()) {
			try {
				Address address = source.getNext();
				if (address.size() == 0)
					continue;
				
				AddressTag prevPrevState = null;
				AddressTag prevState = AddressTag.START;
				emissionUnknowns.train(address);
				
				for (int i=0; i< address.size(); i++) {
					AddressTag currentState = address.getKnownTag(i);
					unigramContCounts.add(currentState, prevState);
					bigramTransitionCounts.increment(prevState, currentState);
					
					if (prevPrevState != null) {
						String prevStates = getTrigramKey(prevPrevState, prevState);
						bigramContCounts.add(currentState.toString(), prevStates);
						trigramTransitionCounts.increment(prevStates, currentState);
					} 
					emissionCounts.increment(address.getKnownTag(i), address.getToken(i));
					prevPrevState = prevState;
					prevState = currentState;
					tagCounter.increment(currentState);
				}
				unigramContCounts.add(AddressTag.STOP, prevState);
				bigramContCounts.add(AddressTag.STOP.toString(), getTrigramKey(prevPrevState, prevState));
				bigramTransitionCounts.increment(prevState, AddressTag.STOP);
				if (prevPrevState != null)
					trigramTransitionCounts.increment(getTrigramKey(prevPrevState, prevState), AddressTag.STOP);
			} catch (InputException ie) {}
		}

		for (AddressTag tag : bigramTransitionCounts.keySet()) {
			Set<Integer> distinctCounts = new HashSet<Integer>();
			for (Integer value : bigramTransitionCounts.getCounter(tag).getCountMap().values())
				distinctCounts.add(value);
			bigramDistinctCounts.put(tag, distinctCounts.size());
		}
		
		for (String tag : trigramTransitionCounts.keySet()) {
			Set<Integer> distinctCounts = new HashSet<Integer>();
			for (Integer value : trigramTransitionCounts.getCounter(tag).getCountMap().values())
				distinctCounts.add(value);
			trigramDistinctCounts.put(tag, distinctCounts.size());
		}

		emProb = emissionCounts.getProbabilityMaps();	
	}

	public List<AddressTag> viterbi(Address address) throws InputException {
		List<String> observations = address.getAddressTokens();
		if (observations.size() == 0)
			return new ArrayList<AddressTag>();
		
		// Initialization step
		Map<String, ViterbiNode<String>> stateMap = new HashMap<String, ViterbiNode<String>>();
		List<Map<String, ViterbiNode<String>>> backPointer = new ArrayList<Map<String, ViterbiNode<String>>>();
		for (AddressTag state : AddressTag.values()) {
			double prob = getTransitionProb(null, AddressTag.START, state) * getEmissionProb(address, 0, state);
			String key = getTrigramKey(AddressTag.START,state);
			stateMap.put(key, new ViterbiNode<String>(prob, key, prob));
		}
		backPointer.add(stateMap);

		// Iteration step
		for (int i=1; i < observations.size(); i++) {
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
						double curProb = getEmissionProb(address, i, next) * getTransitionProb(prevPrevious, previous, next);
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
	public void tagAddress(Address address) throws InputException {
		List<AddressTag> tags = viterbi(address);
		
		for (int i=0; i< address.size(); i++) {
			address.setTag(i, tags.get(i));
		}
	}
	
	@Override
	public void processHeldOutData(InputSource source) throws InputException {}

}
