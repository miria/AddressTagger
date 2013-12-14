package com.grunick.addresstagger.strategy.unknown;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.stat.Counter;


public class FullTagDistributionUnknownStrategy implements UnknownStrategy {
	
	protected Counter<AddressTag> counter = new Counter<AddressTag>();
	

	@Override
	public void train(Address address) {
		for (int i=0; i < address.size(); i++) {
			counter.increment(address.getKnownTags().get(i));
		}
	}

	@Override
	public double getProbability(Address address, int idx, AddressTag prediction) {
		return counter.getProbability(prediction);
	}

}
