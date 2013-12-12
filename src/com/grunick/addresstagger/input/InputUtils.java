package com.grunick.addresstagger.input;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

public class InputUtils {
	
	private InputUtils() {}
		
	public static String validateString(Map<String,String> config, String fieldName) throws InputException {
		String field = config.get(fieldName);
		if (StringUtils.isBlank(field))
			throw new InputException("Missing config value for input source: \""+fieldName+"\"");
		return field;
	}
	
	public static int parseInt(Map<String, String> config, String fieldName) throws InputException {
		try {
			return Integer.parseInt(config.get(fieldName));
		} catch (NumberFormatException e) {
			throw new InputException("Invalid integer value for \""+fieldName+"\"");
		}
	}
	
	public static double parseDouble(Map<String, String> config, String fieldName) throws InputException {
		try {
			return Double.parseDouble(config.get(fieldName));
		} catch (NumberFormatException e) {
			throw new InputException("Invalid integer value for \""+fieldName+"\"");
		}
	}
	
	public static Map<String,String> getSuffixMap(String prefix, Configuration properties) {
		Map<String,String> sourceConfig = new HashMap<String,String>();
		
		for (Iterator<String> keysIter = properties.getKeys(prefix); keysIter.hasNext(); ) {
			String key = keysIter.next();
			sourceConfig.put(key.substring(prefix.length()+1, key.length()), properties.getString(key));
		}
		
		return sourceConfig;
	}
	
	public static Map<String,String> getSuffixMap(String prefix, Map<String,String> config) {
		Map<String,String> sourceConfig = new HashMap<String,String>();
		
		for (String key: config.keySet()) {
			if (!key.contains(prefix))
				continue;
			sourceConfig.put(key.substring(prefix.length()+1, key.length()), config.get(key));
		}
		return sourceConfig;
	}

}
