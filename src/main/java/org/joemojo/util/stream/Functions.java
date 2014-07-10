package org.joemojo.util.stream;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Functions {
	private Functions(){};

	public static String[] trimCells(String[] cellValues){
		return Arrays.stream(cellValues).map((String cellValue) -> cellValue.trim()).collect(Collectors.toList()).toArray(cellValues);
	}

}
