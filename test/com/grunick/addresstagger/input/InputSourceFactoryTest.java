package com.grunick.addresstagger.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.grunick.addresstagger.input.InputConstants.FileConfig;

public class InputSourceFactoryTest {
	
	@Test
	public void testGetUnknownInputSource() throws InputException {
		InputSource source = InputSourceFactory.makeInputSource("adsfds", new HashMap<String,String>());
		assertNull(source);
	}
	
	@Test
	public void testGetNoOpInputSource() throws InputException {
		InputSource source = InputSourceFactory.makeInputSource("noop", new HashMap<String,String>());
		assertTrue(source instanceof NoOpInputSource);
	}
	
	@Test
	public void testGetFileInputSourceValid() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put(FileConfig.DELIMITER, ",");
		config.put(FileConfig.FILENAME, "tmp");
		config.put(FileConfig.HEADER_FILE, "tmp");
		
		InputSource source = InputSourceFactory.makeInputSource("file", config);
		assertTrue(source instanceof FileInputSource);
	}
	
	@Test(expected=InputException.class)
	public void testGetFileInputSourceNoDelimiter() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put(FileConfig.FILENAME, "tmp");
		config.put(FileConfig.HEADER_FILE, "tmp");
		
		InputSource source = InputSourceFactory.makeInputSource("file", new HashMap<String,String>());
		assertTrue(source instanceof FileInputSource);
	}
	
	@Test(expected=InputException.class)
	public void testGetFileInputSourceNoFilename() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put(FileConfig.DELIMITER, ",");
		config.put(FileConfig.HEADER_FILE, "tmp");
		
		InputSource source = InputSourceFactory.makeInputSource("file", new HashMap<String,String>());
		assertTrue(source instanceof FileInputSource);
	}
	
	@Test(expected=InputException.class)
	public void testGetFileInputSourceNoHeaderFile() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put(FileConfig.DELIMITER, ",");
		config.put(FileConfig.FILENAME, "tmp");
		
		InputSource source = InputSourceFactory.makeInputSource("file", new HashMap<String,String>());
		assertTrue(source instanceof FileInputSource);
	}
	
	@Test(expected=InputException.class)
	public void validateStringInvalid() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put("key", " ");
		InputSourceFactory.validateString(config, "key");
	}
	
	@Test
	public void validateStringValid() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put("key", "test");
		String tmp = InputSourceFactory.validateString(config, "key");
		assertEquals(tmp, "test");
	}

}
