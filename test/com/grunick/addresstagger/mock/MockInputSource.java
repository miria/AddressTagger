package com.grunick.addresstagger.mock;

import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public class MockInputSource implements InputSource {


	@Override
	public Address getNext() {
		return null;
	}

	@Override
	public void close() {

	}

	@Override
	public int getRecordIndex() {
		return 0;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public List<String> getColumnNames() {
		return null;
	}

	@Override
	public Map<String, AddressTag> getColumnToTagMap() {
		return null;
	}

	@Override
	public void init() throws InputException {
		
	}

}
