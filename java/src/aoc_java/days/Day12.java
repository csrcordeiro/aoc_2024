package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day12 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day12.in");

		System.out.println(new Day12Solution(input).partOne());
		System.out.println(new Day12Solution(input).partTwo());
	}
}

class Day12Solution {

	private Map<String, List<Day12Cluster>> plants = new HashMap<>();
	private List<String> input;

	private Integer MAX_Y;
	private Integer MAX_X;

	public Day12Solution(List<String> input) {
		this.input = input;

		MAX_Y = input.size();
		MAX_X = input.get(0).length();

		generateMap();
	}

	public Long partOne() {
		return plants.values().stream()
			.flatMapToLong((clusters) ->
				clusters.stream().mapToLong(
					(cluster) -> calculate(cluster.getPoints())
				)
			).sum();
	}

	public Long partTwo() {
		return 0l;
	}

	private Long calculate(List<Day12Point> points) {
		return Long.valueOf(calculateArea(points) * calculatePerimeter(points));
	}

	private Integer calculateArea(List<Day12Point> points) {
		return points.size();
	}

	private Integer calculatePerimeter(List<Day12Point> points) {
		return points
			.stream()
			.mapToInt((point) -> countFreeSides(point, points))
			.sum();
	}

	private Integer countFreeSides(Day12Point currentPoint, Collection<Day12Point> points) {
		Integer emptyCounter = 0;

		for (Day12Direction direction : Day12Direction.values()) {
			if (!points.contains(getNextPoint(currentPoint, direction))) {
				emptyCounter++;
			}
		}

		return emptyCounter;
	}

	private void generateMap() {
		Set<Day12Point> visited = new HashSet<>();

		for (int y = 0; y < input.size(); y++) {
			char[] plants = input.get(y).toCharArray();

			for (int x = 0; x < plants.length; x++) {
				Day12Point point = new Day12Point(x, y);

				if (visited.contains(point)) {
					continue;
				}

				String plant = String.valueOf(plants[x]);

				Day12Cluster cluster = extractCluster(point, plant);

				if (!this.plants.containsKey(plant)) {
					this.plants.put(plant, new ArrayList<Day12Cluster>());
				}

				this.plants.get(plant).add(cluster);

				visited.addAll(cluster.getPoints());
			}
		}
	}

	private Day12Cluster extractCluster(Day12Point point, String plant) {
		var points = new HashSet<Day12Point>();
		points.add(point);

		List<Day12Point> queue = new LinkedList<>();
		queue.add(point);

		while(!queue.isEmpty()) {
			var next = queue.removeFirst();

			for (Day12Direction direction : Day12Direction.values()) {
				var newPoint = getNextPoint(next, direction);

				String newPointValue = pointValue(newPoint);

				if (plant.equals(newPointValue) && !points.contains(newPoint)) {
					queue.add(newPoint);
					points.add(newPoint);
				}
			}
		}

		return new Day12Cluster(new ArrayList<Day12Point>(points));
	}

	private String pointValue(Day12Point point) {
		if (!inBound(point.x(), point.y())) {
			return "";
		}

		String line = input.get(point.y());


		return String.valueOf(line.charAt(point.x()));
	}

	private Day12Point getNextPoint(Day12Point point, Day12Direction direction) {
		switch (direction) {
			case Day12Direction.UP: {
				return new Day12Point(point.x(), point.y() - 1);
			}

			case Day12Direction.RIGHT: {
				return new Day12Point(point.x() + 1, point.y());
			}

			case Day12Direction.DOWN: {
				return new Day12Point(point.x(), point.y() + 1);
			}

			case Day12Direction.LEFT: {
				return new Day12Point(point.x() - 1, point.y());
			}

			default:
				throw new IllegalArgumentException("Unexpected direction " + direction.toString());

		}
	}

	private boolean inBound(int x, int y) {
		return x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y;
	}
}

enum Day12Direction {
	UP,
	RIGHT,
	DOWN,
	LEFT
}

class Day12Cluster {
	private List<Day12Point> points;

	public Day12Cluster(List<Day12Point> points) {
		this.points = points;
	}

	public List<Day12Point> getPoints() {
		return points;
	}

	@Override
	public String toString() {
		return "Cluster[ " + points + " ]";
	}
}

record Day12Point(int x, int y) {}