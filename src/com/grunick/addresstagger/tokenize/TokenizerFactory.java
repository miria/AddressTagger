package com.grunick.addresstagger.tokenize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.data.Constants.TokenizerConfig;
import com.grunick.addresstagger.data.Constants.TokenizerTypes;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputUtils;



public class TokenizerFactory {
	
	private TokenizerFactory() {}
	
	public static Tokenizer makeTaggedColumnTokenizer(Map<String,String> tokenizerConfig) throws InputException {

		String nameColumn = InputUtils.validateString(tokenizerConfig, TokenizerConfig.NAME_COLUMN);
		List<AddressTag> tagOrder = parseTagOrder(tokenizerConfig);
		Map<AddressTag, String> columnToAddressTags = loadHeaderFile(tokenizerConfig);	
		
		return new TaggedColumnTokenizer(nameColumn, columnToAddressTags, tagOrder);
		
	}
	
	protected static List<AddressTag> parseTagOrder(Map<String,String> tokenizerConfig) throws InputException {
		String[] tags = InputUtils.validateString(tokenizerConfig, TokenizerConfig.TAG_ORDER).split(",");
		List<AddressTag> tagOrder = new ArrayList<AddressTag>();
		for (String tag : tags) {
			AddressTag at = AddressTag.valueOf(tag);
			if (at == null)
				throw new InputException("Unknown AddressTag type \""+tag+"\"");
			tagOrder.add(at);
		}
		return tagOrder;
	}
	
	protected static Map<AddressTag, String> loadHeaderFile(Map<String,String> tokenizerConfig) throws InputException {
		String path = InputUtils.validateString(tokenizerConfig, TokenizerConfig.HEADER_FILE);
		String delimiter = InputUtils.validateString(tokenizerConfig, TokenizerConfig.DELIMITER);
		File headerFile = new File(path);
		if (!headerFile.canRead())
			throw new InputException("Cannot read from file "+path);
		
		try {
			Map<AddressTag, String> tagColumnMap = new HashMap<AddressTag,String>();

			List<String> lines = FileUtils.readLines(headerFile);
			for (String line: lines) {
				String[] pieces = line.trim().split(delimiter);
				if (pieces.length != 2)
					continue;
				AddressTag tag = AddressTag.valueOf(pieces[1]);
				if (tag == null)
					continue;
				tagColumnMap.put(tag, pieces[0].trim());
			}
			return tagColumnMap;
		} catch (IOException e) {
			throw new InputException("Unable to read header file: "+e.getMessage());
		}
	}
	
	public static Tokenizer makeTokenizer(String type, Map<String,String> tokenizerConfig) throws InputException {
		if (TokenizerTypes.NO_OP_TOKENIZER.equalsIgnoreCase(type))
			return new NoOpTokenizer();
		if (TokenizerTypes.COLUMN_TOKENIZER.equalsIgnoreCase(type))
			return makeTaggedColumnTokenizer(tokenizerConfig);
		throw new InputException("Unknown Tokenizer "+type);
	}
}