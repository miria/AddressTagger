package com.grunick.addresstagger.tokenize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;

public class TaggedColumnTokenizer implements Tokenizer {
	
	protected List<String> columns;
	protected String nameColumn;
	protected int nameColumnIdx;
	protected List<AddressTag> tagOrder;
	protected Map<AddressTag, String> tagColumnMap;
	
	public TaggedColumnTokenizer(String nameColumn, Map<AddressTag, String> tagColumnMap, List<AddressTag> tagOrder) {
		this.tagColumnMap = tagColumnMap;
		this.tagOrder = tagOrder;
		this.nameColumn = nameColumn;
	}
 
	@Override
	public Address tokenizeAddress(List<String> row) throws InputException {
		String name = row.get(nameColumnIdx);
		ArrayList<String> nameTokens = tokenizeString(name);
		AddressTag[] knownTagArray = new AddressTag[nameTokens.size()];
		Arrays.fill(knownTagArray, AddressTag.UNK);
		List<AddressTag> knownTags = Arrays.asList(knownTagArray);
		
		// Iterate tags from left to right in address format, filling in
		// the first matching instance of the tag. 
		for (AddressTag tag : tagOrder) {
			if (!tagColumnMap.containsKey(tag))
				continue;
			int idx = columns.indexOf(tagColumnMap.get(tag));
			if (idx == -1)
				continue;
			ArrayList<String> rowTokens = tokenizeString(row.get(idx));			
			for (int i=0; i<nameTokens.size(); i++) {
				if (knownTags.get(i) != AddressTag.UNK)
					continue;
				int tIdx = rowTokens.indexOf(nameTokens.get(i));
				if (tIdx > -1) {
					knownTags.set(i, tag);
					rowTokens.remove(tIdx);
				}
			}
		}
		
		Address address = new Address(name, nameTokens, knownTags);

		for (AddressTag tag : address.getKnownTags()) {
			if (tag == AddressTag.UNK)
				throw new InputException("Could not classify all tags: "+name+" -> "+address.getKnownTags()+" "+row);
		}
		
		return address;
	}
	
	protected ArrayList<String> tokenizeString(String name) {
		if (StringUtils.isBlank(name))
			return new ArrayList<String>();
		
		name = name.replaceAll("\\-", " ");
		return new ArrayList<String>(Arrays.asList(StringUtils.split(name)));
	}

	@Override
	public void setColumns(List<String> columns) {
		this.columns = columns;	
		nameColumnIdx = columns.indexOf(nameColumn);
	}

}
