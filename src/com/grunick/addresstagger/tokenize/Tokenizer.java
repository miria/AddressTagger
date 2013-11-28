package com.grunick.addresstagger.tokenize;

import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.data.Term;

public interface Tokenizer {
	
	public List<Term> tokenizeName(String name, Map<AddressTag, List<String>> tagData);

}
