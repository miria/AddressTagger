package com.grunick.addresstagger.tokenize;

import java.util.List;

import com.grunick.addresstagger.data.Address;

public class NoOpTokenizer implements Tokenizer {

	@Override
	public Address tokenizeName(String name, List<String> row) {
		return null; 
	}

}
