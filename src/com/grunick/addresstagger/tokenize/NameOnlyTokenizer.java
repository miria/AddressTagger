package com.grunick.addresstagger.tokenize;

import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.data.Term;

public class NameOnlyTokenizer implements Tokenizer {

	@Override
	public List<Term> tokenizeName(String name, Map<AddressTag, List<String>> tagData) {
		return null;
	}

}
