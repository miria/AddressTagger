package com.grunick.addresstagger.strategy.unknown;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;

public interface UnknownStrategy {
	
	public void train(Address address) throws InputException;
	
	public double getProbability(Address address, int index, AddressTag prediction) throws InputException;	

}
