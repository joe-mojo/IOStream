IOStream
========

Java 8 Stream from IO channels

Example of tools used to create a Stream from IO channels that don't have a `stream()` method.

`java.io.Buffered` reader offers a `Stream<String> stream()` method, but here is an example showing how to do it with `CSVReader` from OpenCSV.

Everything relies on implementing a specific `java.util.SplitOperator` so that `java.util.stream.StreamSupport` can create a `Stream`for us.
