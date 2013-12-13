package com.grunick.addresstagger.strategy.unknown;

import opennlp.model.MaxentModel;


public class MaximumEntropyUnknownStrategy<S,T> implements UnknownStrategy<S,T> {
	
	protected MaxentModel maxent;
	protected String entropyFile;
	protected String persistFile;
	protected boolean trainInit = false;
	protected boolean fetchInit = false;
	
	public MaximumEntropyUnknownStrategy(String entropyFile, String persistFile) {
		this.entropyFile = entropyFile;
		this.persistFile = persistFile;
	}
	
	@Override
	public void train(S state1, T state2) {}

	@Override
	public double getProbability(S state1, T state2) {
		return -1;
	}




}
