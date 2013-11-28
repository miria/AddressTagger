package com.grunick.addresstagger.data;

public class Term {
	
	protected int position;
	protected AddressTag tag = AddressTag.UNK;
	protected String term;
	
	
	public Term(String term, int position) {
		this.position = position;
		this.term = term;
	}
	
	public Term(String term, int position, AddressTag tag) {
		this.position = position;
		this.term = term;
		this.tag = tag;
	}
	
	public String getTerm() {
		return term;
	}
	
	public int getPosition() {
		return position;
	}
	
	public AddressTag getAddressTag() {
		return tag;
	}
	
	public void setAddressTag(AddressTag tag) {
		this.tag = tag;
	}

}
