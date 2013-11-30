package com.grunick.addresstagger;

import org.apache.commons.configuration.ConfigurationException;

import com.grunick.addresstagger.input.InputException;

public class AddressTaggerRunner {

	public static void main(String[] args) throws ConfigurationException, InputException {
		AddressTagger tagger = new AddressTagger("/Users/miria/git/AddressTagger/conf/tagger.properties");

	}

}
