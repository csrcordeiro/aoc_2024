# -*- coding: utf-8 -*-

from collections import namedtuple

Point = namedtuple('Point', ['x', 'y'])

data = None
with open('../resources/day8.in', 'r') as file:
    data = file.read().split('\n')
    data = [list(s) for s in data]
    data = [d for d in data if len(d) > 0]

MAX_X = len(data[0])
MAX_Y = len(data)


def extract_antennas():
    antennas = {}
    for y, line in enumerate(data):
        for x, element in enumerate(line):
            if element == '.':
                continue

            if element not in antennas:
                antennas[element] = []

            antennas[element].append(Point(x, y))

    return antennas


def generate_points(point, other_point, propagate_signal):
    diff_x = point.x - other_point.x
    diff_y = point.y - other_point.y

    points = [
        Point(point.x + diff_x, point.y + diff_y),
        Point(other_point.x - diff_x, other_point.y - diff_y)
    ]

    if not propagate_signal:
        return points

    pa, pb = points
    signal = 0
    while signal < MAX_X:
        pa = Point(pa.x + diff_x, pa.y + diff_y)
        pb = Point(pb.x - diff_x, pb.y - diff_y)

        points.extend((
            pa, pb
        ))
        signal += 1

    return [point, other_point] + points


def find_antinode(coords, propagate_signal=False):
    antinodes = []
    for idx, point in enumerate(coords):
        for other_point in coords[idx+1:]:
            antinodes.extend(
                generate_points(point, other_point, propagate_signal)
            )

    return antinodes


def in_bound(antinode):
    x, y = (antinode.x, antinode.y)
    return x >= 0 and x < MAX_X and y >= 0 and y < MAX_Y


def find_all_antinodes(antennas, propagate_signal=False):
    result = []
    for key, coords in antennas.items():
        antinodes = find_antinode(coords, propagate_signal)
        result.extend(filter(lambda x: in_bound(x), antinodes))

    return set(result)

def part_one(antennas):
    return len(find_all_antinodes(antennas, propagate_signal=False))

def part_two(antennas):
    return len(find_all_antinodes(antennas, propagate_signal=True))


antennas = extract_antennas()

print(f"Part 1: {part_one(antennas)}")
print(f"Part 2: {part_two(antennas)}")
