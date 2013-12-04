package com.grunick.addresstagger.stat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CounterMap<T,U> {
	
	protected Map<T, Counter<U>> counterMap = new HashMap<T, Counter<U>>();
	
	public void increment(T first, U second) {
		if (!counterMap.containsKey(first))
			counterMap.put(first, new Counter<U>());
		counterMap.get(first).increment(second);
	}
	
	public Counter<U> getCounter(T key) {
		return counterMap.get(key);
	}
	
	public boolean containsKey(T term) {
		return counterMap.containsKey(term);
	}
	
	public Map<T, Map<U, Double>> getProbabilityMaps() {
		Map<T, Map<U,Double>> probMap = new HashMap<T, Map<U, Double>>();
		for (T item : counterMap.keySet()) {
			probMap.put(item, counterMap.get(item).getProbabilityMap());
		}
		
		return Collections.unmodifiableMap(probMap);
	}
	
	

}
