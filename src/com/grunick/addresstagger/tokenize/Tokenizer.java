package com.grunick.addresstagger.tokenize;

import java.util.List;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputException;

public interface Tokenizer {

	public Address tokenizeAddress(List<String> row) throws InputException;
	
	public void setColumns(List<String> columns);

}
