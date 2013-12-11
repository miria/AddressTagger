package com.grunick.addresstagger.strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import tbl.learner.TblTrainer;
import tbl.tagger.TblTagger;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.input.InputException;
import com.grunick.addresstagger.input.InputSource;

public class TBLStrategy implements TaggerStrategy {

	protected String corpusFile;
	protected String lexFile;
	protected String templateFile;
	protected String ruleFile;
	protected String guesserFile;
	protected TblTagger tagger;
	
	public TBLStrategy(String corpusFile, String templateFile, String ruleFile, String lexFile, String guesserFile) {
		this.corpusFile = corpusFile;    // generated
		this.lexFile = lexFile;          // generated  
		this.ruleFile = ruleFile;        // generated     
		this.templateFile = templateFile;
		this.guesserFile = guesserFile;
	}
	
	@Override
	public void train(InputSource source) throws InputException {
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File(corpusFile), false));
		} catch (IOException e) {
			throw new InputException("Unable to generate the corpus file! "+e.getMessage());
		}
		
		while (source.hasNext()) {
			try {
				Address address = source.getNext();
				if (address.size() == 0)
					continue;

				for (int i=0; i< address.size(); i++) {
					writer.write(address.getAddressTokens().get(i)+" "+address.getKnownTags().get(i));
					writer.newLine();
				}
				writer.newLine();
			} catch (IOException e) {
				throw new InputException("Unable to generate the corpus file! "+e.getMessage());
			} catch (InputException ie) {}
			
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			throw new InputException("Unable to generate the training file! "+e.getMessage());
		}
		
		TblTrainer trainer = new TblTrainer(lexFile, corpusFile);
		trainer.train(corpusFile, templateFile, ruleFile); 
		tagger = new TblTagger(lexFile, ruleFile, guesserFile);
	}

	@Override
	public void tagAddress(Address address) {
		List<String> tags = tagger.tag(address.getAddressTokens());

		for (int i=0; i<address.size(); i++) {
			try {
				address.setTag(i, AddressTag.valueOf(tags.get(i)));
			} catch (IllegalArgumentException e) {
				System.out.println("TBL tagger can't find tag "+tags.get(i));
				address.setTag(i, AddressTag.UNK);
			}
		}

	}

}
