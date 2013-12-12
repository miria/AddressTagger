package com.grunick.addresstagger.input;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.grunick.addresstagger.input.InputConstants.FileConfig;

public class InputSourceFactoryTest {
	
	@Test(expected=InputException.class)
	public void testGetUnknownInputSource() throws InputException {
		InputSourceFactory.makeInputSource("adsfds", new HashMap<String,String>());
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
		
		InputSource source = InputSourceFactory.makeInputSource("file", config);
		assertTrue(source instanceof FileInputSource);
	}
	
	@Test(expected=InputException.class)
	public void testGetFileInputSourceNoDelimiter() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put(FileConfig.FILENAME, "tmp");
		
		InputSource source = InputSourceFactory.makeInputSource("file", new HashMap<String,String>());
		assertTrue(source instanceof FileInputSource);
	}
	
	@Test(expected=InputException.class)
	public void testGetFileInputSourceNoFilename() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		config.put(FileConfig.DELIMITER, ",");
		
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
	


}
