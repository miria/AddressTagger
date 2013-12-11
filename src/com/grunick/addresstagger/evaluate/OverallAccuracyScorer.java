package com.grunick.addresstagger.evaluate;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;


public class OverallAccuracyScorer implements Scorer {
	
	private int globalCorrect = 0;
	private int globalTotal = 0;
	
	public String scoreResult(Address address) {
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
		double score = ((double)correct) / ((double)total);
		return "Accuracy: "+score;
	}
	
	public String getOverallScore() {
		return "Overall Accuracy over "+globalTotal+" words: "+((double)globalCorrect) / ((double)globalTotal);
	}

}
