package com.grunick.addresstagger.evaluate;

import com.grunick.addresstagger.data.Address;

public interface Scorer {

	public String scoreResult(Address address);
	public String getOverallScore();
}
