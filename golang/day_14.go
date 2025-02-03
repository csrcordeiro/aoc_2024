package main

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"

	"aoc_2024/common"
)

var robots []*robot

const (
	X = 101
	Y = 103
)

type robot struct {
	point    []int
	velocity []int
}

func (r *robot) Pos() string {
	return fmt.Sprintf("(%d,%d)", r.point[0], r.point[1])
}

func (r *robot) Coord() (int, int) {
	return r.point[0], r.point[1]
}

func (r *robot) String() string {
	return fmt.Sprintf("point = %v, velocity = %v", r.point, r.velocity)
}

func (r *robot) walk() {
	x, y   := r.point[0], r.point[1]
	v1, v2 := r.velocity[0], r.velocity[1]

	x += v1
	y += v2

	r.point[0] = correct(x, X)
	r.point[1] = correct(y, Y)
}

func correct(pos, maxSize int) int {
	if pos < 0 {
		return maxSize + pos

	} else if pos >= maxSize {

		return pos - maxSize
	}

	return pos
}

func init() {
	content := common.ReadInput("day14.in")

	lines := strings.Split(content, "\n")

	for _, line := range lines {
		if len(line) == 0 {
			break
		}

		allValues := extractValues(line, "(-?\\d+)+")

		robots = append(robots, &robot{
			point:    allValues[0:2],
			velocity: allValues[2:],
		})
	}
}

func extractValues(entry string, patternStr string) []int {
	pattern := regexp.MustCompile(patternStr)
	numbers := pattern.FindAllString(entry, -1)

	if numbers == nil {
		panic("No match found for " + patternStr)
	}

	result := make([]int, len(numbers))

	for i, number := range numbers {
		result[i], _ = strconv.Atoi(number)
	}

	return result
}

func robotsPerPosition() map[string]int {
	countPositions := make(map[string]int)

	for _, robot := range robots {
		robotPos := robot.Pos()
		_, inMap := countPositions[robotPos]

		if inMap {
			countPositions[robotPos]++
		} else {
			countPositions[robotPos] = 1
		}
	}

	return countPositions
}

func getQuadrantIndex(x, y int) int {
	halfX := int(X / 2)
	halfY := int(Y / 2)

	if x == halfX || y == halfY {
		return -1
	}

	if x < halfX && y < halfY {
		return 0
	} else if x < halfX && y > halfY {
		return 1
	} else if x > halfX && y < halfY {
		return 2
	} else if x > halfX && y > halfY {
		return 3
	}

	panic("Error in quadrant index")
}

func countRobotsInQuadrants(robotsCount map[string]int) []int {
	quadrantsCount := make([]int, 4)
	counted := make(map[string]bool)

	for _, robot := range robots {
		x, y := robot.Coord()
		quadrantIndex := getQuadrantIndex(x, y)

		if quadrantIndex >= 0 {
			pos := robot.Pos()

			_, processedRobot := counted[pos]
			if !processedRobot {
				counted[pos] = true
			} else {
				continue
			}

			count, _ := robotsCount[pos]

			quadrantsCount[quadrantIndex] += count
		}
	}

	return quadrantsCount
}

func multiplyAllQuadrants(quadrants []int) int {
	result := 1
	for _, n := range quadrants {
		result *= n
	}

	return result
}

func partOne() int {
	seconds := 100

	for seconds > 0 {
		for _, robot := range robots {
			robot.walk()
		}

		seconds--
	}

	robotsCount := robotsPerPosition()
	quadrants := countRobotsInQuadrants(robotsCount)

	return multiplyAllQuadrants(quadrants)
}

func main() {
	fmt.Printf("Part 1: %d\n", partOne())
}
