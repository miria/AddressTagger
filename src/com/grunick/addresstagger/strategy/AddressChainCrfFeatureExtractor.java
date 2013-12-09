package com.grunick.addresstagger.strategy;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.aliasi.crf.ChainCrfFeatureExtractor;
import com.aliasi.crf.ChainCrfFeatures;

public class AddressChainCrfFeatureExtractor implements ChainCrfFeatureExtractor<String>, Serializable {

	private static final long serialVersionUID = 8545681040708379578L;

	public ChainCrfFeatures<String> extract(List<String> tokens, List<String> tags) {
		return new AddressChainCrfFeatures(tokens,tags);
	}
	
	static class AddressChainCrfFeatures extends ChainCrfFeatures<String> {

		public AddressChainCrfFeatures(List<String> tokens, List<String> tags) {
			super(tokens,tags);
		}
		
		public Map<String,Integer> nodeFeatures(int n) {
			return Collections.singletonMap("TOK_" + token(n), Integer.valueOf(1));
		}
		
		public Map<String,Integer> edgeFeatures(int n, int k) {
			return Collections.singletonMap("PREV_TOK_" + token(k), Integer.valueOf(1));
		}
	}
}