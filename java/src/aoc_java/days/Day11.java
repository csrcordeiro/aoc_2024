package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Day11 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day11.in");

		System.out.println(new Day11Solution(input).partOne());
		System.out.println(new Day11Solution(input).partTwo());
	}
}

class Day11Solution {

	private Map<Long, Long> stones = new HashMap<>();

	public Day11Solution(List<String> input) {
		for (String s : input) {
			String[] values = s.split(" ");

			for (String value : values) {
				stones.put(Long.valueOf(value), stones.getOrDefault(Long.valueOf(value), 1l));
			}
		}
	}

	public Long partOne() {
		return blink(25);
	}

	public Long partTwo() {
		return blink(75);
	}

	private Long blink(int n) {
		Map<Long, Long> results = stones;

		for (int i = 0; i < n; i++) {
			Map<Long, Long> stonesAfterBlink = new HashMap<Long, Long>();

			for (Entry<Long, Long> entry : results.entrySet()) {
				var stone = entry.getKey();
				var value = entry.getValue();

				List<Long> newStones = applyRules(stone);

				for (Long newStone : newStones) {
					stonesAfterBlink.put(newStone, stonesAfterBlink.getOrDefault(newStone, 0l) + value);
				}
			}

			results = stonesAfterBlink;
		}

		return results.values()
			.stream()
			.reduce(0l, (acc, number) -> acc + number);
	}


	private List<Long> applyRules(Long stone) {
		var results = new ArrayList<Long>();

		String stoneString = stone.toString();

		if (stone == 0) {
			results.add(1l);

		} else if (stoneString.length() % 2 == 0) {
			int half = stoneString.length() / 2;

			results.add(Long.valueOf(stoneString.substring(0, half)));
			results.add(Long.valueOf(stoneString.substring(half, stoneString.length())));

		} else {
			results.add(stone * 2024);
		}

		return results;
	}
}
