package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day8.in");

		System.out.println(new Day8Solution(input).partOne());
		System.out.println(new Day8Solution(input).partTwo());
	}
}

class Day8Solution {

	private List<String> input;
	private Map<Character, List<Day8Point>> map;
	private final int MAX_X;
	private final int MAX_Y;

	public Day8Solution(List<String> input) {
		this.map = new HashMap<>();
		this.input = input;

		this.MAX_X = this.input.get(0).length();
		this.MAX_Y = this.input.size();

		this.findAntennas();
	}

	private void findAntennas() {
		for (int i = 0; i < this.input.size(); i++) {
			char[] chars = this.input.get(i).toCharArray();

			for (int j = 0; j < chars.length; j++) {
				char antenna = chars[j];

				if (antenna == '.') {
					continue;
				}

				Day8Point point  = new Day8Point(Integer.valueOf(j), Integer.valueOf(i));

				if (!map.containsKey(antenna)) {
					List<Day8Point> list = new ArrayList<>();

					map.put(antenna, list);
				}

				map.get(antenna).add(point);
			}
		}
	}

	public Integer partOne() {
		return findAllAntiNodes(false);
	}

	public Integer partTwo() {
		return findAllAntiNodes(true);
	}

	private Integer findAllAntiNodes(boolean propagateSignal) {
		Set<Day8Point> knownAntinodes = new HashSet<Day8Point>();

		return this.map.keySet().stream().map((antenna) -> {
			List<Day8Point> antiNodes = processAntenna(antenna, propagateSignal)
			.stream()
			.filter(
				(point) -> point.inBound(MAX_X, MAX_Y) && !knownAntinodes.contains(point)
			)
			.collect(Collectors.toList());

			knownAntinodes.addAll(antiNodes);

			return antiNodes.size();
		}).reduce(0, (acc, number) -> acc + number);
	}

	private Collection<Day8Point> processAntenna(Character antenna, boolean propagateSignal) {
		List<Day8Point> points = this.map.get(antenna);
		Set<Day8Point> antiNode = new HashSet<>();

		for (int i = 0; i < points.size(); i++) {
			var p1 = points.get(i);

			for (int j = i + 1; j < points.size(); j++) {
				var p2 = points.get(j);

				antiNode.addAll(getAntiNodes(p1, p2, propagateSignal));
			}
		}

		return antiNode;
	}

	private List<Day8Point> getAntiNodes(Day8Point pointOne, Day8Point pointTwo, boolean propagateSignal) {
		List<Day8Point> points = new ArrayList<>();

		var diffX = pointOne.x() - pointTwo.x();
		var diffY = pointOne.y() - pointTwo.y();

		Day8Point pA = Day8Point.newPoint(pointOne.x() + diffX, pointOne.y() + diffY);
		Day8Point pB = Day8Point.newPoint(pointTwo.x() - diffX, pointTwo.y() - diffY);

		points.add(pA);
		points.add(pB);

		if (!propagateSignal) {
			return points;
		}

		int signal = 0;
		while(signal < MAX_X) {
			pA = Day8Point.newPoint(pA.x() + diffX, pA.y() + diffY);
			pB = Day8Point.newPoint(pB.x() - diffX, pB.y() - diffY);

			points.add(pA);
			points.add(pB);

			signal++;
		}

		points.add(pointOne);
		points.add(pointTwo);

		return points;
	}
}

record Day8Point(Integer x, Integer y) {

	public static Day8Point newPoint(int x, int y) {
		return new Day8Point(x, y);
	}

	public boolean inBound(int max_x, int max_y) {
		return (x >= 0 && x < max_x) && (y >= 0 && y < max_y);
	}
}