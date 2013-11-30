package com.grunick.addresstagger.input;

import java.util.Map;

import com.grunick.addresstagger.input.InputConstants.FileConfig;
import com.grunick.addresstagger.input.InputConstants.InputSourceTypes;

public class InputSourceFactory {
	
	private InputSourceFactory() {}
	
	public static FileInputSource makeFileInputSource(Map<String,String> config) throws InputException {
		String filename = InputUtils.validateString(config, FileConfig.FILENAME);
		String delimiter = InputUtils.validateString(config, FileConfig.DELIMITER);
		
		return new FileInputSource(filename, delimiter);
	}
	
	public static InputSource makeInputSource(String type, Map<String,String> config) throws InputException {
		if (InputSourceTypes.FILE_SOURCE.equalsIgnoreCase(type))
			return makeFileInputSource(config);
		if (InputSourceTypes.NO_OP_SOURCE.equalsIgnoreCase(type))
			return new NoOpInputSource();
		return null;
	}

}
