# -*- coding: utf-8 -*-

from itertools import repeat

data = None
with open('../resources/day2.in', 'r') as file:
    data = file.read().split('\n')
    data = [d for d in data if len(d) > 0]
    data = [d.split(" ") for d in data]

measures = list(
    map(lambda measure: [ int(m) for m in measure ], data)
)


def check_measure(l1, l2, first_direction):
    diff = l2 - l1
    measure = abs(diff)

    return direction(diff) == first_direction and measure > 0 and measure < 4


def direction(diff):
    if diff < 0:
        return 'up'

    if diff == 0:
        return 'neutral'

    return 'down'


def is_safe_report(levels):
    first_direction = direction(levels[1] - levels[0])
    for i in range(0, len(levels) - 1):
        if not check_measure(levels[i], levels[i+1], first_direction):
            return False

    return True


def combinations(levels):
    return (
        [
            level
            for idx, level in enumerate(levels)
            if idx != index
        ] for index in range(0, len(levels))
    )


def part_one():
    return len(tuple(filter(is_safe_report, measures)))


def part_two():
    combs = [ combinations(level) for level in measures ]
    checked_combs = [ any((is_safe_report(comb) for comb in sets)) for sets in combs ]
    return len([comb for comb in checked_combs if comb])


print(f"Part 1: {part_one()}")
print(f"Part 2: {part_two()}")
