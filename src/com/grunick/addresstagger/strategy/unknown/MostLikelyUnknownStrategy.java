package com.grunick.addresstagger.strategy.unknown;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;

public class MostLikelyUnknownStrategy implements UnknownStrategy {

	@Override
	public void train(Address address) {

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
