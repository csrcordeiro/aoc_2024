# -*- coding: utf-8 -*-

from collections import namedtuple

data = None
with open('../resources/day10.in', 'r') as file:
    data = file.read().split("\n")

matrix = [ [int(charr) for charr in line] for line in data if len(line) > 0]

MAX_X = len(matrix[0])
MAX_Y = len(matrix)


Point = namedtuple('Point', ['x', 'y', 'value'])


def map_starting_points():
    starting_points = []

    for i in range(len(matrix)):
        for j in range(len(matrix[i])):
            if matrix[i][j] == 0:
                starting_points.append(Point(j, i, 0))

    return starting_points


def in_bound(x, y):
    return x >= 0 and x < MAX_X and y >= 0 and y < MAX_Y


def get_safe_coord(x, y):
    return (x, y) if in_bound(x, y) else None


def next_coord(x, y, direction):
    if 'up' == direction:
        return get_safe_coord(x, y - 1)

    elif 'down' == direction:
        return get_safe_coord(x, y + 1)

    elif 'right' == direction:
        return get_safe_coord(x + 1, y)

    elif 'left' == direction:
        return get_safe_coord(x - 1, y)

    raise Exception(f"Unknown direction {direction}")


def walk(start):
    x, y = (start.x, start.y)
    possible_points = []

    for direction in ['up', 'right', 'down', 'left']:
        coord = next_coord(x, y, direction)

        if not coord:
            continue

        c_x, c_y = coord
        value = matrix[c_y][c_x]

        point = Point(c_x, c_y, value)

        if (point.value - start.value) == 1:
            possible_points.append(point)

    return possible_points


def part_one():
    starts = map_starting_points()
    score = 0

    for start in starts:
        processing_queue = []
        point = start
        visited = set()

        while True:
            processing_queue = walk(point) + processing_queue

            if len(processing_queue) == 0:
                break

            point = processing_queue.pop(0)

            if (start, point) in visited:
                continue

            if point.value == 9:
                score += 1

            visited.add((start, point))

    return score


print(f"Part 1: {part_one()}")
