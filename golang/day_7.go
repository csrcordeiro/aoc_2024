package main

import (
	"fmt"
	"math"
	"regexp"
	"strconv"
	"strings"

	"aoc_2024/common"
)

type entry struct {
	target  int64
	numbers []int64
}

func (e entry) Target() int64 {
	return e.target
}

func (e entry) Numbers() []int64 {
	return e.numbers
}

func (e entry) Len() int {
	return len(e.numbers)
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

func extractEntry(row string) *entry {
	re, _ := regexp.Compile("(\\d+)+")

	extracted := re.FindAllString(row, -1)

	target, _ := strconv.Atoi(extracted[0])

	elements := extracted[1:]

	newEntry := &entry{
		target:  int64(target),
		numbers: make([]int64, len(elements)),
	}

	for i, n := range elements {
		number, _ := strconv.Atoi(n)
		newEntry.numbers[i] = int64(number)
	}

	return newEntry
}

func combinations(entry *entry, start []string) []string {
	size := entry.Len() - 1

	arraySize := int(math.Pow(float64(len(start)), float64(size)))

	if size == 1 {
		return start
	}

	oldList := make([]string, len(start))

	copy(oldList, start)

	newList := make([]string, arraySize)

	var newIndex int
	for i := 1; i < size; i++ {
		newIndex = 0

		for j := 0; j < len(oldList); j++ {
			str := oldList[j]

			if len(str) == 0 {
				break
			}

			for k := 0; k < len(start); k++ {
				newList[newIndex] = str + start[k]
				newIndex++
			}
		}

		if i != size-1 {
			oldList = newList
			newList = make([]string, arraySize)
		}
	}

	return newList
}

func hasCombination(entry *entry, combinations []string) bool {
	numbers := entry.Numbers()

	var result int64

	for i := 0; i < len(combinations); i++ {
		comb := combinations[i]

		result = int64(numbers[0])

		for i, char := range comb {
			number := numbers[i+1]

			if char == '*' {
				result *= number
			} else if char == '+' {
				result += number
			} else {
				strResult := strconv.FormatInt(result, 10)
				strNumber := strconv.FormatInt(number, 10)
				result, _ = strconv.ParseInt(strResult+strNumber, 10, 64)
			}
		}

		if result == entry.Target() {
			return true
		}
	}

	return false
}

func processRow(row string, start []string) int64 {
	entry := extractEntry(row)
	opCombinations := combinations(entry, start)

	if hasCombination(entry, opCombinations) {
		return entry.Target()
	}

	return int64(0)
}

func processRowGoroutine(row string, start []string, channel chan int64) {
	channel <- processRow(row, start)
}

func processRowsWithGoroutines(rows []string, start []string) int64 {
	inputSize := len(rows)

	results := make(chan int64, inputSize)

	for i := 0; i < inputSize; i++ {
		go processRowGoroutine(rows[i], start, results)
	}

	res := int64(0)
	for i := 0; i < inputSize; i++ {
		processedRow := <-results
		res += processedRow
	}

	return res
}

func processRows(rows []string, start []string) int64 {
	res := int64(0)

	for i := 0; i < len(rows); i++ {
		res += processRow(rows[i], start)
	}

	return res
}

func partOne(rows []string) int64 {
	return processRowsWithGoroutines(rows, []string{"+", "*"})
}

func partTwo(rows []string) int64 {
	return processRowsWithGoroutines(rows, []string{"+", "*", "c"})
}

func main() {
	rows := clearEmptyInput(common.ReadInput("day7.in"))

	fmt.Println("Part 1:", partOne(rows))
	fmt.Println("Part 2:", partTwo(rows))
}
