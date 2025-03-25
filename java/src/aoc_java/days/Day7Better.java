package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day7Better {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day7.in");

		System.out.println("Part 1: " + new Day7BetterSolution(input).partOne());
		System.out.println("Part 2: " + new Day7BetterSolution(input).partTwo());
	}
}

class Day7BetterSolution {
    private List<String> input;

    public Day7BetterSolution(List<String> input) {
        this.input = input;
    }

    public Long partOne() {
        return extractRows().mapToLong((row) -> {
            Long target = row.get(0);
            Long value = row.get(1);

            if (canProcess(target, row.subList(2, row.size()), value, 1)) {
                return target;

            } else {
                return 0L;
            }

        }).sum();
    }

    public Long partTwo() {
        return extractRows().mapToLong((row) -> {
            Long target = row.get(0);
            Long value = row.get(1);

            if (canProcess(target, row.subList(2, row.size()), value, 2)) {
                return target;

            } else {
                return 0L;
            }

        }).sum();
    }

    private boolean canProcess(Long target, List<Long> numbers, Long value, int part) {

        if (value > target)
            return false;

        if (numbers.isEmpty())
            return target.equals(value);

        Long x = numbers.get(0);
        List<Long> newNumbers = numbers.subList(1, numbers.size());

        if (part == 1)
            return canProcess(target, newNumbers, value + x, 1) ||
                canProcess(target, newNumbers, value * x, 1);
        else
            return canProcess(target, newNumbers, value + x, 2) ||
                canProcess(target, newNumbers, value * x, 2) ||
                canProcess(target, newNumbers, value * ndecimals(x) + x, 2);
    }

    private Long ndecimals(Long x) {
        double digits = Math.floor(Math.log(x.doubleValue()) / Math.log(10.0) + 1);

        return (long) Math.pow(10.0, digits);
    }

    private Stream<List<Long>> extractRows() {
        return this.input.stream().map((row) -> {
            return extractIntegers(row);
        });
    }

    private List<Long> extractIntegers(String row) {
        List<Long> ints = new ArrayList<>();
        Pattern p = Pattern.compile("(\\d+)+");
        Matcher m = p.matcher(row);

        while(m.find()) {
            ints.add(Long.parseLong(m.group(1)));
        }

        return ints;
    }
}

