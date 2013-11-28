package com.grunick.addresstagger.input;

import com.grunick.addresstagger.data.Address;

public class FileInputSource implements InputSource {
	
	public FileInputSource(String filename, String headerFile) {
		
	}

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
	
	public String getType() {
		return "FileInputSource";
	}

	@Override
	public int getRecordIndex() {
		return 0;
	}

}
