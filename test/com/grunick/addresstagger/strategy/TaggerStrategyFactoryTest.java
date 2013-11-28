package com.grunick.addresstagger.strategy;

import static org.junit.Assert.assertNull;

import com.grunick.addresstagger.config.TaggerConfig;

import org.junit.Test;

public class TaggerStrategyFactoryTest {
	
	@Test
	public void testUnknownStrategy() {
		TaggerConfig config = new TaggerConfig();
		TaggerStrategy strategy = TaggerStrategyFactory.makeStrategy("dasfasdf", config);
		assertNull(strategy);
	}

}
