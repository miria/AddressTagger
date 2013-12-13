package com.grunick.addresstagger.strategy.unknown;


public class MostLikelyUnknownStrategy<S,T> implements UnknownStrategy<S,T> {
	

	@Override
	public void train(S state1, T state2) {

	}

	@Override
	public double getProbability(S state1, T state2) {
		return 0;
	}

}
