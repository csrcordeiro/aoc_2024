'use strict';

import fs from 'fs'

const data = fs.readFileSync('./resources/day6.in', 'utf-8');

const grid = data.split('\n');

const Y_MAX = grid.length;
const X_MAX = grid[0].length;

const DIRECTION_CHANGE = {
  '^': '>',
  '>': 'v',
  'v': '<',
  '<': '^',
};

function findInitialGuardCoord() {
  for (let i = 0; i < grid.length; i++) {
    for (let j = 0; j < grid[i].length; j++) {
      if (grid[i][j] === '^') {
        return [j, i, '^'];
      }
    }
  }
}

function walk(x, y, direction) {
  switch(direction) {
  case '^':
    return [x, y-1, direction];
  case '>':
    return [x + 1, y, direction];
  case 'v':
    return [x, y + 1, direction];
  case '<':
    return [x - 1, y, direction];
  default:
    throw new Error(`Unsupported direction ${direction}`);
  }
}

function isInGrid(x, y) {
  return (x < X_MAX && x >= 0) && (y < Y_MAX && y >= 0);
}

function partOne() {
  let [x, y, direction] = findInitialGuardCoord();
  const breadcrumb = new Set();

  do {
    const [nextX, nextY, nextDirection] = walk(x, y, direction);

    if (!isInGrid(nextX, nextY)) {
      break;
    }

    if (grid[nextY][nextX] === '#') {
      direction = DIRECTION_CHANGE[direction];
    } else {
      [x, y, direction] = [nextX, nextY, nextDirection];
    }

    breadcrumb.add(`${x},${y}`);

  } while(true);


  return breadcrumb.size;
}

console.log(`Part 1: ${partOne()}`);
