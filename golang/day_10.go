package main

import (
	"fmt"
	"strings"

	"aoc_2024/common"
)

var directions [4]string
var matrix [][]int

var MAX_X int
var MAX_Y int

type Point struct {
	x, y int
}

func (p Point) String() string {
	return fmt.Sprintf("%d,%d", p.x, p.y)
}

func init() {
	content := common.ReadInput("day10.in")
	directions = [4]string{"up", "right", "down", "left"}

	contentSplit := strings.Split(content, "\n")
	size := len(contentSplit) - 1
	matrix = make([][]int, size)

	for y, line := range contentSplit {
		if len(line) == 0 {
			break
		}

		matrix[y] = make([]int, size)

		for x, value := range line {
			if value == '\n' {
				continue
			}
			intVal := int(value - '0')
			matrix[y][x] = intVal
		}
	}

	MAX_X = len(matrix[0])
	MAX_Y = len(matrix)
}

func mapStartingPoints() []*Point {
	starting := make([]*Point, 0)

	for y, line := range matrix {
		for x, val := range line {
			if val == 0 {
				starting = append(starting, &Point{x: x, y: y})
			}
		}
	}

	return starting
}

func inBound(p *Point) bool {
	return p.x < MAX_X && p.x >= 0 && p.y < MAX_Y && p.y >= 0
}

func nextSafePoint(p *Point, direction string) *Point {
	newPoint := nextPoint(p, direction)

	if !inBound(newPoint) {
		return nil
	}

	return newPoint
}

func nextPoint(p *Point, direction string) *Point {
	switch direction {
	case "up":
		return &Point{x: p.x, y: p.y - 1}
	case "down":
		return &Point{x: p.x, y: p.y + 1}
	case "left":
		return &Point{x: p.x - 1, y: p.y}
	case "right":
		return &Point{x: p.x + 1, y: p.y}
	}

	return nil
}

func walk(start *Point) []*Point {
	possibleWalk := make([]*Point, 0)

	for _, direction := range directions {
		newPoint := nextSafePoint(start, direction)

		if newPoint == nil {
			continue
		}

		valStart := matrix[start.y][start.x]
		valNewPoint := matrix[newPoint.y][newPoint.x]

		if (valNewPoint - valStart) == 1 {
			possibleWalk = append(possibleWalk, newPoint)
		}
	}

	return possibleWalk
}

func walkTrail(start *Point, unique bool) int {
	score := 0
	queue := make([]*Point, 0)
	point := start
	set := make(map[string]int)

	for true {
		queue = append(walk(point), queue[:]...)

		if len(queue) == 0 {
			break
		}

		point, queue = queue[0], queue[1:]

		_, hasElement := set[point.String()]

		if unique && hasElement {
			continue
		}

		if matrix[point.y][point.x] == 9 {
			score++
		}

		set[point.String()] = 1
	}

	return score
}

func sumResults(unique bool) int {
	result := 0

	for _, starting := range mapStartingPoints() {
		result += walkTrail(starting, unique)
	}

	return result
}

func partOne() int {
	return sumResults(true)
}

func partTwo() int {
	return sumResults(false)
}

func main() {
	fmt.Printf("Part 1: %d\n", partOne())
	fmt.Printf("Part 2: %d\n", partTwo())
}
