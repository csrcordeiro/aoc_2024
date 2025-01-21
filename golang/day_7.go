package main

import (
	"fmt"
	"math"
	"math/big"
	"regexp"
	"strconv"
	"strings"

	"aoc_2024/common"
)

type entry struct {
	target  *big.Int
	numbers []*big.Int
}

func (e entry) Target() *big.Int {
	return e.target
}

func (e entry) Numbers() []*big.Int {
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
		target:  big.NewInt(int64(target)),
		numbers: make([]*big.Int, len(elements)),
	}

	for i, n := range elements {
		number, _ := strconv.Atoi(n)
		newEntry.numbers[i] = big.NewInt(int64(number))
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

	var result *big.Int

	for i := 0; i < len(combinations); i++ {
		comb := combinations[i]

		result = big.NewInt(numbers[0].Int64())

		for i, char := range comb {
			number := numbers[i+1]

			if char == '*' {
				result.Mul(result, number)
			} else if char == '+' {
				result.Add(result, number)
			} else {
				result.SetString(result.String()+number.String(), 10)
			}
		}

		if result.Cmp(entry.Target()) == 0 {
			return true
		}
	}

	return false
}

func processRow(row string, start []string) *big.Int {
	entry := extractEntry(row)
	opCombinations := combinations(entry, start)

	if hasCombination(entry, opCombinations) {
		return entry.Target()
	}

	return big.NewInt(0)
}

func processRowGoroutine(row string, start []string, channel chan *big.Int) {
	channel <- processRow(row, start)
}

func processRowsWithGoroutines(rows []string, start []string) *big.Int {
	inputSize := len(rows)

	results := make(chan *big.Int, inputSize)

	for i := 0; i < inputSize; i++ {
		go processRowGoroutine(rows[i], start, results)
	}

	res := big.NewInt(0)
	for i := 0; i < inputSize; i++ {
		processedRow := <-results
		res.Add(res, processedRow)
	}

	return res
}

func processRows(rows []string, start []string) *big.Int {
	res := big.NewInt(0)

	for i := 0; i < len(rows); i++ {
		res.Add(res, processRow(rows[i], start))
	}

	return res
}

func partOne(rows []string) *big.Int {
	return processRowsWithGoroutines(rows, []string{"+", "*"})
}

func partTwo(rows []string) *big.Int {
	return processRowsWithGoroutines(rows, []string{"+", "*", "c"})
}

func main() {
	rows := clearEmptyInput(common.ReadInput("day7.in"))

	fmt.Println("Part 1:", partOne(rows))
	fmt.Println("Part 2:", partTwo(rows))
}
