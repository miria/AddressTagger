package com.grunick.addresstagger.stat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContinuationCounter<S> {
	
	private Map<S, Set<S>> continuationCounts = new HashMap<S, Set<S>>();
	private Set<S> allPrevStates = new HashSet<S>();
	
	public void add(S currentState, S prevState) {
		if (!continuationCounts.containsKey(currentState)) {
			continuationCounts.put(currentState, new HashSet<S>());
		}
		continuationCounts.get(currentState).add(prevState);
		allPrevStates.add(prevState);
	}
	
	public double getContinuationProb(S state) {
		if (!continuationCounts.containsKey(state)) {
			return 0.0;
		}
		return (double)continuationCounts.get(state).size() / (double)allPrevStates.size();
	}

}
