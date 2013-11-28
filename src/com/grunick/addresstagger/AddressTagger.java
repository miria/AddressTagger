package com.grunick.addresstagger;


import org.apache.commons.configuration.ConfigurationException;

import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.config.TaggerConfigBuilder;

public class AddressTagger {
	
	protected TaggerConfig config;
	
	public AddressTagger(TaggerConfig config) {
		this.config = config;
	}
	
	public AddressTagger(String filename) throws ConfigurationException {
		this.config = TaggerConfigBuilder.loadConfiguration(filename);
	}

}
