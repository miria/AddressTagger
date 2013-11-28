package com.grunick.addresstagger.strategy;

import com.grunick.addresstagger.config.TaggerConfig;

public class TaggerStrategyFactory {

	private TaggerStrategyFactory() {}
	
	public static TaggerStrategy makeStrategy(String type, TaggerConfig config) {
		if ("maximumLikelihood".equalsIgnoreCase(type))
			return new MaximumLikelihoodStrategy();
		return null;
	}
}
