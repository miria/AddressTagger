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
	public double getProbability(Address address, int index, AddressTag prediction) {
		return value;
	}

}
