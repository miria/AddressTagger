package com.grunick.addresstagger.tokenize;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.grunick.addresstagger.input.InputConstants.TokenizerTypes;
import com.grunick.addresstagger.input.InputException;

public class TokenizerFactoryTest {
	
	@Test
	public void testMakeTokenizerUnknown() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		Tokenizer tokenizer = TokenizerFactory.makeTokenizer("adsf", config);
		assertNull(tokenizer);
	}
	
	@Test
	public void testMakeTokenizerNoOp() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		Tokenizer tokenizer = TokenizerFactory.makeTokenizer(TokenizerTypes.NO_OP_TOKENIZER, config);
		assertTrue(tokenizer instanceof NoOpTokenizer);
	}
	
	@Test
	public void testMakeTokenizerTaggedColumn() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		Tokenizer tokenizer = TokenizerFactory.makeTokenizer(TokenizerTypes.COLUMN_TOKENIZER, config);
		assertTrue(tokenizer instanceof TaggedColumnTokenizer);
	}

}
