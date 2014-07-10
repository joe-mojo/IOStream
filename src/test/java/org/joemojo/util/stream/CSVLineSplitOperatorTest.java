package org.joemojo.util.stream;

import au.com.bytecode.opencsv.CSVReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.fest.assertions.Assertions.assertThat;

public class CSVLineSplitOperatorTest {
	private Spliterator<String[]> splitOp;
	private CSVReader reader;

	@Before
	public void openCSV(){
		this.reader = new CSVReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("input.csv")), ';', '"', '\\');
		this.splitOp = new CSVLineSplitOperator(reader);
	}

	@After
	public void clean(){
		if(this.reader != null){
			try{
				this.reader.close();
			}catch (IOException ex){
				//Don't care
			}
		}
	}

	@Test
	public void general_characteristics(){
		assertThat(this.splitOp.trySplit()).describedAs("trySplit result").isNull();
		assertThat(this.splitOp.characteristics()).describedAs("characteristics").isEqualTo(Spliterator.NONNULL|Spliterator.IMMUTABLE|Spliterator.ORDERED);
		assertThat(this.splitOp.estimateSize()).describedAs("estimated size").isEqualTo(Long.MAX_VALUE);
//		TODO assertThat(this.splitOp.getComparator()).
		assertThat(this.splitOp.getExactSizeIfKnown()).describedAs("exact size").isEqualTo(-1);
		assertThat(this.splitOp.hasCharacteristics(Spliterator.CONCURRENT)).describedAs("Concurrency").isFalse();
		assertThat(this.splitOp.hasCharacteristics(Spliterator.IMMUTABLE)).describedAs("Immutability").isTrue();
	}

	@Test
	public void when_stream_count_then_tryAdvance_return_false(){
		//Given
		Stream streamOfStringArray = StreamSupport.stream(this.splitOp, false);
		//When
		assertThat(streamOfStringArray.count()).describedAs("Elements count").isEqualTo(4);
		//Then
		assertThat(this.splitOp.tryAdvance(line -> {})).describedAs("tryAdvance result after exhaustion").isFalse();
	}

	@Test
	public void when_new_tryAdvance_return_false(){
		Stream streamOfStringArray = StreamSupport.stream(this.splitOp, false);
		assertThat(this.splitOp.tryAdvance(line -> {})).describedAs("tryAdvance result after exhaustion").isTrue();
		assertThat(streamOfStringArray.count()).describedAs("Elements count").isEqualTo(3);
	}

	@Test
	public void forEachRemaining_gives_only_remainings(){
		Stream streamOfStringArray = StreamSupport.stream(this.splitOp, false);
		//Bad things coming, don't do it at home
		//Bad mutables
		final List<String[]> capturedLines = new ArrayList<>(1);
		//Bad side effect
		assertThat(this.splitOp.tryAdvance(line -> {
			capturedLines.add(line);
		})).describedAs("tryAdvance result after exhaustion").isTrue();
		assertThat(capturedLines.get(0)).describedAs("First captured line").isEqualTo(new String[]{
				"M.", "JEAN", "NEYMAR", "1983-12-10", "", "", "93360", "NEUILLY PLAISANCE", "bonj@yopmail.com", "", "22/02/2011", "00/00/0000"
		});
		assertThat(capturedLines.size()).isEqualTo(1);

		capturedLines.clear();
		this.splitOp.forEachRemaining(line ->{
			capturedLines.add(line);
		});
		assertThat(capturedLines.size()).isEqualTo(3);
		assertThat(capturedLines.get(2)).describedAs("First captured line").isEqualTo(new String[]{
				"Mme", "JESSICA", "SCROUTDAMMONSAC", "1991-06-14", "22 avenue Desflics", "", "92220", "bagneux", "jessicas@yopmail.fr", "0601020304", "19/03/2011", "01/03/2012"
		});
		assertThat(this.splitOp.tryAdvance(line -> {})).isFalse();
	}
}
