package com.grunick.addresstagger.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import com.grunick.addresstagger.data.Constants.InputSourceTypes;
import com.grunick.addresstagger.strategy.NoOpTaggerStrategy;

public class TaggerConfigBuilderTest {
	
	@Test
	public void testParseBooleanKnown() throws ConfigurationException {
		Configuration conf = new BaseConfiguration();
		conf.setProperty("test", "true");
		assertEquals(TaggerConfigBuilder.parseBoolean("test", conf), true);
		
	}
	
	@Test(expected=ConfigurationException.class)
	public void testParseBooleanUnknown() throws ConfigurationException {
		Configuration conf = new BaseConfiguration();
		TaggerConfigBuilder.parseBoolean("adsfsad", conf);
	}
	
	@Test
	public void testLoadStrategyKnown() throws ConfigurationException {
		TaggerConfig taggerConf = new TaggerConfig();
		Configuration conf = new BaseConfiguration();
		conf.setProperty("strategy", InputSourceTypes.NO_OP_SOURCE);
		assertTrue(TaggerConfigBuilder.loadStrategy(conf, taggerConf) instanceof NoOpTaggerStrategy);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testLoadStrategyUnknown() throws ConfigurationException {
		TaggerConfig taggerConf = new TaggerConfig();
		Configuration conf = new BaseConfiguration();
		conf.setProperty("strategy", "dsfasd");
		TaggerConfigBuilder.loadStrategy(conf, taggerConf);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testLoadInputSourceUnknown() throws ConfigurationException {
		Configuration conf = new BaseConfiguration();
		conf.setProperty("my_stuff.type", "dsfasd");
		TaggerConfigBuilder.loadInputSource("my_stuff", conf);
	}
	
	@Test
	public void testLoadConfiguration() throws ConfigurationException {
		TaggerConfig config = TaggerConfigBuilder.loadConfiguration("test/data/test.properties");
		assertNotNull(config);
	}

	

}
