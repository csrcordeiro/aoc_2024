# -*- coding: utf-8 -*-

from collections import namedtuple

data = None
with open('../resources/day12.in', 'r') as file:
    data = file.read().split("\n")

matrix = [ list(line) for line in data if len(line) > 0 ]



Point = namedtuple('Point', ['x', 'y'])


MAX_X = len(matrix[0])
MAX_Y = len(matrix)


def in_bound(point):
    x, y = point.x, point.y

    return x >= 0 and x < MAX_X and y >= 0 and y < MAX_Y


def next_point(point, direction):
    x, y = point.x, point.y

    match direction:
        case 'up':
            return Point(x, y - 1)

        case 'down':
            return Point(x, y + 1)

        case 'right':
            return Point(x + 1, y)

        case 'left':
            return Point(x - 1, y)

        case _:
            raise Exception(f"Unknown direction {direction}")


def find_cluster(point):
    visited = set((point,))
    plant = matrix[point.y][point.x]
    queue = [point]
    points = [point]

    while len(queue) > 0:
        p = queue.pop(0)

        next_points = []
        for direction in ['up', 'right', 'down', 'left']:
            n = next_point(p, direction)

            if n in visited:
                continue

            if (
                in_bound(n) and plant == matrix[n.y][n.x]
            ):
                next_points.append(n)

            visited.add(n)

        queue = queue + next_points
        points = points + next_points

    return points


def map_plants():
    visited = set()
    plants = {}

    for y, line in enumerate(matrix):
        for x, plant in enumerate(line):
            point = Point(x, y)

            if point in visited:
                continue

            cluster = find_cluster(point)

            if plant not in plants:
                plants[plant] = []

            plants[plant].append(cluster)

            for point in cluster:
                visited.add(point)

    return plants


def calculate_perimeter(point, points):
    empty_counter = 0

    for direction in ['up', 'right', 'down', 'left']:
        next_p = next_point(point, direction)

        if next_p in points:
            continue

        empty_counter += 1

    return empty_counter


# I shamelessly copied this algorithm from reddit.
# https://www.reddit.com/r/adventofcode/comments/1hcdnk0/comment/m1trm2r/
#
# This is an interesting idea of using the string pattern to find corners.
def get_sides(cluster):
    sides = 0

    edge_coord_corners = set()
    for x, y in cluster:
        for dx, dy in [(.5, .5), (.5, -.5), (-.5, .5), (-.5, -.5)]:
            edge_coord_corners.add((x + dx, y + dy))

    for x, y in edge_coord_corners:
        pattern = ""
        for dx, dy in [(.5, .5), (.5, -.5), (-.5, .5), (-.5, -.5)]:
            pattern += "X" if (x+dx, y+dy) in cluster else "O"
        if pattern in ("OXXO", "XOOX"):
            # When an edge coord is two the region meets itself all catty-corner
            sides += 2
        elif pattern.count("X") == 3 or pattern.count("O") == 3:
            # For when an edge coord is an interior or exterior corner.
            sides += 1

    return sides


def calculate_region_price(cluster, discount=False):
    area = len(cluster)
    perimeter = 0

    if discount:
        perimeter = get_sides(cluster)

    else:
        for point in cluster:
            perimeter += calculate_perimeter(point, cluster)

    return area * perimeter


def part_one():
    return sum((
        calculate_region_price(cluster, discount=False)
        for _, clusters in map_plants().items()
        for cluster in clusters
    ))


def part_two():
    return sum((
        calculate_region_price(cluster, discount=True)
        for _, clusters in map_plants().items()
        for cluster in clusters
    ))


print(f"Part 1: {part_one()}")
print(f"Part 2: {part_two()}")
