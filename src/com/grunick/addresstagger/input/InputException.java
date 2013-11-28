package com.grunick.addresstagger.input;

@SuppressWarnings("serial")
public class InputException extends Exception {

	protected String message;
	
	public InputException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
