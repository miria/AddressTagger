package com.grunick.addresstagger.strategy;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ViterbiTest {
	
	protected Map<String, Map<String,Double>> getTransmissionProbabilities() {
		Map<String,Map<String, Double>> transmissionProbability = new HashMap<String, Map<String,Double>>();
		
		Map<String,Double> startMap = new HashMap<String,Double>();
		startMap.put("Healthy", 0.6);
		startMap.put("Fever", 0.4);
		transmissionProbability.put("Start", startMap);


		Map<String,Double> healthyMap = new HashMap<String,Double>();
		healthyMap.put("Healthy", 0.7);
		healthyMap.put("Fever", 0.3);
		transmissionProbability.put("Healthy", healthyMap);
		
		Map<String,Double> feverMap = new HashMap<String,Double>();
		feverMap.put("Healthy", 0.4);
		feverMap.put("Fever", 0.6);
		transmissionProbability.put("Fever", feverMap);
		
		return transmissionProbability;
	}
	
	protected Map<String, Map<String,Double>> getEmissionProbabilities() {
		Map<String,Map<String, Double>> emissionProbability = new HashMap<String, Map<String,Double>>();
		
		Map<String,Double> emMap1 = new HashMap<String,Double>();
		emMap1.put("normal", 0.5);
		emMap1.put("cold", 0.4);
		emMap1.put("dizzy", 0.1);
		emissionProbability.put("Healthy", emMap1);
		
		Map<String,Double> emMap2 = new HashMap<String,Double>();
		emMap2.put("normal", 0.1);
		emMap2.put("cold", 0.3);
		emMap2.put("dizzy", 0.6);
		emissionProbability.put("Fever", emMap2);
		
		return emissionProbability;
	}
	
	@Test
	public void testFindMaxPathFever() {
		
		List<String> states = Arrays.asList(new String[] {"Healthy", "Fever"});
		List<String> observations = Arrays.asList(new String[] {"normal", "cold", "dizzy"});
		Map<String, Map<String,Double>> transmission = getTransmissionProbabilities();
		Map<String, Map<String,Double>> emission  = getEmissionProbabilities();
		
		Viterbi<String,String> viterbi = new Viterbi<String,String>(transmission, emission, states, "Start", "Fever");
		List<String> bestPath = viterbi.findMaxStates(observations);
		assertEquals(bestPath, Arrays.asList(new String[] {"Healthy", "Healthy", "Fever", "Fever"}));


	}
	
	@Test
	public void testFindMaxPathHealthy() {
		
		List<String> states = Arrays.asList(new String[] {"Healthy", "Fever"});
		List<String> observations = Arrays.asList(new String[] {"normal", "cold", "dizzy"});
		Map<String, Map<String,Double>> transmission = getTransmissionProbabilities();
		Map<String, Map<String,Double>> emission  = getEmissionProbabilities();
		
		Viterbi<String,String> viterbi = new Viterbi<String,String>(transmission, emission, states, "Start", "Healthy");
		List<String> bestPath = viterbi.findMaxStates(observations);
		assertEquals(bestPath, Arrays.asList(new String[] {"Healthy", "Healthy", "Fever","Healthy"}));

	}

}
