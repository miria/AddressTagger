package com.grunick.addresstagger.strategy.unknown;

import java.util.Map;

import com.grunick.addresstagger.data.Constants.UnknownWordConfig;
import com.grunick.addresstagger.data.Constants.UnknownWordTypes;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputUtils;

public class UnknownStrategyFactory {
	
	private UnknownStrategyFactory() {}
	
	public static UnknownStrategy getUnknownEmissionStrategy(String type, Map<String,String> config) throws InputException {
		if (UnknownWordTypes.SINGLETON_DIST_STRATEGY.equalsIgnoreCase(type))
			return new SingletonTagDistributionUnknownStrategy();
		if (UnknownWordTypes.FEATURE_STRATEGY.equalsIgnoreCase(type))
			return new ObservedFeatureUnknownStrategy();
		if (UnknownWordTypes.MAXENT_STRATEGY.equalsIgnoreCase(type))
			return new MaximumEntropyUnknownStrategy(config.get(UnknownWordConfig.ENTROPY_FILE), config.get(UnknownWordConfig.PERSIST_FILE));
		if (UnknownWordTypes.FIXED_VALUE_STRATEGY.equalsIgnoreCase(type))
			return new FixedValueUnknownStrategy(InputUtils.parseDouble(config, UnknownWordConfig.VALUE));
		throw new InputException("Invalid UnknownStrategy "+type);
	}

}
