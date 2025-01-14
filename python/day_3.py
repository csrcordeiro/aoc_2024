# -*- coding: utf-8 -*-

import re


data = None
with open('../resources/day3.in', 'r') as file:
    data = file.read().split('\n')
    data = [ line for line in data if len(line) > 0 ]


def extract_pairs():
    results = []
    for line in data:
        muls = re.findall(r"(mul\(\d+,\d+\))", line)
        for mul in muls:
            numbers = re.findall(r"(\d+)+", mul)
            results.append(numbers)

    return [(int(n[0]), int(n[1])) for n in results]


def part_one():
    return sum(((p[0] * p[1]) for p in  extract_pairs()))


print(f"Part 1: {part_one()}")
