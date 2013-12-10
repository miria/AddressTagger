package com.grunick.addresstagger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.config.TaggerConfigBuilder;
import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.evaluate.AccuracyScorer;
import com.grunick.addresstagger.evaluate.Scorer;
import com.grunick.addresstagger.evaluate.UnknownWordAccuracyScorer;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public class AddressTaggerRunner {
	
	private static AddressTagger tagger;

	public static void main(String[] args) throws ConfigurationException, InputException {
		if (args.length < 1)
			throw new RuntimeException("Missing config file argument!");
		String configFile = args[0];
		// TODO: pass config in via command line...
		System.out.println("Loading configuration....");
		TaggerConfig config = TaggerConfigBuilder.loadConfiguration(configFile);
		tagger = new AddressTagger(config);
		
		System.out.println("Training the address tagger....");
		tagger.train();
		
		if (config.runValidationMode()) {
			System.out.println("Tagging the validation data....");

			double accuracy = executeBatch(config.getValidationData(), config.getTrainingData(), config.isVerbose());
			System.out.println("-------------------------------");
			System.out.println("Overall accuracy: "+accuracy);
			System.out.println("-------------------------------");
		}
		
		if (config.runTestMode()) {
			System.out.println("Tagging the test data....");
			
			System.out.println("-------------------------------");
			double accuracy = executeBatch(config.getTestData(), config.getTrainingData(), config.isVerbose());
			System.out.println("Overall accuracy: "+accuracy);
			System.out.println("-------------------------------");
		}
		System.out.println("Done!");


	}
	
	protected static double executeBatch(InputSource source, InputSource train,  boolean verbose) throws InputException {
		Scorer scorer = new AccuracyScorer();
		int counter = 0;
		HashSet<String> knownTerms = new HashSet<String>();
		train.reset();
		while (train.hasNext()) {
			Address address;
			try {
				address = train.getNext();
				knownTerms.addAll(address.getAddressTokens());
			} catch (InputException ie) {
				continue;
			}
		}
		Scorer unknownScorer = new UnknownWordAccuracyScorer(knownTerms);
		

		while (source.hasNext()) {
			Address address;
			try {
				address = source.getNext();
			} catch (InputException ie) {
				continue;
			}
			
			tagger.tagAddress(address);
			double score = scorer.scoreResult(address);
			unknownScorer.scoreResult(address);
			
			counter++;
			if (verbose) {
				System.out.println("Index:    "+counter);
				System.out.println("Address:  "+address.getFullName());
				System.out.println("Tokens:   "+address.getAddressTokens());
				System.out.println("Guess:    "+address.getGuessedTags());
				System.out.println("Gold:     "+address.getKnownTags());
				System.out.println("Accuracy: "+score);
				System.out.println("----------------------------------------");
			}
			if (counter % 10000 == 0)
				System.out.println("Tagged "+counter+" current accuracy = "+scorer.getOverallScore());
		}
		
		System.out.println("Tagged "+counter+" addresses");
		System.out.println("Unknown accuracy: "+unknownScorer.getOverallScore());

		return scorer.getOverallScore();

	}

}
