package com.grunick.addresstagger.strategy;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public class MaximumLikelihoodStrategy implements TaggerStrategy {

	@Override
	public void train(InputSource source) throws InputException {
		
		while (source.hasNext()) {
			Address address = source.getNext();
		}

	}

}
