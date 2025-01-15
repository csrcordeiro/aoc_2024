package aoc_java.days;

import static java.lang.Integer.parseInt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day2 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day2.in");

		System.out.println(new Day2Solution(input).partOne());
		System.out.println(new Day2Solution(input).partTwo());
	}
}

class Day2Solution {

	private final List<String> reports;

	private static enum DIRECTION {
		NEUTRAL,
		UP,
		DOWN;

		public static DIRECTION nextDirection(int newMesure) {
			if (newMesure > 0) {
				return UP;
			}

			return DOWN;
		}
	}

	public Day2Solution(List<String> input) {
		this.reports = input;
	}

	public Long partOne() {
		return reports.stream()
			.filter((report) -> isReportSafe(report))
			.count();
	}

	public Long partTwo() {
		return reports.stream()
		.filter((report) -> isReportSafeWithProblemDampener(report))
		.count();
	}

	private boolean isReportSafeWithProblemDampener(String report) {
		List<String[]> allPossibleMFCombination = new ArrayList<>();
		String[] levels = report.split(" ");
		int combinations = levels.length - 1;

		allPossibleMFCombination.add(levels);

		while (combinations >= 0) {
			String[] newCombination = new String[levels.length - 1];
			int shift = 0;

			for (int i = 0; i < levels.length - 1; i++) {
				if (combinations == i) {
					shift++;
				}

				newCombination[i] = levels[i + shift];
			}

			allPossibleMFCombination.add(newCombination);

			combinations--;
		}

		return allPossibleMFCombination.stream()
			.map((reportCombination) -> isReportSafe(reportCombination))
			.filter((isSafe) -> isSafe)
			.count() > 0;
	}

	private boolean isReportSafe(String report) {
		String[] levels = report.split(" ");

		return isReportSafe(levels);
	}

	private boolean isReportSafe(String[] levels) {
		DIRECTION direction = DIRECTION.nextDirection(parseInt(levels[1]) - parseInt(levels[0]));

		for (int i = 0, j = 1; j < levels.length; i++, j++) {
			int mesure = parseInt(levels[j]) - parseInt(levels[i]);

			boolean isSafe = isSafeDiffLevel(mesure) && direction == DIRECTION.nextDirection(mesure);

			if (!isSafe) {
				return false;
			}
		}

		return true;
	}

	private boolean isSafeDiffLevel(int mesure) {
		int absoluteMesure = Math.abs(mesure);

		return absoluteMesure > 0 && absoluteMesure <= 3;
	}
}
