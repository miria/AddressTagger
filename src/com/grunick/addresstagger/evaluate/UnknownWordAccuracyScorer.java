package com.grunick.addresstagger.evaluate;

import java.util.Set;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;

public class UnknownWordAccuracyScorer implements Scorer {

	private int globalCorrect = 0;
	private int globalTotal = 0;
	private Set<String> knownTerms;
	
	public UnknownWordAccuracyScorer(Set<String> terms) {
		knownTerms = terms;
	}
	 
	
	public String scoreResult(Address address) {
		int correct = 0;
		int total = 0;
		for (int i=0; i < address.getAddressTokens().size(); i++) {
			if (knownTerms.contains(address.getAddressTokens().get(i)))
				continue;
			AddressTag guess = address.getGuessedTags().get(i);
			AddressTag gold = address.getKnownTags().get(i);
			total++;
			globalTotal++;
			if (guess == gold) {
				correct++;
				globalCorrect++;
			}
		}
		return "Unknown word accuracy: "+((double)correct) / ((double)total);
	}
	
	public String getOverallScore() {
		System.out.println("global c "+globalCorrect+" global t "+ globalTotal);
		return "Overall unknown word accuracy: "+((double)globalCorrect) / ((double)globalTotal);
	}
}
