package com.grunick.addresstagger;


import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.strategy.TaggerStrategy;

public class AddressTagger {
	
	protected TaggerConfig config;
	protected TaggerStrategy strategy;
	
	public AddressTagger(TaggerConfig config) {
		this.config = config;
		strategy = config.getTaggerStrategy();
	}
	
	public void train() throws InputException {
		strategy.train(config.getTrainingData());
		strategy.processHeldOutData(config.getValidationData());
	}
	
	public void tagAddress(Address address) throws InputException {
		strategy.tagAddress(address);
	}

}
