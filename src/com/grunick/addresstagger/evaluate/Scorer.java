package com.grunick.addresstagger.evaluate;

import com.grunick.addresstagger.data.Address;

public interface Scorer {

	public double scoreResult(Address address);
	public double getOverallScore();
}
