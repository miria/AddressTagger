package com.grunick.addresstagger;


import org.apache.commons.configuration.ConfigurationException;

import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.config.TaggerConfigBuilder;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.strategy.TaggerStrategy;

public class AddressTagger {
	
	protected TaggerConfig config;
	
	public AddressTagger(TaggerConfig config) {
		this.config = config;
	}
	
	public AddressTagger(String filename) throws ConfigurationException, InputException {
		this.config = TaggerConfigBuilder.loadConfiguration(filename);
		TaggerStrategy strategy = config.getTaggerStrategy();
		strategy.train(config.getTrainingData());
	}

}
