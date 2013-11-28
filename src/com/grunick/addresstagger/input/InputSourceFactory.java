package com.grunick.addresstagger.input;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.grunick.addresstagger.input.InputConstants.FileConfig;

public class InputSourceFactory {
	
	private InputSourceFactory() {}
	
	public static FileInputSource makeFileInputSource(Map<String,String> config) throws InputException {
		String filename = validateString(config, FileConfig.FILENAME);
		String headerFile = validateString(config, FileConfig.HEADER_FILE);
		String delimiter = validateString(config, FileConfig.DELIMITER);
		
		return new FileInputSource(filename, headerFile, delimiter);
	}
	
	protected static String validateString(Map<String,String> config, String fieldName) throws InputException {
		String field = config.get(fieldName);
		if (StringUtils.isBlank(field))
			throw new InputException("Missing config value for input source: \""+fieldName+"\"");
		return field;
	}
	
	public static InputSource makeInputSource(String type, Map<String,String> config) throws InputException {
		if ("file".equalsIgnoreCase(type))
			return makeFileInputSource(config);
		if ("noop".equalsIgnoreCase(type))
			return new NoOpInputSource();
		return null;
	}

}
