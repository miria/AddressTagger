package com.grunick.addresstagger.tokenize;

import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.AddressTag;

public class TokenizerFactory {
	
	private TokenizerFactory() {}
	
	public static Tokenizer makeTokenizer(String type, List<String> columns, Map<String,AddressTag> columnToAddressTags) {
		if ("noop".equalsIgnoreCase(type))
			return new NoOpTokenizer();
		if ("taggedColumn".equalsIgnoreCase(type))
			return new TaggedColumnTokenizer(columns, columnToAddressTags);
		return null;
	}
}