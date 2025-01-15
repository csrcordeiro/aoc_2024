package aoc_java.days;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Common {

	public static List<String> getInput(String fileName) throws FileNotFoundException, IOException {
		String file = Common.class.getClassLoader().getResource(fileName).getFile();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return reader.lines().collect(Collectors.toList());
		}
	}

}