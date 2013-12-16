package com.grunick.addresstagger.strategy;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.grunick.addresstagger.data.Constants.StrategyTypes;
import com.grunick.addresstagger.input.InputException;

import org.junit.Test;

public class TaggerStrategyFactoryTest {
	
	@Test(expected=InputException.class)
	public void testUnknownStrategy() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		TaggerStrategyFactory.makeStrategy("dasfasdf", config);
	}

	
	@Test
	public void testNoOpStrategy() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy(StrategyTypes.NO_OP_STRATEGY, config);
		assertTrue(strategy instanceof NoOpTaggerStrategy);
	}
	
	@Test
	public void testMaxLikelihoodStrategy() throws InputException {
		Map<String,String> config = new HashMap<String,String>();
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy(StrategyTypes.MOST_LIKELY_TAG_STRATEGY, config);
		assertTrue(strategy instanceof MostLikelyTagStrategy);
	}
}
