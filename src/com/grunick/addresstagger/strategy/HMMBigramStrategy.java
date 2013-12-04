package com.grunick.addresstagger.strategy;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.stat.CounterMap;

public class HMMBigramStrategy implements TaggerStrategy {
	
	protected Viterbi<AddressTag, String> viterbi;

	@Override
	public void train(InputSource source) throws InputException {
		CounterMap<AddressTag, AddressTag> transitionCounts = new CounterMap<AddressTag, AddressTag>();
		CounterMap<AddressTag, String> emissionCounts = new CounterMap<AddressTag, String>();
		
		while (source.hasNext()) {
			try {
				Address address = source.getNext();
				if (address.size() == 0)
					continue;
				
				AddressTag prevState = AddressTag.START;
				
				for (int i=0; i< address.size(); i++) {
					transitionCounts.increment(prevState, address.getKnownTags().get(i));
					emissionCounts.increment(address.getKnownTags().get(i), address.getAddressTokens().get(i));
					prevState = address.getKnownTags().get(i);
				}
				transitionCounts.increment(prevState, AddressTag.STOP);
			} catch (InputException ie) {}
		}
		
		EnumSet<AddressTag> values = EnumSet.allOf(AddressTag.class);
		List<AddressTag> knownStates = Arrays.asList(values.toArray(new AddressTag[] {}));
		viterbi = new Viterbi<AddressTag, String>(transitionCounts.getProbabilityMaps(), emissionCounts.getProbabilityMaps(),
				knownStates, AddressTag.START, AddressTag.STOP);		
	}

	@Override
	public void tagAddress(Address address) {
		List<AddressTag> tags = viterbi.findMaxStates(address.getAddressTokens());
		
		for (int i=0; i< address.size(); i++) {
			address.setTag(i, tags.get(i));
		}
		
		
	}

}
