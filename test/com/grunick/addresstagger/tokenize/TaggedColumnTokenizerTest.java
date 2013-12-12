package com.grunick.addresstagger.tokenize;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;


import org.junit.Test;


public class TaggedColumnTokenizerTest {
	
	
	protected TaggedColumnTokenizer getTokenizer() {
		return new TaggedColumnTokenizer(null, null, null);
	}
	
	@Test
	public void testTokenizeStringOneTerm() {
		TaggedColumnTokenizer tokenizer = getTokenizer();
		assertEquals(tokenizer.tokenizeString("term"), Arrays.asList(new String[] {"term"}));
	}
	
	@Test
	public void testTokenizeStringManyTerm() {
		TaggedColumnTokenizer tokenizer = getTokenizer();
		assertEquals(tokenizer.tokenizeString("Route  I-187   N"), 
				Arrays.asList(new String[] {"Route", "I", "187", "N"}));
	}

	
	@Test
	public void testTokenizeStringBlank() {
		TaggedColumnTokenizer tokenizer = getTokenizer();
		assertEquals(tokenizer.tokenizeString(" "), 
				Arrays.asList(new String[] {}));
	}
}
