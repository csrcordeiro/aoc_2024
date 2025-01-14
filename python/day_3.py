# -*- coding: utf-8 -*-

import re


data = None
with open('../resources/day3.in', 'r') as file:
    data = file.read().split('\n')
    data = [ line for line in data if len(line) > 0 ]


def extract(pattern):
    results = []
    for line in data:
        muls = re.findall(pattern, line)
        results.extend(muls)

    return results


def multiply(mul):
    numbers = re.findall(r"\d+", mul)
    return int(numbers[0]) * int(numbers[1])


def tokenize(patterns):
    selection = []
    must_multiply = True

    for pattern in patterns:
        if "don't" in pattern:
            must_multiply = False
        elif "do" in pattern:
            must_multiply = True
        else:
            if must_multiply:
                selection.append(pattern)

    return selection


def part_one():
    pattern = r"(mul\(\d+,\d+\))"
    muls = extract(pattern)
    return sum(multiply(mul) for mul in muls)


def part_two():
    pattern = r"(do\(\)|don't\(\)|mul\(\d+,\d+\))"
    muls = tokenize(extract(pattern))
    return sum(multiply(mul) for mul in muls)


print(f"Part 1: {part_one()}")
print(f"Part 2: {part_two()}")
