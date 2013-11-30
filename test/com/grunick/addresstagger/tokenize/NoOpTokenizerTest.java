package com.grunick.addresstagger.tokenize;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;

public class NoOpTokenizerTest {
	
	@Test
	public void testNoOpTokenizer() {
		NoOpTokenizer tokenizer = new NoOpTokenizer();
		assertNull(tokenizer.tokenizeAddress(new ArrayList<String>()));
	}

}
