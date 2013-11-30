package com.grunick.addresstagger.stat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Counter<T> {
	
	protected Map<T, Integer> counterMap = new HashMap<T, Integer>();
	protected int total = 0;
	
	
	public void increment(T key) {
		if (!counterMap.containsKey(key))
			counterMap.put(key, 0);
		counterMap.put(key, counterMap.get(key)+1);
		total++;
	}
	
	public int getCount(T key) {
		if (!counterMap.containsKey(key))
			return 0;
		return counterMap.get(key);
	}
	
	public int getTotal() {
		return total;
	}

	public Map<T, Integer> getCountMap() {
		return Collections.unmodifiableMap(counterMap);
	}
	
	public T argMax() {
	    double maxCount = -1;
	    T maxValue = null;
	    for (Map.Entry<T, Integer> entry : counterMap.entrySet()) {
	    	if (entry.getValue() > maxCount || maxValue == null) {
	    		maxValue = entry.getKey();
	    		maxCount = entry.getValue();
	    	}
	    }
	    return maxValue;
	}
}
