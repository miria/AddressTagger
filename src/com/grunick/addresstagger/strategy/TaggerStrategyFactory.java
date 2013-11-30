package com.grunick.addresstagger.strategy;

import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.input.InputConstants.StrategyTypes;

public class TaggerStrategyFactory {

	private TaggerStrategyFactory() {}
	
	public static TaggerStrategy makeStrategy(String type, TaggerConfig config) {
		if (StrategyTypes.MAX_LIKELIHOOD_STRATEGY.equalsIgnoreCase(type))
			return new MaximumLikelihoodStrategy();
		if (StrategyTypes.NO_OP_STRATEGY.equalsIgnoreCase(type))
			return new NoOpTaggerStrategy();
		return null;
	}
}
