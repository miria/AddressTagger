package com.grunick.addresstagger.tokenize;

import java.util.List;

import com.grunick.addresstagger.data.Address;

public interface Tokenizer {

	public Address tokenizeName(String name, List<String> row);

}
