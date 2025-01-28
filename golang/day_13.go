package main

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"

	"aoc_2024/common"
)

var (
	machines = make([]*Machine, 0)
)

// All values for properties are X and Y.
type Machine struct {
	A     []int
	B     []int
	Prize []int
}

func (m *Machine) String() string {
	return fmt.Sprintf(
		"Button A: %d %d\nButton B: %d %d\nPrize: %d %d",
		m.A[0],
		m.A[1],
		m.B[0],
		m.B[1],
		m.Prize[0],
		m.Prize[1],
	)
}

func init() {
	content := common.ReadInput("day13.in")

	lines := strings.Split(content, "\n")

	for i := 0; i < len(lines); i += 4 {
		buttonA := extractValues(lines[i])
		buttonB := extractValues(lines[i+1])
		prizes := extractValues(lines[i+2])

		newMachine := &Machine{
			A:     buttonA,
			B:     buttonB,
			Prize: prizes,
		}

		machines = append(machines, newMachine)
	}
}

// Using Cramer's rule.
// Very fancy stuff.
//
// https://www.youtube.com/watch?v=jBsC34PxzoM
func cost(machine *Machine, offset int) int {
	det := float64((machine.A[0] * machine.B[1]) - (machine.A[1] * machine.B[0]))

	prize1 := machine.Prize[0] + offset
	prize2 := machine.Prize[1] + offset

	a := int(float64(prize1*machine.B[1]-prize2*machine.B[0]) / det)
	b := int(float64(prize2*machine.A[0]-prize1*machine.A[1]) / det)

	testA1 := machine.A[0] * a
	testA2 := machine.A[1] * a

	testB1 := machine.B[0] * b
	testB2 := machine.B[1] * b

	if testA1+testB1 == prize1 && testB2+testA2 == prize2 {
		return int(a*3 + b)
	}

	return 0
}

func extractValues(entry string) []int {
	pattern := regexp.MustCompile("(\\d+)")
	numbers := pattern.FindAllString(entry, -1)

	buttons := make([]int, 2)
	buttons[0], _ = strconv.Atoi(numbers[0])
	buttons[1], _ = strconv.Atoi(numbers[1])

	return buttons
}

func partOne() int {
	solution := 0
	for _, machine := range machines {
		solution += cost(machine, 0)
	}

	return solution
}

func partTwo() int {
	solution := 0
	for _, machine := range machines {
		solution += cost(machine, 10_000_000_000_000)
	}

	return solution
}

func main() {
	fmt.Println(fmt.Sprintf("Part 1: %d", partOne()))
	fmt.Println(fmt.Sprintf("Part 2: %d", partTwo()))
}
