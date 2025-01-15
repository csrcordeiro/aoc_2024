package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day6.in");

		System.out.println(new Day6Solution(input).partOne());
		System.out.println(new Day6Solution(input).partTwo());
	}
}

enum Day6Direction {
	UP,
	DOWN,
	RIGHT,
	LEFT;

	public static Day6Direction turn(Day6Direction direction) {
		switch(direction) {
		case UP:
			return RIGHT;
		case DOWN:
			return LEFT;
		case RIGHT:
			return DOWN;
		case LEFT:
			return UP;
		}

		throw new IllegalArgumentException();
	}
}

class Day6Point {

	private Integer x;
	private Integer y;
	private Day6Direction direction;

	public Day6Point(Integer x, Integer y, Day6Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public Day6Point turn() {
		return new Day6Point(x, y, Day6Direction.turn(direction));
	}

	public Day6Point nextPoint() {
		switch (direction) {
		case UP:
			return new Day6Point(x, y - 1, direction);

		case DOWN:
			return new Day6Point(x, y + 1, direction);

		case LEFT:
			return new Day6Point(x - 1, y, direction);

		case RIGHT:
			return new Day6Point(x + 1, y, direction);

		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}
	}

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public Day6Direction getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "Day6Point [x=" + x + ", y=" + y + ", direction=" + direction + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Day6Point other = (Day6Point) obj;
		return Objects.equals(x, other.x) && Objects.equals(y, other.y);
	}
}

class Day6Solution {

	private List<List<Character>> input;
	private final Integer MAX_Y;
	private final Integer MAX_X;

	public Day6Solution(List<String> input) {
		this.input = input.stream().map(
			(line) -> line.chars().mapToObj(
				(e) -> Character.valueOf((char) e)
			).collect(
				Collectors.toList()
			)
		).collect(Collectors.toList());

		MAX_X = this.input.getFirst().size();
		MAX_Y = this.input.size();

	}

	public Integer partTwo() {
		Set<Day6Point> visited = walk();

		int tolerance = visited.size();
		int cycles = 0;

		Day6Point initialPoint = findGuardPosition();

		for (Day6Point visitedPoint : visited) {
			if (hasCycle(initialPoint, visitedPoint, tolerance)) {
				cycles++;
			}
		}

		return cycles;
	}

	private boolean hasCycle(Day6Point initialPoint, Day6Point visitedPoint, int tolerance) {
		List<List<Character>> newGrid = this.input.stream().map(
			(line) -> line.stream().map((charr) -> (charr)).collect(Collectors.toList())
		).collect(Collectors.toList());

		newGrid.get(visitedPoint.getY()).set(visitedPoint.getX(), '#');

		int turns = 0;
		Day6Point point = initialPoint;

		do {
			Day6Point nextPoint = point.nextPoint();

			int x = nextPoint.getX();
			int y = nextPoint.getY();

			if ((x >= MAX_X || x < 0) || (y >= MAX_Y || y < 0)) {
				return false;
			}

			if (turns >= tolerance) {
				return true;
			}

			if (newGrid.get(y).get(x) == '#') {
				point = point.turn();
				turns++;
			} else {
				point = nextPoint;
			}

		} while(true);
	}

	public Integer partOne() {
		Set<Day6Point> walkedPath = walk();

		return walkedPath.size();
	}

	private Set<Day6Point> walk() {
		Day6Point guardPosition = findGuardPosition();
		Set<Day6Point> walkedPath = new HashSet<>();
		Day6Point point = guardPosition;

		walkedPath.add(point);

		while(true) {
			Day6Point nextPoint = point.nextPoint();

			int x = nextPoint.getX();
			int y = nextPoint.getY();

			if ((x >= MAX_X || x < 0) || (y >= MAX_Y || y < 0)) {
				break;
			}

			if (input.get(y).get(x) == '#') {
				point = point.turn();
			} else {
				point = nextPoint;
				walkedPath.add(point);
			}
		}

		return walkedPath;
	}

	private Day6Point findGuardPosition() {
		for (int i = 0; i < input.size(); i++) {
			var line = input.get(i);

			for (int j = 0; j < line.size(); j++) {
				if (line.get(j) == '^') {
					return new Day6Point(j, i, Day6Direction.UP);
				}
			}
		}

		return null;
	}
}