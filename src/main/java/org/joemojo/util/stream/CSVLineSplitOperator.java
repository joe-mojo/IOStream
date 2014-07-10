package org.joemojo.util.stream;

import au.com.bytecode.opencsv.CSVReader;

import java.io.IOException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class CSVLineSplitOperator implements Spliterator<String[]> {
	CSVReader source;

	public CSVLineSplitOperator(CSVReader source){
		this.source = source;
	}

	@Override
	public boolean tryAdvance(Consumer<? super String[]> action) {
		try{
			String [] line = this.source.readNext();
			if(line == null) return false;
			action.accept(line);
			return true;
		}catch (IOException ex){
			return false;
		}
	}

	@Override
	public Spliterator<String[]> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public int characteristics() {
		return IMMUTABLE | NONNULL | ORDERED;
	}
}
