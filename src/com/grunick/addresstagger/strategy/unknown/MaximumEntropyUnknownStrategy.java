package com.grunick.addresstagger.strategy.unknown;

import opennlp.model.MaxentModel;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;

public class MaximumEntropyUnknownStrategy implements UnknownStrategy {
	
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
	public void train(Address address) {
		fetchInit = false;
		if (!trainInit) 
			initializeTraining();
		

	}
	
	protected void initializeTraining() {
		
		
		trainInit = true;
	}
	
	protected void initializeMaxEnt() {
		
		fetchInit = true;
	}

	@Override
	public double getEmissionProb(AddressTag state, String observation) {
		if (!fetchInit) 
			initializeMaxEnt();
		
		return 0;
	}

	@Override
	public double getTransitionProb(AddressTag state1, AddressTag state2) {
		if (!fetchInit) 
			initializeMaxEnt();
		return 0;
	}

}
