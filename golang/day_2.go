package main

import (
	"fmt"
	"math"
	"strconv"
	"strings"

	"aoc_2024/common"
)

var measures [][]int
var MAX_Y int
var MAX_X int

func init() {
	content := common.ReadInput("day2.in")
	lines := strings.Split(content, "\n")

	measures = make([][]int, len(lines)-1)
	for y, line := range lines {
		if len(line) == 0 {
			break
		}

		elements := strings.Split(line, " ")

		measures[y] = make([]int, len(elements))
		for x, element := range elements {
			measures[y][x], _ = strconv.Atoi(element)
		}
	}

	MAX_X = len(measures[0])
	MAX_Y = len(measures)
}

func direction(diffValue int) string {
	if diffValue < 0 {
		return "up"
	}

	if diffValue == 0 {
		return "neutral"
	}

	return "down"
}

func checkMeasure(element int, nextElement int, tendency string) bool {
	diff := nextElement - element
	measure := math.Abs(float64(diff))

	return direction(diff) == tendency && measure > 0 && measure < 4
}

func isReportSafe(report []int) bool {
	tendency := direction(report[1] - report[0])

	for i := 0; i < len(report)-1; i++ {
		if !checkMeasure(report[i], report[i+1], tendency) {
			return false
		}
	}

	return true
}

func countSafe(reports [][]int) int {
	counter := 0
	for _, report := range reports {
		if isReportSafe(report) {
			counter++
		}
	}

	return counter
}

func partOne() int {
	return countSafe(measures)
}

func main() {
	fmt.Printf("Part 1: %d\n", partOne())
}
