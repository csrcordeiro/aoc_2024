# -*- coding: utf-8 -*-

from itertools import chain

data = None
with open('../resources/day1.in', 'r') as file:
    data = file.read().split('\n')


def extract_columns():
    col1 = []
    col2 = []

    for e in data:
        n1, n2 = e.split()
        col1.append(int(n1))
        col2.append(int(n2))

    return (col1, col2)


def part_one(c1, c2):
    col1 = sorted(c1)
    col2 = sorted(c2)

    flatten_zipped_column = chain.from_iterable(zip(col1, col2))
    distances = (
        abs(x - next(flatten_zipped_column))
        for x in flatten_zipped_column
    )

    return sum(distances)


def part_two(c1, c2):
    return sum((n * c2.count(n) for n in c1))


col1, col2 = extract_columns()

print(f"Part 1: {part_one(col1, col2)}")
print(f"Part 2: {part_two(col1, col2)}")
