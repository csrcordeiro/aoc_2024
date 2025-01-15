package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day7.in");

		System.out.println(new Day7Solution(input).partOne());
		System.out.println(new Day7Solution(input).partTwo());
	}
}

class Day7Solution {

	private List<String> input;
	private List<Day7Entry> entries;

	public Day7Solution(List<String> input) {
		this.input = input;
		extractValues();
	}

	private void extractValues() {
		this.entries = input.stream().map((line) -> {
			String[] split = line.split(": ");

			Long target = Long.valueOf(split[0]);

			List<Integer> equations = Arrays.stream(
				split[1].split(" ")
			).map(
				(eq) -> Integer.valueOf(eq)
			).toList();

			return new Day7Entry(target, equations);
		}).collect(Collectors.toList());
	}

	public Long partOne() {
		String[] possibilities = new String[] { "+", "*" };
		return calculate(possibilities);
	}

	public Long partTwo() {
		String[] possibilities = new String[] { "+", "*", "c" };
		return calculate(possibilities);
	}

	private Long calculate(String[] possibilities) {
		return entries.stream().map((entry) -> {
			List<String> permutations = generatePermutation(entry, possibilities);
			BigDecimal value = calculateValues(entry, permutations);

			return value.equals(BigDecimal.valueOf(entry.target())) ? entry.target() : 0L;
		}).reduce(0l, (acc, value) -> acc + value);
	}

	private BigDecimal calculateValues(Day7Entry entry, List<String> permutations) {

		for (String permutation : permutations) {
			char[] ops = permutation.toCharArray();
			List<Integer> equations = entry.equations();
			BigDecimal acc = BigDecimal.valueOf(equations.get(0));

			for (int i = 0, j = 1; i < ops.length; i++, j++) {
				Integer number = equations.get(j);

				char op = ops[i];

				if (op == '+') {
					acc = acc.add(BigDecimal.valueOf(number));
				}

				if (op == '*') {
					if (acc.equals(BigDecimal.ZERO)) {
						acc = BigDecimal.ONE;
					}

					acc = acc.multiply(BigDecimal.valueOf(number));
				}

				if (op == 'c') {
					String newValue = acc.toString() + number.toString();

					acc = new BigDecimal(newValue);
				}
			}

			if (acc.equals(BigDecimal.valueOf(entry.target()))) {
				return acc;
			}

		}

		return BigDecimal.ZERO;
	}

	private List<String> generatePermutation(Day7Entry entry, String[] possibilities) {
		int n = entry.equations().size() - 1;

		int arraySize = (int) Math.pow(possibilities.length, n);

		String[] oldList = possibilities;

		String[] newList = new String[arraySize];

		if (n == 1) {
			return new ArrayList<>(Arrays.asList(possibilities));
		}

		int newIndex = 0;
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < oldList.length && oldList[j] != null; j++) {
				String str = oldList[j];

				for (int k = 0; k < possibilities.length; k++) {
					newList[newIndex] = (str + possibilities[k]);
					newIndex++;
				}
			}

			if (i != n-1) {
				oldList = newList;
				newList = new String[arraySize];
				newIndex = 0;
			}
		}

		List<String> result = new ArrayList<>();

		for (int i = 0; i < newList.length && newList[i] != null; i++) {
			result.add(newList[i]);
		}

		return result;
	}
}

record Day7Entry(Long target, List<Integer> equations) {}