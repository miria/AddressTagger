package com.grunick.addresstagger.data;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AddressTest {
	
	protected Address getAddress() {
		List<String> addressTokens = Arrays.asList(new String[]{"123", "Main", "St"});
		List<AddressTag> tagList = Arrays.asList(new AddressTag[] {AddressTag.NUM, AddressTag.STR, AddressTag.SFX});
		return new Address("123 Main St", addressTokens, tagList);
	}
	
	@Test
	public void testFullName() {
		Address address = getAddress();
		assertEquals(address.getFullName(), "123 Main St");
	}
	
	@Test
	public void testKnownTags() {
		Address address = getAddress();
		assertEquals(address.getKnownTags(), Arrays.asList(new AddressTag[]{AddressTag.NUM, AddressTag.STR, AddressTag.SFX}));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testKnownTagsUnmodifiable() {
		Address address = getAddress();
		address.getKnownTags().add(AddressTag.UNK);
	}
	
	@Test
	public void testGuessedTags() {
		Address address = getAddress();
		assertEquals(address.getGuessedTags(), Arrays.asList(new AddressTag[]{AddressTag.UNK, AddressTag.UNK, AddressTag.UNK}));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testGuessedTagsUnmodifiable() {
		Address address = getAddress();
		address.getGuessedTags().add(AddressTag.UNK);
	}
	
	@Test
	public void testAddressTokens() {
		Address address = getAddress();
		assertEquals(address.getAddressTokens(), Arrays.asList(new String[] {"123", "Main", "St"}) );
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testAddressTokensUnmodifiable() {
		Address address = getAddress();
		address.getAddressTokens().add("token");
	}
	
	@Test
	public void testSize() {
		Address address = getAddress();
		assertEquals(address.size(), 3);
	}

	@Test
	public void testAddGuessedTagValid() {
		Address address = getAddress();
		address.setTag(1, AddressTag.STR);
		assertEquals(address.getGuessedTags(), Arrays.asList(new AddressTag[]{AddressTag.UNK, AddressTag.STR, AddressTag.UNK}));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testAddGuessedTagInvalid() {
		Address address = getAddress();
		address.setTag(7, AddressTag.STR);
	}
	
	
}
