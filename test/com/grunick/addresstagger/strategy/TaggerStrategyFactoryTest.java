package com.grunick.addresstagger.strategy;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.grunick.addresstagger.config.TaggerConfig;
import com.grunick.addresstagger.input.InputConstants.StrategyTypes;

import org.junit.Test;

public class TaggerStrategyFactoryTest {
	
	@Test
	public void testUnknownStrategy() {
		TaggerConfig config = new TaggerConfig();
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy("dasfasdf", config);
		assertNull(strategy);
	}

	
	@Test
	public void testNoOpStrategy() {
		TaggerConfig config = new TaggerConfig();
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy(StrategyTypes.NO_OP_STRATEGY, config);
		assertTrue(strategy instanceof NoOpTaggerStrategy);
	}
	
	@Test
	public void testMaxLikelihoodStrategy() {
		TaggerConfig config = new TaggerConfig();
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy(StrategyTypes.MOST_FREQUENT_STRATEGY, config);
		assertTrue(strategy instanceof MostFrequentTagStrategy);
	}
}
