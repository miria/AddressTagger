package com.grunick.addresstagger.mock;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputSource;

public class MockInputSource implements InputSource {

	@Override
	public void open() {
		
	}

	@Override
	public Address getNext() {
		return null;
	}

	@Override
	public void close() {

	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public int getRecordIndex() {
		return 0;
	}

}
