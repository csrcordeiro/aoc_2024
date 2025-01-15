package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day3.in");

		System.out.println(new Day3Solution(input).partOne());
		System.out.println(new Day3Solution(input).partTwo());
	}
}

class Day3Solution {

	private final List<String> input;

	public Day3Solution(List<String> input) {
		this.input = input;
	}

	public Long partOne() {
		List<String> tokens = tokenize("(mul\\(\\d+,\\s?\\d+\\))");

		return sumResultsFromTokens(tokens);
	}

	public Long partTwo() {
		List<String> tokens = tokenize("(do\\(\\))|(don't\\(\\))|(mul\\(\\d+,\\s?\\d+\\))");

		 return sumResultsFromTokens(selectTokens(tokens));
	}

	private Long sumResultsFromTokens(List<String> tokens) {
		return tokens.stream()
			.map((expression) -> extractNumbersToMultiply(expression))
			.collect(Collectors.toList())
			.stream()
			.map((pair) -> {
				return pair.get(0) * pair.get(1);
			})
			.reduce(0L, Long::sum);
	}

	private List<String> selectTokens(List<String> tokens) {
		boolean enabled = true;
		List<String> tokensToProcess = new ArrayList<>();

		for (String token : tokens) {
			if (enabled) {
				if (token.contains("mul(")) {
					tokensToProcess.add(token);
				}

				if (token.contains("don't(")) {
					enabled = false;
				}

			} else {
				if (token.contains("do(")) {
					enabled = true;
				}
			}
		}

		return tokensToProcess;
	}

	private List<Long> extractNumbersToMultiply(String expression) {
		Pattern pattern = Pattern.compile("(\\d+)+");

		Matcher matcher = pattern.matcher(expression);

		List<Long> numbers = new ArrayList<>();

		while(matcher.find()) {
			numbers.add(Long.parseLong(matcher.group()));
		}

		return numbers;
	}

	private List<String> tokenize(String regex) {
		return input.stream()
			.flatMap((expression) -> tokenStream(expression, regex))
			.collect(Collectors.toList());
	}

	private Stream<String> tokenStream(String expression, String regex) {
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(expression);

		List<String> extracted = new ArrayList<>();

		while(matcher.find()) {
			extracted.add(matcher.group());
		}

		return extracted.stream();
	}
}