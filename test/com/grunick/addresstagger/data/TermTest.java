package com.grunick.addresstagger.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TermTest {
	
	@Test
	public void testTerm() {
		Term term = new Term("my term", 3);
		assertEquals(term.getTerm(), "my term");
	}
	
	@Test
	public void testPosition() {
		Term term = new Term("my term", 3);
		assertEquals(term.getPosition(), 3);
	}
	
	@Test
	public void testTagDefault() {
		Term term = new Term("my term", 3);
		assertEquals(term.getAddressTag(), AddressTag.UNK);
	}
	
	@Test
	public void testTag() {
		Term term = new Term("my term", 3);
		term.setAddressTag(AddressTag.AA1);
		assertEquals(term.getAddressTag(), AddressTag.AA1);
	}
	
	@Test
	public void testTagConstructor() {
		Term term = new Term("my term", 3, AddressTag.AA1);
		assertEquals(term.getAddressTag(), AddressTag.AA1);
	}

}
