package com.grunick.addresstagger.input;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class InputUtilsTest {
	
	@Test(expected=InputException.class)
	public void validateStringInvalid() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put("key", " ");
		InputUtils.validateString(config, "key");
	}
	
	@Test
	public void validateStringValid() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put("key", "test");
		String tmp = InputUtils.validateString(config, "key");
		assertEquals(tmp, "test");
	}

}
