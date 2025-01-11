# -*- coding: utf-8 -*-

data = None
with open('../resources/day9.in', 'r') as file:
    data = file.read().split('\n')
    data = [c for line in data for c in line]


class MemoryBlock(object):
    def __init__(self, start_index, size):
        self.start_index = start_index
        self.size = size

    def can_move_file(self, other_block):
        return self.start_index < other_block.start_index and \
                    self.size >= other_block.size

    def resize(self, other_block):
        self.size = self.size - other_block.size

    def __str__(self):
        return f"MemoryBlock: start {self.start_index} size {self.size}"

    def __repr__(self):
        return str(self)


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

    while first_index + 1 < last_index - 1:
        while new_disk[first_index] != '.':
            first_index += 1

        while new_disk[last_index] == '.':
            last_index -= 1

        new_disk[first_index] = new_disk[last_index]
        new_disk[last_index] = '.'

    return new_disk


def extract_blocks():
    file_blocks = []
    free_blocks = []

    index = 0
    for idx, d in enumerate(data):
        size = int(d)

        block = MemoryBlock(index, size)

        if idx % 2 == 0:
            file_blocks.append(block)
        else:
            free_blocks.append(block)

        index += size

    return (file_blocks, free_blocks)


def defrag_in_block(disk):
    file_blocks, free_blocks = extract_blocks()

    disk_copy = list(disk)

    for file_block in reversed(file_blocks):
        for free_block in free_blocks:
            if free_block.can_move_file(file_block):

                for i in range(file_block.size):
                    disk_copy[free_block.start_index] = \
                        disk[file_block.start_index]

                    disk_copy[file_block.start_index + i] = '.'

                    free_block.start_index += 1

                free_block.resize(file_block)
                file_block.size = 0

    return disk_copy


def checksum(disk):
    return sum((v[0] * v[1]) for v in enumerate(disk) if v[1] != '.')


def part_one():
    return checksum(defrag(create_disk()))


def part_two():
    return checksum(defrag_in_block(create_disk()))


print(f"Part 1: {part_one()}")
print(f"Part 2: {part_two()}")

