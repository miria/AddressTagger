package com.grunick.addresstagger.strategy.unknown;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.stat.Counter;

public class MostLikelyUnknownStrategy implements UnknownStrategy {
	
	protected Counter<AddressTag> tagCounter = new Counter<AddressTag>();

	@Override
	public void train(Address address) {
		for (AddressTag tag : address.getKnownTags())
			tagCounter.increment(tag);
	}

	@Override
	public double getEmissionProb(AddressTag state, String observation) {
		return 0;
	}

	@Override
	public double getTransitionProb(AddressTag state1, AddressTag state2) {
		return 0;
	}

}
