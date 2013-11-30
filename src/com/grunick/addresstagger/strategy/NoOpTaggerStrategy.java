package com.grunick.addresstagger.strategy;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputSource;

public class NoOpTaggerStrategy implements TaggerStrategy {

	@Override
	public void train(InputSource source) {

	}

	@Override
	public void tagAddress(Address address) {
		
	}

}
