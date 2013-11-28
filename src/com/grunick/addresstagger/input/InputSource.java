package com.grunick.addresstagger.input;

import java.util.List;
import java.util.Map;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;

public interface InputSource {
		
	public Address getNext() throws InputException;
	
	public boolean hasNext();
	
	public void close();
	
	public void init() throws InputException;
		
	public int getRecordIndex();
	
	public List<String> getColumnNames();
	
	public Map<String, AddressTag> getColumnToTagMap();

}
