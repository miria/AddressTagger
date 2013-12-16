package com.grunick.addresstagger.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Address {
	
	protected String fullName;	
	protected List<String> addressTokens;
	protected List<AddressTag> knownTags;
	protected List<AddressTag> guessedTags;
	protected long classifyTime;
	
	public Address(String fullName, List<String> nameTokens, List<AddressTag> knownTags) {
		this.fullName = fullName;
		this.addressTokens = nameTokens;
		this.knownTags = knownTags;
		AddressTag[] unknownTags = new AddressTag[addressTokens.size()];
		Arrays.fill(unknownTags, AddressTag.UNK);
		this.guessedTags = new ArrayList<AddressTag>(Arrays.asList(unknownTags));
	}
	
	public List<String> getAddressTokens() {
		return Collections.unmodifiableList(addressTokens);
	}
	
	public String getToken(int idx) {
		return addressTokens.get(idx);
	}
	
	public AddressTag getKnownTag(int idx) {
		return knownTags.get(idx);
	}
	
	
	public List<AddressTag> getKnownTags() {
		return Collections.unmodifiableList(knownTags);
	}
	
	public List<AddressTag> getGuessedTags() {
		return Collections.unmodifiableList(guessedTags);
	}
	
	public void setTag(int position, AddressTag tag) {
		guessedTags.set(position, tag);
	}
	
	public int size() {
		return addressTokens.size();
	}

	public String getFullName() {
		return fullName;
	}
	
	public void setClassifyTime(long time) {
		this.classifyTime = time;
	}
	
	public long getClassifyTime() {
		return classifyTime;
	}


}
