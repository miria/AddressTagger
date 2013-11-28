package com.grunick.addresstagger.data;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AddressTest {
	
	@Test
	public void testFullName() {
		List<Term> terms = Arrays.asList(new Term[] {new Term("123", 0, AddressTag.NUM), 
			      new Term("Main", 1, AddressTag.STR)});
		Address address = new Address("123 Main", terms);
		
		assertEquals(address.getFullName(), "123 Main");
	}
	
	@Test
	public void testTerms() {
		List<Term> terms = Arrays.asList(new Term[] {new Term("123", 0, AddressTag.NUM), 
			      new Term("Main", 1, AddressTag.STR)});
		Address address = new Address("123 Main", terms);
				
		assertEquals(address.getAddressTokens(), terms);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testTermsUnmodifiable() {
		List<Term> terms = Arrays.asList(new Term[] {new Term("123", 0, AddressTag.NUM), 
			      new Term("Main", 1, AddressTag.STR)});
		Address address = new Address("123 Main", terms);
				
		address.getAddressTokens().add(new Term("St", 1, AddressTag.SFX));
	}
	
	@Test
	public void testSize() {
		List<Term> terms = Arrays.asList(new Term[] {new Term("123", 0, AddressTag.NUM), 
			      new Term("Main", 1, AddressTag.STR)});
		Address address = new Address("123 Main", terms);
				
		assertEquals(address.size(), 2);
	}

}
