package com.grunick.addresstagger;

import org.apache.commons.configuration.ConfigurationException;

import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.config.TaggerConfigBuilder;
import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public class AddressTaggerRunner {
	
	private static AddressTagger tagger;

	public static void main(String[] args) throws ConfigurationException, InputException {
		// TODO: pass config in via command line...
		System.out.println("Loading configuration....");
		TaggerConfig config = TaggerConfigBuilder.loadConfiguration("/Users/miria/git/AddressTagger/conf/tagger.properties");
		tagger = new AddressTagger(config);
		
		System.out.println("Training the address tagger....");
		tagger.train();
		
		if (config.runValidationMode()) {
			System.out.println("Tagging the validation data....");

			double accuracy = executeBatch(config.getValidationData(), config.isVerbose());
			System.out.println("-------------------------------");
			System.out.println("Overall accuracy: "+accuracy);
			System.out.println("-------------------------------");
		}
		
		if (config.runTestMode()) {
			System.out.println("Tagging the test data....");

			double accuracy = executeBatch(config.getTestData(), config.isVerbose());
			System.out.println("-------------------------------");
			System.out.println("Overall accuracy: "+accuracy);
			System.out.println("-------------------------------");
		}
		System.out.println("Done!");


	}
	
	protected static double executeBatch(InputSource source,  boolean verbose) {
		AccuracyScorer scorer = new AccuracyScorer();
		int counter = 0;
		while (source.hasNext()) {
			Address address;
			try {
				address = source.getNext();
			} catch (InputException ie) {
				continue;
			}
			tagger.tagAddress(address);
			double score = scorer.scoreResult(address);
			if (verbose) {
				System.out.println("Index:    "+counter++);
				System.out.println("Address:  "+address.getFullName());
				System.out.println("Tokens:   "+address.getAddressTokens());
				System.out.println("Guess:    "+address.getGuessedTags());
				System.out.println("Gold:     "+address.getKnownTags());
				System.out.println("Accuracy: "+score);
				System.out.println("----------------------------------------");
			}
		}
		return scorer.getOverallScore();

	}

}
