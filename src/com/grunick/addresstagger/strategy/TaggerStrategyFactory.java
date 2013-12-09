package com.grunick.addresstagger.strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.grunick.addresstagger.input.InputConstants.StrategyConfig;
import com.grunick.addresstagger.input.InputConstants.StrategyTypes;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputUtils;

public class TaggerStrategyFactory {

	private TaggerStrategyFactory() {}
	
	public static TaggerStrategy makeStrategy(String type, Map<String,String> strategyConfig) throws InputException {
		if (StrategyTypes.MOST_LIKELY_TAG_STRATEGY.equalsIgnoreCase(type))
			return new MostLikelyTagStrategy();
		if (StrategyTypes.NO_OP_STRATEGY.equalsIgnoreCase(type))
			return new NoOpTaggerStrategy();
		if (StrategyTypes.HMM_BIGRAM_STRATEGY.equalsIgnoreCase(type))
			return new HMMBigramStrategy();
		if (StrategyTypes.HMM_TRIGRAM_STRATEGY.equalsIgnoreCase(type))
			return new HMMTrigramStrategy();
		if (StrategyTypes.MEMM_STRATEGY.equalsIgnoreCase(type))
			return getMEMMStrategy(strategyConfig);
		if (StrategyTypes.TBL_STRATEGY.equalsIgnoreCase(type))
			return getTBLStrategy(strategyConfig);
		if (StrategyTypes.CRF_STRATEGY.equalsIgnoreCase(type))
			return new CRFStrategy();
		return null;
	}
	
	
	public static MEMMStrategy getMEMMStrategy(Map<String,String> config) throws InputException {
		if (!config.containsKey(StrategyConfig.PERSIST_FILE))
			throw new InputException("Undefined persist file!");
		File persistFile = new File(config.get(StrategyConfig.PERSIST_FILE));
		if (!config.containsKey(StrategyConfig.ENTROPY_FILE))
			throw new InputException("Undefined entropy file!");
		File entropyFile = new File(config.get(StrategyConfig.ENTROPY_FILE));
		int cutoff = InputUtils.parseInt(config, StrategyConfig.CUTOFF);
		int iterations = InputUtils.parseInt(config, StrategyConfig.ITERATIONS);
		return new MEMMStrategy(entropyFile, persistFile, iterations, cutoff);
		
	}
	
	public static TBLStrategy getTBLStrategy(Map<String,String> config) throws InputException {
		if (!config.containsKey(StrategyConfig.GUESSER_FILE))
			throw new InputException("Undefined guesser file!");
		String guesserFile = config.get(StrategyConfig.GUESSER_FILE);
		
		if (!config.containsKey(StrategyConfig.RULE_FILE))
			throw new InputException("Undefined rule file!");
		String ruleFile = config.get(StrategyConfig.RULE_FILE);
		
		if (!config.containsKey(StrategyConfig.LEX_FILE))
			throw new InputException("Undefined lex file!");
		String lexFile = config.get(StrategyConfig.LEX_FILE);
		
		if (!config.containsKey(StrategyConfig.TEMPLATE_FILE))
			throw new InputException("Undefined template file!");
		String templateFile = config.get(StrategyConfig.TEMPLATE_FILE);
		
		if (!config.containsKey(StrategyConfig.CORPUS_FILE))
			throw new InputException("Undefined corpus file!");
		String corpusFile = config.get(StrategyConfig.CORPUS_FILE);
		
		return new TBLStrategy(corpusFile, templateFile, ruleFile, lexFile, guesserFile);
	}
	
}
