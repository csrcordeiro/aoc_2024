package main

import (
	"fmt"
	"strings"
	"strconv"
	"regexp"
	"math"

	"aoc_2024/common"
)

func can_process(target int, numbers []int, value int, part int) bool {
	if value > target {
		return false
	}

	if len(numbers) == 0 {
		return target == value
	}

	x := numbers[0]

	if part == 1 {
		return can_process(target, numbers[1:], value + x, 1) ||
			can_process(target, numbers[1:], value * x, 1)
	} else {
		return can_process(target, numbers[1:], value + x, 2) ||
			can_process(target, numbers[1:], value * x, 2) ||
			can_process(target, numbers[1:], appendNumber(value, x), 2)
	}
}

func appendNumber(value int, x int) int {
	digits := math.Pow(10.0, ndecimal(x))

	return value * int(math.Ceil(digits)) + x
}

func ndecimal(x int) float64 {
	return math.Trunc(math.Log(float64(x)) / math.Log(10.0) + 1)
}

func clearEmptyInput(input string) []string {
	rows := strings.Split(input, "\n")

	for i, row := range rows {
		if len(row) == 0 {
			rows = append(rows[:i], rows[i+1:]...)
		}
	}

	return rows
}

func main() {
	var part1, part2 = 0, 0

	lines := clearEmptyInput(common.ReadInput("day7.in"))

	for _, row := range lines {
		r := regexp.MustCompile("\\d+")
		n := r.FindAllString(row, -1)

		numbers := make([]int, len(n))

		var err error
		for i, number := range n {
			numbers[i], err = strconv.Atoi(number)
			if err != nil {
				panic(fmt.Sprintf("Error converting %s\n", number))
			}
		}

		if can_process(numbers[0], numbers[2:], numbers[1], 1) {
			part1 += numbers[0]
		}

		if can_process(numbers[0], numbers[2:], numbers[1], 2) {
			part2 += numbers[0]
		}
	}

	fmt.Printf("Part 1: %d\n", part1)
	fmt.Printf("Part 2: %d\n", part2)
}
