package com.grunick.addresstagger.strategy;

public class ViterbiNode<S> {
	
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
