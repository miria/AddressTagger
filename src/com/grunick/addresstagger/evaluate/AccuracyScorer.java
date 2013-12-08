package com.grunick.addresstagger.evaluate;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;


public class AccuracyScorer {
	
	private int globalCorrect = 0;
	private int globalTotal = 0;
	
	public double scoreResult(Address address) {
		int correct = 0;
		int total = 0;
		for (int i=0; i < address.getAddressTokens().size(); i++) {
			AddressTag guess = address.getGuessedTags().get(i);
			AddressTag gold = address.getKnownTags().get(i);
			total++;
			globalTotal++;
			if (guess == gold) {
				correct++;
				globalCorrect++;
			}
		}
		return ((double)correct) / ((double)total);
	}
	
	public double getOverallScore() {
		return ((double)globalCorrect) / ((double)globalTotal);
	}

}
