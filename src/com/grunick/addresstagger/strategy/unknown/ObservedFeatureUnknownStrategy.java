package com.grunick.addresstagger.strategy.unknown;

import org.apache.commons.lang.StringUtils;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.stat.Counter;

public class ObservedFeatureUnknownStrategy implements UnknownStrategy {
	
	protected Counter<Boolean> hasNumberCounter = new Counter<Boolean>();
	protected Counter<Boolean> allCapsCounter = new Counter<Boolean>();
	protected Counter<Integer> wordLengthCounter = new Counter<Integer>();
	protected Counter<Boolean> isNumericCounter = new Counter<Boolean>();
	protected Counter<AddressTag> wordCounter = new Counter<AddressTag>();

	@Override
	public void train(Address address) {
		for (int i=0; i < address.size(); i++) {
			String token = address.getAddressTokens().get(i);
			wordLengthCounter.increment(token.length());
			allCapsCounter.increment(StringUtils.isAllUpperCase(token));
			isNumericCounter.increment(StringUtils.isNumeric(token));
			hasNumberCounter.increment(StringUtils.containsAny(token, "0123456789"));
			wordCounter.increment(address.getKnownTags().get(i));
		}
		
	}

	@Override
	public double getProbability(Address address, int idx, AddressTag prediction) {
		String token = address.getAddressTokens().get(idx);
		
		return wordCounter.getProbability(prediction) * wordLengthCounter.getProbability(token.length()) 
				* allCapsCounter.getProbability(StringUtils.isAllUpperCase(token))
				* isNumericCounter.getProbability(StringUtils.isNumeric(token))
				* hasNumberCounter.getProbability(StringUtils.containsAny(token, "0123456789"));
	}

}
