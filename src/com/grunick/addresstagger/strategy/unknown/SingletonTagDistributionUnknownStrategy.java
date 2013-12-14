package com.grunick.addresstagger.strategy.unknown;

import java.util.HashMap;
import java.util.Map;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.stat.Counter;

public class SingletonTagDistributionUnknownStrategy implements UnknownStrategy {

	protected Counter<AddressTag> counter = null;
	protected Map<String, Integer> wordCount = new HashMap<String,Integer>();
	protected Map<String, AddressTag> wordToTagMap = new HashMap<String, AddressTag>();

	@Override
	public void train(Address address) {
		for (int i=0; i < address.size(); i++) {
			String token = address.getAddressTokens().get(i);
			AddressTag tag = address.getKnownTags().get(i);
			if (!wordCount.containsKey(token)) {
				wordToTagMap.put(token, tag);
				wordCount.put(token, 0);
			}
			wordCount.put(token, wordCount.get(token)+1);
		}
	}

	@Override
	public double getProbability(Address address, int idx, AddressTag prediction) {
		if (counter == null) {
			counter = new Counter<AddressTag>();
			for (String key : wordCount.keySet()) {
				if (wordCount.get(key) > 1)
					continue;
				counter.increment(wordToTagMap.get(key));
			}
		}
		
		return counter.getProbability(prediction);
	}
}
