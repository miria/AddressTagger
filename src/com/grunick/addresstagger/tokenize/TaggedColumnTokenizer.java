package com.grunick.addresstagger.tokenize;

import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;

public class TaggedColumnTokenizer implements Tokenizer {
	
	protected List<String> columns;
	protected Map<String, AddressTag> columnsToAddressTags;
	
	public TaggedColumnTokenizer(List<String> columns, Map<String, AddressTag> columnsToAddressTags) {
		this.columns = columns;
		this.columnsToAddressTags = columnsToAddressTags;
	}
 
	@Override
	public Address tokenizeName(String name, List<String> row) {
		return null;
	}

}
