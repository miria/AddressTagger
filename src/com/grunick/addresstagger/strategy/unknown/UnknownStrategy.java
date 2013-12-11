package com.grunick.addresstagger.strategy.unknown;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;

public interface UnknownStrategy {
	
	public void train(Address address);
	
	public double getEmissionProb(AddressTag state, String observation);
	
	public double getTransitionProb(AddressTag state1, AddressTag state2);
	

}
