package com.grunick.addresstagger.strategy.unknown;

import java.util.Map;

import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputConstants.UnknownWordConfig;
import com.grunick.addresstagger.input.InputConstants.UnknownWordTypes;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputUtils;

public class UnknownStrategyFactory {
	
	private UnknownStrategyFactory() {}
	
	public static UnknownStrategy<AddressTag,AddressTag> getUnknownTransitionStrategy(String type, Map<String,String> config) throws InputException {
		if (UnknownWordTypes.MOST_LIKELY_STRATEGY.equalsIgnoreCase(type))
			return new MostLikelyUnknownStrategy<AddressTag,AddressTag>();
		if (UnknownWordTypes.MAXENT_STRATEGY.equalsIgnoreCase(type))
			return new MaximumEntropyUnknownStrategy<AddressTag,AddressTag>(config.get(UnknownWordConfig.ENTROPY_FILE), config.get(UnknownWordConfig.PERSIST_FILE));
		if (UnknownWordTypes.FIXED_VALUE_STRATEGY.equalsIgnoreCase(type))
			return new FixedValueUnknownStrategy<AddressTag,AddressTag>(InputUtils.parseDouble(config, UnknownWordConfig.VALUE));
		throw new InputException("Invalid UnknownStrategy "+type);
	}
	
	public static UnknownStrategy<AddressTag, String> getUnknownEmissionStrategy(String type, Map<String,String> config) throws InputException {
		if (UnknownWordTypes.MOST_LIKELY_STRATEGY.equalsIgnoreCase(type))
			return new MostLikelyUnknownStrategy<AddressTag, String>();
		if (UnknownWordTypes.MAXENT_STRATEGY.equalsIgnoreCase(type))
			return new MaximumEntropyUnknownStrategy<AddressTag, String>(config.get(UnknownWordConfig.ENTROPY_FILE), config.get(UnknownWordConfig.PERSIST_FILE));
		if (UnknownWordTypes.FIXED_VALUE_STRATEGY.equalsIgnoreCase(type))
			return new FixedValueUnknownStrategy<AddressTag, String>(InputUtils.parseDouble(config, UnknownWordConfig.VALUE));
		throw new InputException("Invalid UnknownStrategy "+type);
	}

}
