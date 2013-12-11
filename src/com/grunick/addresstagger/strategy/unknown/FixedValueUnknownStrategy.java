package com.grunick.addresstagger.strategy.unknown;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;

public class FixedValueUnknownStrategy implements UnknownStrategy {
	
	private double value;
	
	public FixedValueUnknownStrategy(double value) {
		this.value = value;
	}

	@Override
	public void train(Address address) {}

	@Override
	public double getEmissionProb(AddressTag state, String observation) {
		return value;
	}

	@Override
	public double getTransitionProb(AddressTag state1, AddressTag state2) {
		return value;
	}

}
