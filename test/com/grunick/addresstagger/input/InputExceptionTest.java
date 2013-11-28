package com.grunick.addresstagger.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InputExceptionTest {
	
	@Test
	public void testMessage() {
		InputException e = new InputException("myMessage");
		assertEquals(e.getMessage(), "myMessage");
	}

}
