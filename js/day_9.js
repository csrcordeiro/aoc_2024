'use strict';

import fs from 'fs';

const data = fs.readFileSync('../resources/day9.in', 'utf-8');

const diskMap = data.replace('\n', '');

class MemoryBlock {
  constructor(startIndex, size) {
    this.startIndex = startIndex;
    this.size = size;
  }

  canMoveFile(memoryBlock) {
    return this.startIndex >= memoryBlock.startIndex && this.size <= memoryBlock.size
  }
}

function createDisk() {
  let fileBlockCounter = 0;

  return diskMap.split('').flatMap((item, idx) => {
    const block = parseInt(item);

    if (idx % 2 == 0) {
      const result = [];

      for (let i = 0; i < block; i++) {
        result.push(fileBlockCounter);
      }

      fileBlockCounter++;

      return result;

    } else {
      return '.'.repeat(block).split('');
    }
  });
}

function defrag(disk) {
  const diskCopy = [...disk];

  let freeIndex = 0;
  let fileIndex = diskCopy.length - 1;

  while (freeIndex + 1 < fileIndex - 1) {
    while (diskCopy[freeIndex] != '.') {
      freeIndex++;
    }

    while (diskCopy[fileIndex] == '.') {
      fileIndex--;
    }

    diskCopy[freeIndex] = diskCopy[fileIndex];
    diskCopy[fileIndex] = '.';
  }

  return diskCopy;
}

function extractMemoryBlocks() {
  const fileBlocks = [];
  const freeBlocks = [];
  const blocks = diskMap.split('');

  let index = 0;
  for (let i = 0; i < blocks.length; i++) {
    const value = parseInt(blocks[i]);
    const memory = new MemoryBlock(index, value);

    if (i % 2 == 0) {
      fileBlocks.push(memory);
    } else {
      freeBlocks.push(memory);
    }

    index += value;
  }

  return [fileBlocks, freeBlocks];
}

function defragInBlocks(disk) {
  const diskCopy = [...disk];
  const [fileBlocks, freeBlocks] = extractMemoryBlocks();

  for (let i = fileBlocks.length - 1; i >= 0; i--) {
    const fileMem = fileBlocks[i];

    for (let j = 0; j < freeBlocks.length; j++) {
      const freeMem = freeBlocks[j];

      if (fileMem.canMoveFile(freeMem)) {
        for (let m = 0; m < fileMem.size; m++) {
          diskCopy[freeMem.startIndex] = diskCopy[fileMem.startIndex + m];

          diskCopy[fileMem.startIndex + m] = '.';

          freeMem.startIndex++;
        }

        freeMem.size = freeMem.size - fileMem.size;
        fileMem.size = 0;
      }
    }
  }

  return diskCopy;
}

function checksum(disk) {
  return disk.map(
    (e, idx) => [idx, e]
  ).filter(
    (e) => e[1] != '.'
  ).reduce(
    (acc, e) => acc + (e[0] * e[1]),
    0
  );
}

function partOne() {
  return checksum(defrag(createDisk()));
}

function partTwo() {
  return checksum(defragInBlocks(createDisk()));
}

console.log(`Part 1: ${partOne()}`);
console.log(`Part 2: ${partTwo()}`);
