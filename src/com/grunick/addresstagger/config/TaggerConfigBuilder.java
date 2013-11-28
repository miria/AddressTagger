package com.grunick.addresstagger.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
		Map<String,String> sourceConfig = getInputSourceConfig(keyPrefix, properties);

		try {
			InputSource source = InputSourceFactory.makeInputSource(type, sourceConfig);
			if (source == null)
				throw new ConfigurationException("Unknown input source type \""+type+"\"");
			return source;
		} catch (InputException e) {
			throw new ConfigurationException("Error creating source: "+e.getMessage());
		}

	}
	
	protected static TaggerStrategy loadStrategy(Configuration properties, TaggerConfig config) throws ConfigurationException {
		String type = properties.getString("strategy");
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy(type, config);
		if (strategy == null)
			throw new ConfigurationException("Unknown tagger strategy \""+type+"\"");
		return strategy;
		
	}
	
	protected static Map<String,String> getInputSourceConfig(String prefix, Configuration properties) {
		Map<String,String> sourceConfig = new HashMap<String,String>();
		
		for (Iterator<String> keysIter = properties.getKeys(prefix); keysIter.hasNext(); ) {
			String key = keysIter.next();
			sourceConfig.put(key.substring(prefix.length()+1, key.length()), properties.getString(key));
		}
		
		return sourceConfig;
	}

}
