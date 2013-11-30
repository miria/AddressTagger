package com.grunick.addresstagger.tokenize;

import java.util.List;

import com.grunick.addresstagger.data.Address;

public class NoOpTokenizer implements Tokenizer {

	@Override
	public Address tokenizeAddress(List<String> row) {
		return null; 
	}

	@Override
	public void setColumns(List<String> columns) {
		
	}

}
