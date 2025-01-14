'use strict';

import fs from 'fs';

const data = fs.readFileSync('../resources/day10.in', 'utf-8');

const matrix = data.split("\n").map((line) => {
  return line.split('').map((e) => parseInt(e));
}).filter((e) => e.length > 0);


const MAX_X = matrix[0].length;
const MAX_Y = matrix.length;


function mapTrailHeads() {
  const result = [];
  for (let y = 0; y < matrix.length; y++) {
    for (let x = 0; x < matrix[y].length; x++) {
      const value = matrix[y][x];

      if (value === 0)
        result.push([x, y, 0]);
    }
  }

  return result;
}


function isInBound(x, y) {
  return x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y;
}


function getSafeCoord(x, y) {
  return isInBound(x, y) ? [x, y, matrix[y][x]] : null;
}


function nextCoord(x, y, direction) {
  if ('up' === direction)
    return getSafeCoord(x, y - 1);

  else if ('down' === direction)
    return getSafeCoord(x, y + 1);

  else if ('right' === direction)
    return getSafeCoord(x + 1, y);

  else if ('left' === direction)
    return getSafeCoord(x - 1, y);

  throw new Error(`Unknown direction ${direction}`);
}


function walk(start) {
  const [x, y, value] = start;
  const possiblePoints = [];
  const directions = ['up', 'right', 'down', 'left'];

  for (let i = 0; i < directions.length; i++) {
    const coord = nextCoord(x, y, directions[i]);

    if (coord == null) {
      continue;
    }

    if ((coord[2] - value) == 1) {
      possiblePoints.push(coord);
    }

  }

  return possiblePoints;
}


function walkTrailhead(start, unique) {
  let score = 0;
  let processingQueue = [];
  let point = start;
  const visited = new Set();

  while(true) {
    processingQueue = walk(point).concat(processingQueue);

    if (processingQueue.length === 0)
      break;

    point = processingQueue.shift();

    if (unique && visited.has(`${point}`))
      continue;

    if (point[2] === 9)
      score++;

    visited.add(`${point}`);
  }

  return score;
}


function walkAllTrailheads(unique) {
  return mapTrailHeads()
    .map((start) => walkTrailhead(start, unique))
    .reduce((acc, number) => acc + number);
}


function partOne() {
  return walkAllTrailheads(true);
}


function partTwo() {
  return walkAllTrailheads(false);
}

console.log(`Part 1: ${partOne()}`);
console.log(`Part 2: ${partTwo()}`);
