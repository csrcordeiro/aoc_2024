package main

import (
	"fmt"
	"math"
	"regexp"
	"sort"
	"strconv"
	"strings"

	"aoc_2024/common"
)

func getColumns(input string) ([]int, []int) {
	lines := strings.SplitAfter(input, "\n")

	pattern := regexp.MustCompile("(\\d+)")

	var firstColumn  []int
	var secondColumn []int

	firstColumn  = make([]int, len(lines))
	secondColumn = make([]int, len(lines))

	for i, row := range lines {
		extracted := pattern.FindAllString(row, -1)

		firstColumn[i],  _ = strconv.Atoi(extracted[0])
		secondColumn[i], _ = strconv.Atoi(extracted[len(extracted) - 1])
	}

	return firstColumn, secondColumn
}

func calculateDistance(firstCol, secondCol []int) []int {
	colSize := len(firstCol)

	var result []int

	result = make([]int, colSize)

	for i := 0; i < colSize; i++ {
		result[i] = int(math.Abs(float64(firstCol[i] - secondCol[i])))
	}

	return result
}

func sumAll(numbers []int) int {
	result := 0

	for _, n := range numbers {
		result += n
	}

	return result
}

func partOne(content string) int {
	first, second := getColumns(content)
	sort.Ints(first)
	sort.Ints(second)

	distances := calculateDistance(first, second)

	return sumAll(distances)
}

func main () {
	content := common.ReadInput("day1.in")

	fmt.Println(partOne(content))
}

