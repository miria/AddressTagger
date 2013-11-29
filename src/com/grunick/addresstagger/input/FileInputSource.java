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

public class FileInputSource implements InputSource {
	
	protected File dataFile;
	protected File headerFile;
	protected LineIterator inputIterator;
	
	protected int counter = -1;
	
	protected Map<String, AddressTag> columnToTagMap = new HashMap<String,AddressTag>();
	protected List<String> columnNames;
	
	private String delimiter;
	
	public FileInputSource(String filename, String headerFile, String delimiter) {
		this.dataFile = new File(filename);
		this.headerFile = new File(headerFile);
		this.delimiter = delimiter;
	}

	@Override
	public void init() throws InputException {
		if (!dataFile.canRead() || !headerFile.canRead())
			throw new InputException("Cannot read from file "+dataFile.getPath());
		
		loadHeaderFile();
		
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
	
	protected void loadHeaderFile() throws InputException {
		try {
			List<String> lines = FileUtils.readLines(headerFile);
			for (String line: lines) {
				String[] pieces = line.trim().split(delimiter);
				if (pieces.length != 2)
					continue;
				AddressTag tag = AddressTag.valueOf(pieces[1]);
				if (tag == null)
					continue;
				columnToTagMap.put(pieces[0].trim(), tag);
			}
		} catch (IOException e) {
			throw new InputException("Unable to read header file: "+e.getMessage());
		}
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
		// TODO: line to Address...
		
		counter++;
		return null;
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
	public int getRecordIndex() {
		return counter;
	}

}
