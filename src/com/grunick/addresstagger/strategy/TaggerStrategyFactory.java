package com.grunick.addresstagger.strategy;

import java.io.File;
import java.util.Map;

import com.grunick.addresstagger.input.InputConstants.StrategyTypes;
import com.grunick.addresstagger.input.InputException;

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
	
	
	public static MEMMStrategy getMEMMStrategy(Map<String,String> strategyConfig) throws InputException {
		File persistFile = new File(strategyConfig.get("persistFile"));
		if (!persistFile.canWrite())
			throw new InputException("Invalid persist file!");
		File entropyFile = new File(strategyConfig.get("entropyFile"));
		if (!entropyFile.canWrite())
			throw new InputException("Invalid entropy file!");
		return new MEMMStrategy(entropyFile, persistFile);
		
	}
}
