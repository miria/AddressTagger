package com.grunick.addresstagger.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Viterbi<S, O> {

	private Map<S, Map<S, Double>> transProb;
	private Map<S, Map<O, Double>> emProb;
	private List<S> knownStates;
	private S startState;
	private S stopState;

	private class ViterbiNode {
		private double totalScore;
		private S maxState;
		private double maxScore;

		public ViterbiNode(double totalScore, S maxState, double maxScore) {
			this.totalScore = totalScore;
			this.maxState = maxState;
			this.maxScore = maxScore;
		}

		public double getTotalScore() {
			return totalScore;
		}

		public S getMaxState() {
			return maxState;
		}

		public double getMaxScore() {
			return maxScore;
		}
		
		public String toString() {
			return "total="+totalScore+";max="+maxState+","+maxScore;
		}
	}

	public Viterbi(Map<S, Map<S, Double>> transmissionProbabilities,
			Map<S, Map<O, Double>> emissionProbabilities, List<S> knownStates,
			S startState, S stopState) {
		this.transProb = transmissionProbabilities;
		this.emProb = emissionProbabilities;
		this.knownStates = knownStates;
		this.startState = startState;
		this.stopState = stopState;
	}

	// TODO: Better unknown state model
	protected double getTransitionProb(S state1, S state2) {
		if (!transProb.containsKey(state1))
			return 0.0000001;
		if (!transProb.get(state1).containsKey(state2))
			return 0.0000001;
		return transProb.get(state1).get(state2);
	}

	// TODO: Better unknown term model
	protected double getEmissionProb(S state, O observation) {
		if (!emProb.containsKey(state))
			return 0.0000001;
		if (!emProb.get(state).containsKey(observation))
			return 0.0000001;
		return emProb.get(state).get(observation);
	}

	public List<S> findMaxStates(List<O> observations) {
		if (observations.size() == 0)
			return new ArrayList<S>();
		
		// Initialization step
		Map<S, ViterbiNode> stateMap = new HashMap<S, ViterbiNode>();
		List<Map<S, ViterbiNode>> backPointer = new ArrayList<Map<S, ViterbiNode>>();
		for (S state : knownStates) {
			double prob = getTransitionProb(startState, state) * getEmissionProb(state, observations.get(0));
			stateMap.put(state, new ViterbiNode(prob, state, prob));
		}
		backPointer.add(stateMap);

		// Iteration step
		for (int i=1; i < observations.size(); i++) {
			O obs = observations.get(i);
			HashMap<S, ViterbiNode> nextStates = new HashMap<S, ViterbiNode>();
			for (S next : knownStates) {
				double stateTotal = 0.0;
				S maxArg = null;
				double stateMax = 0.0;

				for (S previous : knownStates) {
					ViterbiNode node = stateMap.get(previous);

					double curProb = getEmissionProb(next, obs) * getTransitionProb(previous, next);
					double totalProb = node.getTotalScore() * curProb;
					stateTotal += totalProb;
					if (totalProb > stateMax) {
						maxArg = previous;
						stateMax = totalProb;
					}
				}
				nextStates.put(next, new ViterbiNode(stateTotal, maxArg, stateMax)); 
				
			}
			backPointer.add(nextStates);
			stateMap = nextStates;
		}
		
		// Termination step
		HashMap<S, ViterbiNode> nextStates = new HashMap<S, ViterbiNode>();
		double stateTotal = 0.0;
		S maxArg = null;
		double stateMax = 0.0;

		for (S previous : knownStates) {
			ViterbiNode node = stateMap.get(previous);
			double totalProb = node.getTotalScore() * getTransitionProb(previous, stopState);
			stateTotal += totalProb;
			if (totalProb > stateMax) {
				maxArg = previous;
				stateMax = totalProb;
			}
		}
		nextStates.put(stopState, new ViterbiNode(stateTotal, maxArg, stateMax));

		backPointer.add(nextStates);
		stateMap = nextStates;
		
		List<S> maxStates = new ArrayList<S>();
		S nextState = stopState;
		
		// Destroy the backpointer to get the list...
		while (backPointer.size() > 0) {
			
			maxStates.add(0, nextState);
			Map<S,ViterbiNode>nodes = backPointer.remove(backPointer.size()-1);
			ViterbiNode max = nodes.get(nextState);
			nextState = max.getMaxState();
		}
		
		return maxStates;
	}

}
