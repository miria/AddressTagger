package com.grunick.addresstagger.config;

import com.grunick.addresstagger.input.InputSource;
import com.grunick.addresstagger.strategy.TaggerStrategy;

public class TaggerConfig {
	
	protected InputSource trainingData;
	protected InputSource validationData;
	protected InputSource testData;
	
	protected boolean runValidation = false;
	protected boolean runTest = false;
	
	protected TaggerStrategy strategy;
	
	protected boolean verbose = false;
	
	public void setTrainingData(InputSource source) {
		this.trainingData = source;
	}
	
	public InputSource getTrainingData() {
		return trainingData;
	}

	public void setValidationData(InputSource source) {
		this.validationData = source;
	}
	
	public InputSource getValidationData() {
		return validationData;
	}
	
	public void setTestData(InputSource source) {
		this.testData = source;
	}
	
	public InputSource getTestData() {
		return testData;
	}
	
	public void setTestMode(boolean bool) {
		this.runTest = bool;
	}
	
	public boolean runTestMode() {
		return runTest;
	}
	
	public void setValidationMode(boolean bool) {
		this.runValidation = bool;
	}
	
	public boolean runValidationMode() {
		return runValidation;
	}
	
	public void setTaggerStrategy(TaggerStrategy strategy) {
		this.strategy = strategy;
	}
	
	public TaggerStrategy getTaggerStrategy() {
		return strategy;
	}
	
	public void setVerbose(boolean bool) {
		this.verbose = bool;
	}
	
	public boolean isVerbose() {
		return verbose;
	}
	
}
