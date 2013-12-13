package com.grunick.addresstagger.strategy.unknown;

public interface UnknownStrategy<S,T> {
	
	public void train(S state1, T state2);
	
	public double getProbability(S state1, T state2);	

}
