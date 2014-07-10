package org.joemojo.util.stream;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.StreamSupport;

public class Main {
	public static void main(String args[]){
		char separator = ';';
		char quote = '"';
		char escape = '\\';
		File input = new File("input.csv");

		try(CSVReader reader = new CSVReader(new FileReader(input), separator, quote, escape)){
			StreamSupport.stream(new CSVLineSplitOperator(reader), false).limit(10).map(Functions::trimCells).forEach((String[] line) -> {
				System.out.println(Arrays.asList(line));
			});
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
