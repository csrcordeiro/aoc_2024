package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day1 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day1.in");

		System.out.println(new Day1Solution(input).partOne());
		System.out.println(new Day1Solution(input).partTwo());
	}
}

class Day1Solution {

	private final List<String> inputs;

	private List<Long> firstColumn;
	private List<Long> secondColumn;

	public Day1Solution(List<String> inputs) {
		this.inputs = inputs;
		this.firstColumn = new ArrayList<Long>();
		this.secondColumn = new ArrayList<Long>();
	}

	public Long partOne() {
		extractNumbers();
		sortColumns();

		return IntStream
			.range(0,
			Integer.min(firstColumn.size(), secondColumn.size()))
			.mapToLong((index) -> Math.abs(
			firstColumn.get(index) - secondColumn.get(index)))
			.sum();
	}

	public Long partTwo() {
		extractNumbers();

		return firstColumn
			.stream()
			.mapToLong((number) -> number
			* Collections.frequency(secondColumn, number))
			.sum();
	}

	private void sortColumns() {
		Collections.sort(firstColumn);
		Collections.sort(secondColumn);
	}

	private void extractNumbers() {
		firstColumn.clear();
		secondColumn.clear();

		var pattern = Pattern.compile("([0-9]+)\s+([0-9]+)");

		for (String input : inputs) {
			var matcher = pattern.matcher(input);

			matcher.find();

			firstColumn.add(Long.parseLong(matcher.group(1)));
			secondColumn.add(Long.parseLong(matcher.group(2)));
		}

	}
}
