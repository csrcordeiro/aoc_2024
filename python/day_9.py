# -*- coding: utf-8 -*-


data = None
with open('../resources/day9.in', 'r') as file:
    data = file.read().split('\n')
    data = [c for line in data for c in line]

def create_disk():
    disk = list('.' * sum(map(lambda x: int(x), data)))
    file_id = -1
    is_file = True

    file_index = 0
    for charr in data:
        if is_file:
            file_id += 1

        amount = int(charr)
        value = file_id if is_file else '.'

        for _ in range(amount):
            disk[file_index] = value
            file_index += 1

        is_file = not is_file

    return disk


def defrag(disk):
    new_disk = list(disk)
    first_index = 0
    last_index = len(disk) - 1

    while first_index + 1 < last_index + 1:
        first_char = new_disk[first_index]
        while first_char != '.':
            first_index += 1
            first_char = new_disk[first_index]

        last_char = new_disk[last_index]
        while last_char == '.':
            last_index -= 1
            last_char = new_disk[last_index]

        new_disk[first_index] = last_char
        new_disk[last_index] = first_char

        first_index += 1
        last_index -= 1

    return new_disk


def checksum(disk):
    return sum((v[0] * v[1]) for v in enumerate(disk))


def part_one():
    disk = defrag(create_disk())

    return checksum(( data for data in disk if data != '.' ))


print(f"Part 1: {part_one()}")

