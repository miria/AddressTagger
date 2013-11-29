package com.grunick.addresstagger.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.input.NoOpInputSource;
import com.grunick.addresstagger.strategy.NoOpTaggerStrategy;
import com.grunick.addresstagger.strategy.TaggerStrategy;

public class TaggerConfigTest {
	
	@Test
	public void testTrainingData() {
		InputSource source = new NoOpInputSource();
		TaggerConfig config = new TaggerConfig();
		config.setTrainingData(source);
		assertSame(source, config.getTrainingData());
	}
	
	@Test
	public void testValidationData() {
		InputSource source = new NoOpInputSource();
		TaggerConfig config = new TaggerConfig();
		config.setValidationData(source);
		assertSame(source, config.getValidationData());
	}
	
	@Test
	public void testTestData() {
		InputSource source = new NoOpInputSource();
		TaggerConfig config = new TaggerConfig();
		config.setTestData(source);
		assertSame(source, config.getTestData());
	}
	
	@Test
	public void testVerbose() {
		TaggerConfig config = new TaggerConfig();
		config.setVerbose(true);
		assertEquals(config.isVerbose(), true);
	}
	
	@Test
	public void testVerboseDefault() {
		TaggerConfig config = new TaggerConfig();
		assertEquals(config.isVerbose(), false);
	}
	
	@Test
	public void testRunValidation() {
		TaggerConfig config = new TaggerConfig();
		config.setValidationMode(true);
		assertEquals(config.runValidationMode(), true);
	}
	
	@Test
	public void testRunValidationDefault() {
		TaggerConfig config = new TaggerConfig();
		assertEquals(config.runValidationMode(), false);
	}
	
	@Test
	public void testRunTest() {
		TaggerConfig config = new TaggerConfig();
		config.setTestMode(true);
		assertEquals(config.runTestMode(), true);
	}
	
	@Test
	public void testRunTestDefault() {
		TaggerConfig config = new TaggerConfig();
		assertEquals(config.runTestMode(), false);
	}
	
	@Test
	public void testStrategy() {
		TaggerStrategy strategy = new NoOpTaggerStrategy();
		TaggerConfig config = new TaggerConfig();
		config.setTaggerStrategy(strategy);
		assertSame(strategy, config.getTaggerStrategy());
	}

}
