package com.grunick.addresstagger.tokenize;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.grunick.addresstagger.data.AddressTag;

public class TokenizerFactoryTest {
	
	@Test
	public void testMakeTokenizerUnknown() {
		List<String> columns = new ArrayList<String>();
		Map<String,AddressTag> columnsToAddressTag = new HashMap<String,AddressTag>();
		Tokenizer tokenizer = TokenizerFactory.makeTokenizer("adsf", columns, columnsToAddressTag);
		assertNull(tokenizer);
	}
	
	@Test
	public void testMakeTokenizerNoOp() {
		List<String> columns = new ArrayList<String>();
		Map<String,AddressTag> columnsToAddressTag = new HashMap<String,AddressTag>();
		Tokenizer tokenizer = TokenizerFactory.makeTokenizer("noop", columns, columnsToAddressTag);
		assertTrue(tokenizer instanceof NoOpTokenizer);
	}
	
	@Test
	public void testMakeTokenizerTaggedColumn() {
		List<String> columns = new ArrayList<String>();
		Map<String,AddressTag> columnsToAddressTag = new HashMap<String,AddressTag>();
		Tokenizer tokenizer = TokenizerFactory.makeTokenizer("taggedColumn", columns, columnsToAddressTag);
		assertTrue(tokenizer instanceof TaggedColumnTokenizer);
	}

}
