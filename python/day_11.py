# -*- coding: utf-8 -*-

data = None
with open('../resources/day11.in', 'r') as file:
    data = file.read().split('\n')
    data = [
        int(c)
        for line in data
        for c in line.split(' ')
        if len(line) != 0
    ]


def map_stones():
    stones = {}

    for stone in data:
        stones[stone] = stones.get(stone, 1)

    return stones


def apply_rule(stone):
    stone_str = str(stone)

    if stone == 0:
        return [1]

    elif len(stone_str) % 2 == 0:
        size = len(stone_str)
        return [int(stone_str[0:size//2]), int(stone_str[size//2:size])]

    else:
        return [stone * 2024]


def blink(n, stones):
    results = stones
    while n != 0:
        new_order = {}

        for stone, value in results.items():
            new_stones = apply_rule(stone)

            for new_stone in new_stones:
                new_order[new_stone] = new_order.get(new_stone, 0) + value

        results = new_order
        n -= 1

    return results


def check_stones(blinks):
    return sum((
        items[1]
        for items in blink(blinks, map_stones()).items()
    ))


def part_one():
    return check_stones(25)


def part_two():
    return check_stones(75)


print(f"Part 1: {part_one()}")
print(f"Part 2: {part_two()}")
