# -*- coding: utf-8 -*-

import re
import math


def can_process(target, numbers, value, part=1):
    if value > target:
        return False

    if len(numbers) == 0:
        return target == value

    x = numbers[0]
    new_numbers = numbers[1:]

    if part == 1:
        return can_process(target, new_numbers, value*x) or \
                can_process(target, new_numbers, value+x)

    else:
        return can_process(target, new_numbers, value*x, part=2) or \
                can_process(target, new_numbers, value+x, part=2) or \
                    can_process(target, new_numbers, value * 10 ** ndigits(x) + x, part=2)


def ndigits(x):
    return int(math.log(x) / math.log(10) + 1)


with open("../resources/day7.in") as f:
    data = (line for line in f.readlines())
    data = (re.findall(r"\d+", line) for line in data)
    data = (tuple(map(lambda x: int(x), line)) for line in data)


part1 = 0
part2 = 0
for row in data:
    part1 += row[0] if can_process(row[0], row[2:], row[1], part=1) else 0
    part2 += row[0] if can_process(row[0], row[2:], row[1], part=2) else 0


print(f"Part 1: {part1}")
print(f"Part 2: {part2}")
