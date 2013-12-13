package com.grunick.addresstagger.strategy.unknown;


public class FixedValueUnknownStrategy<S,T> implements UnknownStrategy<S,T> {
	
	private double value;
	
	public FixedValueUnknownStrategy(double value) {
		this.value = value;
	}

	@Override
	public void train(S state1, T state2) {}

	@Override
	public double getProbability(S state1, T state2) {
		return value;
	}

}
