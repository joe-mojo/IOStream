package org.joemojo.util.stream;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FunctionsTest {
	@Test
	public void trimCells(){
		String[] given = new String[]{" test ", "  Toto\n   ", "\n\ttest\t2\r\n "};
		String[] expected = new  String[]{"test", "Toto", "test\t2"};
		assertThat(Functions.trimCells(given)).describedAs("Trimed string array").isEqualTo(expected);
	}
}
