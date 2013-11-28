package com.grunick.addresstagger.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.BooleanUtils;

import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.input.InputSourceFactory;
import com.grunick.addresstagger.strategy.TaggerStrategy;
import com.grunick.addresstagger.strategy.TaggerStrategyFactory;

public class TaggerConfigBuilder {
	
	private TaggerConfigBuilder() {}
	
	public static TaggerConfig loadConfiguration(String filename) throws ConfigurationException {
		TaggerConfig config = new TaggerConfig();
		Configuration properties = new PropertiesConfiguration(filename);
		
		config.setValidationMode(parseBoolean("run_validation", properties));
		config.setTestMode(parseBoolean("run_test", properties));
		config.setVerbose(parseBoolean("verbose", properties));
		
		config.setTrainingData(loadInputSource("train_data", properties));
		if (config.runTestMode())
			config.setTestData(loadInputSource("test_data", properties));
		if (config.runValidationMode())
			config.setValidationData(loadInputSource("validate_data", properties));
		
		config.setTaggerStrategy(loadStrategy(properties, config));
		
		return config;
	}
	
	protected static boolean parseBoolean(String key, Configuration properties) throws ConfigurationException {
		if (!properties.containsKey(key))
			throw new ConfigurationException("Could not find mandatory configuration for \""+key+"\"");
		return BooleanUtils.toBoolean(properties.getString(key));

	}

	protected static InputSource loadInputSource(String keyPrefix, Configuration properties) throws ConfigurationException {
		String type = properties.getString(keyPrefix+".type");
		try {
			if ("file".equalsIgnoreCase(type)) {
				String filename = properties.getString(keyPrefix+".filename");
				String headerFile = properties.getString(keyPrefix+".header_file");
				return InputSourceFactory.makeFileInputSource(filename, headerFile);
			}
		} catch (InputException i) {
			throw new ConfigurationException("Unable to instantiate store: "+i.getMessage());
		}
		throw new ConfigurationException("Unknown input source type \""+type+"\"");
	}
	
	protected static TaggerStrategy loadStrategy(Configuration properties, TaggerConfig config) throws ConfigurationException {
		String type = properties.getString("strategy");
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy(type, config);
		if (strategy == null)
			throw new ConfigurationException("Unknown tagger strategy \""+type+"\"");
		return strategy;
		
	}

}
