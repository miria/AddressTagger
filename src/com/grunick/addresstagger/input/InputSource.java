package com.grunick.addresstagger.input;

import com.grunick.addresstagger.data.Address;

public interface InputSource {
	
	public void open();
	
	public Address getNext();
	
	public void close();
	
	public String getType();
	
	public int getRecordIndex();

}
