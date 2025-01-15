package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day10 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day10.in");

		System.out.println(new Day10Solution(input).partOne());
		System.out.println(new Day10Solution(input).partTwo());
	}

}

class Day10Solution {

	private final int MAX_X;
	private final int MAX_Y;
	private final Integer[][] matrix;

	public Day10Solution(List<String> input) {
		this.MAX_X = input.get(0).length();
		this.MAX_Y = input.size();

		this.matrix = new Integer[this.MAX_Y][this.MAX_X];

		for (int i = 0; i < MAX_Y; i++) {
			String line = input.get(i);

			for (int j = 0; j < MAX_X; j++) {
				this.matrix[i][j] = Integer.valueOf(line.charAt(j) - '0');
			}
		}
	}

	public Integer partOne() {
		return walkTrail(true);
	}

	public Integer partTwo() {
		return walkTrail(false);
	}

	private Integer walkTrail(boolean unique) {
		List<Day10Point> starts = findAllTrailheads();
		Integer score = 0;

		for (Day10Point start : starts) {
			List<Day10Point> processingQueue = new LinkedList<>();
			Set<Day10PointPair> visited = new HashSet<>();
			Day10Point point = start;

			while(true) {
				processingQueue.addAll(0, walk(point));

				if (processingQueue.isEmpty()) {
					break;
				}

				point = processingQueue.removeFirst();

				var pointPair = new Day10PointPair(start, point);

				if (unique && visited.contains(pointPair)) {
					continue;
				}

				if (point.value() == 9) {
					score++;
				}

				visited.add(pointPair);
			}
		}


		return score;
	}

	private List<Day10Point> walk(Day10Point point) {
		List<Day10Point> possiblePoints = new ArrayList<>();

		for (Day10Direction direction : Day10Direction.values()) {
			Day10Point nextPoint = getNextPoint(point, direction);

			if (nextPoint == null) {
				continue;
			}

			if ((nextPoint.value() - point.value()) == 1) {
				possiblePoints.add(nextPoint);
			}
		}

		return possiblePoints;
	}

	private Day10Point getNextPoint(Day10Point point, Day10Direction direction) {
		switch(direction) {
		case Day10Direction.UP:
			return getNextPoint(point.x(), point.y() - 1);

		case Day10Direction.RIGHT:
			return getNextPoint(point.x() + 1, point.y());

		case Day10Direction.DOWN:
			return getNextPoint(point.x(), point.y() + 1);

		case Day10Direction.LEFT:
			return getNextPoint(point.x() - 1, point.y());

		default:
			throw new RuntimeException(String.format("Unknown direction %s", direction));
		}
	}

	private Day10Point getNextPoint(Integer x, Integer y) {
		if (isInBound(x, y)) {
			return new Day10Point(x, y, matrix[y][x]);
		}

		return null;
	}

	private boolean isInBound(Integer x, Integer y) {
		return x >=0 && x < MAX_X && y >= 0 && y < MAX_Y;
	}

	private List<Day10Point> findAllTrailheads() {
		List<Day10Point> trailHeads = new ArrayList<Day10Point>();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 0) {
					trailHeads.add(new Day10Point(j, i, 0));
				}
			}
		}

		return trailHeads;
	}

}

enum Day10Direction {
	UP,
	RIGHT,
	DOWN,
	LEFT
}
record Day10Point(Integer x, Integer y, Integer value) {}

record Day10PointPair(Day10Point start, Day10Point point) {}