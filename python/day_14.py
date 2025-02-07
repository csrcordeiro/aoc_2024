# -*- coding: utf-8 -*-

import re

from functools import reduce

MAX_X = 101
MAX_Y = 103


data = None
with open('../resources/day14.in', 'r') as file:
    data = file.read().split('\n')
    data = [ line for line in data if len(line) > 0 ]


def make_robots():
    robots = []
    for d in data:
        numbers = tuple(int(n) for n in re.findall(r"(-?\d+)+", d))

        robots.append(Robot(position=numbers[0:2], velocity=numbers[2:]))

    return robots


class Robot(object):

    def __init__(self, position, velocity):
        self.position = position
        self.velocity = velocity

    def walk(self):
        x, y = self.position
        vx, vy = self.velocity

        x += vx
        y += vy

        self.position = (
            self._compensate(x, MAX_X),
            self._compensate(y, MAX_Y),
        )

    def _compensate(self, position, max_size):
        if position >= max_size:
            return position - max_size

        if position < 0:
            return max_size + position

        return position


    def quadrant_number(self):
        halfX = int(MAX_X / 2)
        halfY = int(MAX_Y / 2)

        x, y = self.position

        if x == halfX or y == halfY:
            return -1

        if x < halfX and y < halfY:
            return 0
        elif x < halfX and y > halfY:
            return 1
        elif x > halfX and y < halfY:
            return 2
        elif x > halfX and y > halfY:
            return 3

    def __repr__(self):
        x, y = self.position
        v1, v2 = self.velocity
        return f"Robot ({x},{y}) ({v1},{v2})"


robots = make_robots()


def count_quadrants():
    quadrants = [0] * 4

    for robot in robots:
        quadrant = robot.quadrant_number()

        if quadrant >= 0:
            quadrants[quadrant] += 1

    return quadrants


def part_one():
    seconds = 100
    while seconds > 0:
        for robot in robots:
            robot.walk()

        seconds -= 1

    return reduce(lambda x, y: x * y, count_quadrants())


print(f"Part 1: {part_one()}")
