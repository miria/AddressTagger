package com.grunick.addresstagger.strategy;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public interface TaggerStrategy {
	
	public void train(InputSource source) throws InputException;
	
	public void tagAddress(Address address) throws InputException;
	
	

}
