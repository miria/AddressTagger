package com.grunick.addresstagger.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.BooleanUtils;

import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.input.InputSourceFactory;
import com.grunick.addresstagger.input.InputUtils;
import com.grunick.addresstagger.strategy.TaggerStrategy;
import com.grunick.addresstagger.strategy.TaggerStrategyFactory;
import com.grunick.addresstagger.tokenize.Tokenizer;
import com.grunick.addresstagger.tokenize.TokenizerFactory;

public class TaggerConfigBuilder {
	
	private TaggerConfigBuilder() {}
	
	public static TaggerConfig loadConfiguration(String filename) throws ConfigurationException {
		
		TaggerConfig config = new TaggerConfig();
		PropertiesConfiguration properties = new PropertiesConfiguration();
		properties.setDelimiterParsingDisabled(true);

		try {
			properties.load(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			throw new ConfigurationException("Unable to load the file \""+filename+"\"");
		}
		
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
		String sourceType = properties.getString(keyPrefix+".type");
		String tokenizerType = properties.getString(keyPrefix+".tokenizer.type");
		Map<String,String> sourceConfig = InputUtils.getSuffixMap(keyPrefix, properties);
		Map<String,String> tokenizerConfig = InputUtils.getSuffixMap(keyPrefix+".tokenizer", properties);

		try {
			InputSource source = InputSourceFactory.makeInputSource(sourceType, sourceConfig);
			if (source == null)
				throw new ConfigurationException("Unknown input source type \""+sourceType+"\"");
			source.init();
			
			Tokenizer tokenizer = TokenizerFactory.makeTokenizer(tokenizerType, tokenizerConfig);
			if (tokenizer == null)
				throw new ConfigurationException("Unknown tokenizer type \""+tokenizer+"\"");
			tokenizer.setColumns(source.getColumnNames());
			source.setTokenizer(tokenizer);
			
			return source;
		} catch (InputException e) {
			throw new ConfigurationException("Error creating source: "+e.getMessage());
		}

	}
	
	protected static TaggerStrategy loadStrategy(Configuration properties, TaggerConfig config) throws ConfigurationException {
		String type = properties.getString("strategy");
		Map<String,String> strategyConfig = InputUtils.getSuffixMap("strategy.conf", properties);
		TaggerStrategy strategy;
		try {
			strategy = TaggerStrategyFactory.makeStrategy(type, strategyConfig);
			if (strategy == null)
				throw new ConfigurationException("Unknown tagger strategy \""+type+"\"");
			return strategy;
		} catch (InputException e) {
			throw new ConfigurationException("Error creating strategy: "+e.getMessage());
		}

		
	}

}
