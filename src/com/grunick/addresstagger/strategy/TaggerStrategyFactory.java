package com.grunick.addresstagger.strategy;

import java.io.File;
import java.util.Map;

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
}
