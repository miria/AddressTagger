package com.grunick.addresstagger.input;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class InputUtils {
	
	private InputUtils() {}
		
	public static String validateString(Map<String,String> config, String fieldName) throws InputException {
		String field = config.get(fieldName);
		if (StringUtils.isBlank(field))
			throw new InputException("Missing config value for input source: \""+fieldName+"\"");
		return field;
	}

}
