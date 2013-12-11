package com.grunick.addresstagger.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aliasi.corpus.Corpus;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.crf.ChainCrf;
import com.aliasi.crf.ChainCrfFeatureExtractor;
import com.aliasi.io.LogLevel;
import com.aliasi.io.Reporter;
import com.aliasi.io.Reporters;
import com.aliasi.stats.AnnealingSchedule;
import com.aliasi.stats.RegressionPrior;
import com.aliasi.tag.Tagging;
import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;



public class CRFStrategy implements TaggerStrategy {
	
	protected ChainCrf<String> crfModel;
	
	public static class AddressCorpus extends Corpus<ObjectHandler<Tagging<String>>> {
		
		protected InputSource input;
		
		public AddressCorpus(InputSource input) {
			this.input = input;
		}
		
	    public void visitTrain(ObjectHandler<Tagging<String>> handler) {
	    	
			while (input.hasNext()) {
				try {
					Address address = input.getNext();
					if (address.size() == 0)
						continue;
					List<String> tags = new ArrayList<String>();
					for (AddressTag tag : address.getKnownTags())
						tags.add(tag.toString());
		            Tagging<String> tagging = new Tagging<String>(address.getAddressTokens(), tags);
		            handler.handle(tagging);
				} catch (InputException ie) {}
			}
	    }
	    
	    public void visitTest(ObjectHandler<Tagging<String>> handler) {
	        // Do nothing.
	    }
	}


	@Override
	public void train(InputSource source) throws InputException {
		Corpus<ObjectHandler<Tagging<String>>> corpus = new AddressCorpus(source);
        ChainCrfFeatureExtractor<String> featureExtractor= new AddressChainCrfFeatureExtractor();
        boolean addIntercept = true;  //t
        int minFeatureCount = 1;
        boolean cacheFeatures = false;
        boolean allowUnseenTransitions = true;
        double priorVariance = 4.0;
        boolean uninformativeIntercept = true; //t
        RegressionPrior prior = RegressionPrior.gaussian(priorVariance, uninformativeIntercept);
        int priorBlockSize = 1;  //3
        double initialLearningRate = 0.05;
        double learningRateDecay = 0.995;
        AnnealingSchedule annealingSchedule = AnnealingSchedule.exponential(initialLearningRate, learningRateDecay);

        double minImprovement = 0.00001;
        int minEpochs = 2;
        int maxEpochs = 2000;

        Reporter reporter = Reporters.stdOut().setLevel(LogLevel.TRACE);

		try {
			crfModel = ChainCrf.estimate(corpus, featureExtractor, addIntercept, minFeatureCount, cacheFeatures, allowUnseenTransitions,
			                    prior, priorBlockSize, annealingSchedule, minImprovement, minEpochs, maxEpochs, reporter);
		} catch (IOException e) {
			throw new InputException("Unable to generate CRF model : "+e.getMessage());
		}
		
	}

	@Override
	public void tagAddress(Address address) {
		
		Tagging<String> tags = crfModel.tag(address.getAddressTokens());
		for (int i=0; i<address.size(); i++) {
			try {
				address.setTag(i, AddressTag.valueOf(tags.tag(i)));
			} catch (IllegalArgumentException e) {
				System.out.println("CRF tagger can't find tag "+tags.tag(i));
				address.setTag(i, AddressTag.UNK);
			}
		}
	}



}
