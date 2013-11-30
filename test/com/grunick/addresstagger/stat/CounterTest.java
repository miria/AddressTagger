package com.grunick.addresstagger.stat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CounterTest {
	
	@Test
	public void testGetCountEmpty() {
		Counter<String> counter = new Counter<String>();
		assertEquals(counter.getCount("test"), 0);
	}
	
	@Test
	public void testGetTotalEmpty() {
		Counter<String> counter = new Counter<String>();
		assertEquals(counter.getTotal(), 0);
	}
	
	@Test 
	public void testIncrement() {
		Counter<String> counter = new Counter<String>();
		counter.increment("test");
		counter.increment("test");
		counter.increment("test2");

		assertEquals(counter.getCount("test"), 2);
	}

	@Test 
	public void testTotal() {
		Counter<String> counter = new Counter<String>();
		counter.increment("test");
		counter.increment("test");
		counter.increment("test2");

		assertEquals(counter.getTotal(), 3);
	}

}
