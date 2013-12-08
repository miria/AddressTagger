package com.grunick.addresstagger.strategy;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.stat.Counter;
import com.grunick.addresstagger.stat.CounterMap;

/**
 * This is a very simple strategy that just tags each token
 * with its most likely tag based on the training data.
 * 
 * @author miria
 *
 */
public class MostLikelyTagStrategy implements TaggerStrategy {
	
	protected CounterMap<String, AddressTag> counters = new CounterMap<String, AddressTag>();
	protected Counter<AddressTag> unknownTermTags = new Counter<AddressTag>();

	@Override
	public void train(InputSource source) throws InputException {
		
		while (source.hasNext()) {
			try {
				Address address = source.getNext();
				for (int i=0; i< address.size(); i++) {
					counters.increment(address.getAddressTokens().get(i), address.getKnownTags().get(i));
					unknownTermTags.increment(address.getKnownTags().get(i));
				}
			} catch (InputException ie) {}
		}

	}

	@Override
	public void tagAddress(Address address) {
		for (int i=0; i < address.size(); i++) {
			String token = address.getAddressTokens().get(i);
			AddressTag tag;
			if (counters.containsKey(token)) {
				tag = counters.getCounter(token).argMax();	
			} else {
				tag = unknownTermTags.argMax();
			}
			address.setTag(i, tag);
		}		
	}

}
