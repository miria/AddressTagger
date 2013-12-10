package com.grunick.addresstagger.input;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.grunick.addresstagger.data.Address;
import com.grunick.addresstagger.data.AddressTag;
import com.grunick.addresstagger.tokenize.Tokenizer;

public class FileInputSource implements InputSource {
	
	protected File dataFile;
	protected LineIterator inputIterator;
	protected Tokenizer tokenizer;
		
	protected Map<String, AddressTag> columnToTagMap = new HashMap<String,AddressTag>();
	protected List<String> columnNames;
	
	private String delimiter;
	
	public FileInputSource(String filename,String delimiter) {
		this.dataFile = new File(filename);
		this.delimiter = delimiter;
	}

	@Override
	public void init() throws InputException {
		if (!dataFile.canRead())
			throw new InputException("Cannot read from file "+dataFile.getPath());

		try {
			inputIterator = FileUtils.lineIterator(dataFile);
		} catch (IOException e) {
			throw new InputException("Unable to open data file: "+e.getMessage());
		} 
		
		loadColumnNames();
	}
	
	protected void loadColumnNames() throws InputException {
		if (!inputIterator.hasNext())
			throw new InputException("Unable to load header - no data in the input file");
		String line = inputIterator.nextLine();
		String[] pieces = line.trim().split(delimiter);
		columnNames = Arrays.asList(pieces);
	}
	


	@Override
	public boolean hasNext() {
		return inputIterator.hasNext();
	}
	
	@Override
	public Address getNext() throws InputException {
		if (!inputIterator.hasNext())
			return null;
		
		String line = inputIterator.nextLine();
		String[] pieces = line.split(delimiter);
		
		if (pieces.length != columnNames.size())
			throw new InputException("Found an irregular sized row!");
		
		Address address = tokenizer.tokenizeAddress(Arrays.asList(pieces));
		
		return address;
	}

	@Override
	public void close() {
		LineIterator.closeQuietly(inputIterator);
	}

	@Override
	public List<String> getColumnNames() {
		return Collections.unmodifiableList(columnNames);
	}
	
	@Override
	public Map<String, AddressTag> getColumnToTagMap() {
		return Collections.unmodifiableMap(columnToTagMap);
	}


	@Override
	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
		
	}

	@Override
	public void reset() throws InputException {
		close();
		init();
	}

}
