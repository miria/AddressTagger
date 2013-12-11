package com.grunick.addresstagger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.config.TaggerConfigBuilder;
import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.evaluate.OverallAccuracyScorer;
import com.grunick.addresstagger.evaluate.ResponseTimeScorer;
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
		System.out.println("Loading configuration....");
		TaggerConfig config = TaggerConfigBuilder.loadConfiguration(configFile);
		tagger = new AddressTagger(config);
		
		System.out.println("Training the address tagger....");
		tagger.train();
		
		if (config.runValidationMode()) {
			System.out.println("Tagging the validation data....");

			System.out.println("-------------------------------");
			executeBatch(config.getValidationData(), config.getTrainingData(), config.isVerbose());
			System.out.println("-------------------------------");
		}
		
		if (config.runTestMode()) {
			System.out.println("Tagging the test data....");
			
			System.out.println("-------------------------------");
			executeBatch(config.getTestData(), config.getTrainingData(), config.isVerbose());
			System.out.println("-------------------------------");
		}
		System.out.println("Done!");


	}
	
	protected static void executeBatch(InputSource source, InputSource train,  boolean verbose) throws InputException {
		
		int counter = 0;
		int error = 0;
		HashSet<String> knownTerms = new HashSet<String>();
		List<Scorer> scorers = new ArrayList<Scorer>();
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
		scorers.add(new OverallAccuracyScorer());
		scorers.add(new UnknownWordAccuracyScorer(knownTerms));
		scorers.add(new ResponseTimeScorer());
		
		List<String> scores;

		while (source.hasNext()) {
			
			Address address;
			try {
				address = source.getNext();
			} catch (InputException ie) {
				error++;
				continue;
			}
			scores = new ArrayList<String>();
			long start = System.currentTimeMillis();
			tagger.tagAddress(address);
			address.setClassifyTime(System.currentTimeMillis()-start);
			for (Scorer scorer : scorers)
				scores.add(scorer.scoreResult(address));
			
			counter++;
			if (verbose) {
				System.out.println("Index:    "+counter);
				System.out.println("Address:  "+address.getFullName());
				System.out.println("Tokens:   "+address.getAddressTokens());
				System.out.println("Guess:    "+address.getGuessedTags());
				System.out.println("Gold:     "+address.getKnownTags());
				for (String score : scores)
					System.out.println(score);
				System.out.println("----------------------------------------");
			}
			if (counter % 10000 == 0) {
				StringBuilder builder = new StringBuilder("Tagged");
				builder.append(counter).append(" ");
				for (Scorer scorer : scorers)
					builder.append(scorer.getOverallScore()).append(" ");
			}
		}
		
		System.out.println("Tagged "+counter+" addresses, with "+error+" errors");
		for (Scorer scorer : scorers)
			System.out.println(scorer.getOverallScore());


	}

}
