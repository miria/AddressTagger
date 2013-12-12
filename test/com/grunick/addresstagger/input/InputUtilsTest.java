package com.grunick.addresstagger.input;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
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
	
	
	@Test
	public void testGetSuffixMapConfig() {
		Configuration conf = new BaseConfiguration();
		conf.setProperty("prefix.key1", "value1");
		conf.setProperty("prefix.key2", "value2");
		conf.setProperty("notprefix.key3", "value3");
		
		Map<String,String> config = InputUtils.getSuffixMap("prefix", conf);
		assertEquals(config.get("key1"), "value1");
		assertEquals(config.get("key2"), "value2");
		assertEquals(config.get("key3"), null);
		
	}
	

}
