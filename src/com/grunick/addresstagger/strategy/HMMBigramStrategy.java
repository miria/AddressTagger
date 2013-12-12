package com.grunick.addresstagger.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.stat.CounterMap;
import com.grunick.addresstagger.strategy.unknown.UnknownStrategy;

public class HMMBigramStrategy implements TaggerStrategy {
	
	private Map<AddressTag, Map<AddressTag, Double>> transProb;
	private Map<AddressTag, Map<String, Double>> emProb;
	
	protected UnknownStrategy unknowns;
	
	public HMMBigramStrategy(UnknownStrategy unknowns) {
		this.unknowns = unknowns;
	}
	
	protected double getTransitionProb(AddressTag state1, AddressTag state2) {
		if (transProb.containsKey(state1) && transProb.get(state1).containsKey(state2))
			return transProb.get(state1).get(state2);
		return unknowns.getTransitionProb(state1, state2);
	}

	protected double getEmissionProb(AddressTag state, String observation) {
		if (emProb.containsKey(state) && emProb.get(state).containsKey(observation))
			return emProb.get(state).get(observation);
		return unknowns.getEmissionProb(state, observation);
	}

	public List<AddressTag> viterbi(List<String> observations) {
		if (observations.size() == 0)
			return new ArrayList<AddressTag>();
		
		// Initialization step
		Map<AddressTag, ViterbiNode<AddressTag>> stateMap = new HashMap<AddressTag, ViterbiNode<AddressTag>>();
		List<Map<AddressTag, ViterbiNode<AddressTag>>> backPointer = new ArrayList<Map<AddressTag, ViterbiNode<AddressTag>>>();
		for (AddressTag state : AddressTag.values()) {
			double prob = getTransitionProb(AddressTag.START, state) * getEmissionProb(state, observations.get(0));
			stateMap.put(state, new ViterbiNode<AddressTag>(prob, state, prob));
		}
		backPointer.add(stateMap);

		// Iteration step
		for (int i=1; i < observations.size(); i++) {
			String obs = observations.get(i);
			HashMap<AddressTag, ViterbiNode<AddressTag>> nextStates = new HashMap<AddressTag, ViterbiNode<AddressTag>>();
			for (AddressTag next : AddressTag.values()) {
				double stateTotal = 0.0;
				AddressTag maxArg = null;
				double stateMax = 0.0;

				for (AddressTag previous : AddressTag.values()) {
					ViterbiNode<AddressTag> node = stateMap.get(previous);

					double curProb = getEmissionProb(next, obs) * getTransitionProb(previous, next);
					double totalProb = node.getTotalScore() * curProb;
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
			double totalProb = node.getTotalScore() * getTransitionProb(previous, AddressTag.STOP);
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

	@Override
	public void train(InputSource source) throws InputException {
		CounterMap<AddressTag, AddressTag> transitionCounts = new CounterMap<AddressTag, AddressTag>();
		CounterMap<AddressTag, String> emissionCounts = new CounterMap<AddressTag, String>();
		
		while (source.hasNext()) {
			try {
				Address address = source.getNext();
				if (address.size() == 0)
					continue;
				
				AddressTag prevState = AddressTag.START;
				
				for (int i=0; i< address.size(); i++) {
					transitionCounts.increment(prevState, address.getKnownTags().get(i));
					emissionCounts.increment(address.getKnownTags().get(i), address.getAddressTokens().get(i));
					prevState = address.getKnownTags().get(i);
				}
				transitionCounts.increment(prevState, AddressTag.STOP);
			} catch (InputException ie) {}
		}
		

		transProb = transitionCounts.getProbabilityMaps();
		emProb = emissionCounts.getProbabilityMaps();	
	}

	@Override
	public void tagAddress(Address address) {
		List<AddressTag> tags = viterbi(address.getAddressTokens());
		
		for (int i=0; i< address.size(); i++) {
			address.setTag(i, tags.get(i));
		}
	}

}
