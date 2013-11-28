package com.grunick.addresstagger.data;

import java.util.Collections;
import java.util.List;

public class Address {
	
	protected String fullName;	
	protected List<Term> addressTokens;
	
	public Address(String fullName, List<Term> addressTokens) {
		this.fullName = fullName;
		this.addressTokens = addressTokens;
	}
	
	public List<Term> getAddressTokens() {
		return Collections.unmodifiableList(addressTokens);
	}
	
	public int size() {
		return addressTokens.size();
	}

	public String getFullName() {
		return fullName;
	}

}
