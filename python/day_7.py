# -*- coding: utf-8 -*-

from functools import reduce

data = None
with open('../resources/day7.in', 'r') as file:
    data = file.read().split('\n')
    data = [ x.split(':') for x in data ]
    data = [ x for x in data if len(x) > 1 ]
    data = [
        [
            int(x[0]),
            [ int(y) for y in x[1].split() ],
        ] for x in data
    ]

def permutations(n, ops):
    old = ops
    new = None

    if n == 1:
        return ops

    for i in range(1, n + 1):
        new = ( old_item + op for old_item in old for op in ops )

        if i < n - 1:
            old = new

    return new


def interpret_symbol(n1, n2, symbol):
    if symbol == 'c':
        return int(f"{n1}{n2}")

    if symbol == '+':
        return n1 + n2

    return n1 * n2


def apply_operations(operations, equations):
    symbols = ( s for s in operations )

    return reduce(
        lambda acc, number : interpret_symbol(acc, number, next(symbols)),
        equations
    )


def calculate(entry, permutations):
    target, equations = entry

    for symbols in permutations:
        if apply_operations(symbols, equations) == target:
            return target

    return 0


def sum_possibilities(entry, possibilities):
    _, equations = entry
    ops = permutations(len(equations) - 1, possibilities)
    return calculate(entry, ops)


def sum_entries(operations):
    return sum(( sum_possibilities(entry, operations) for entry in data ))


def part_one():
    return sum_entries(['+', '*'])


def part_two():
    return sum_entries(['+', '*', 'c'])


print(f"Part 1: {part_one()}")
print(f"Part 2: {part_two()}")
