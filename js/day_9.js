'use strict';

import fs from 'fs';

const data = fs.readFileSync('../resources/day9.in', 'utf-8');

const diskMap = data.replace('\n', '');

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

console.log(`Part 1: ${partOne()}`);
