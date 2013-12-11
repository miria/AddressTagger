package com.grunick.addresstagger.evaluate;

import com.grunick.addresstagger.data.Address;

public class ResponseTimeScorer implements Scorer {
	
	protected long totalTime = 0;
	protected long count = 0;

	@Override
	public String scoreResult(Address address) {
		long classifyTime = address.getClassifyTime();
		totalTime += classifyTime;
		count += 1;
		return "Classify time: "+totalTime;
	}

	@Override
	public String getOverallScore() {
		return "Average classify time: "+((double)totalTime)/((double)count);
	}

}
