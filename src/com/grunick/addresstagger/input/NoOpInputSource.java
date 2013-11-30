package com.grunick.addresstagger.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.tokenize.Tokenizer;

public class NoOpInputSource implements InputSource {

	@Override
	public Address getNext() {
		return null;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public void close() {
	}
	
	@Override
	public void init() {
	}

	@Override
	public List<String> getColumnNames() {
		return new ArrayList<String>();
	}

	@Override
	public Map<String, AddressTag> getColumnToTagMap() {
		return new HashMap<String,AddressTag>();
	}

	@Override
	public void setTokenizer(Tokenizer tokenizer) {		
	}

}
