'use strict';

import fs from 'fs';

const data = fs.readFileSync('../resources/day1.in', 'utf-8');

const numbers = data.split("\n").map((line) => {
  return line.split(/\s+/).map((e) => parseInt(e));
});

const firstColumn = numbers.map((e) => e[0]);
const secondColumn = numbers.map((e) => e[1]);

function partOne() {
  const sortFunc = (a, b) => a - b;

  const firstSorted = [...firstColumn].sort(sortFunc);
  const secondSorted = [...secondColumn].sort(sortFunc);

  return firstSorted.map((e, idx) => {
    return Math.abs(e - secondSorted[idx]);
  }).reduce((acc, e) => acc + e);
}

function partTwo() {
  return firstColumn.map((e) => {
    const reps = secondColumn.filter((s) => s === e).length;

    return e * reps;
  }).reduce((acc, e) => acc + e);
}

console.log(`Part 1: ${partOne()}`);
console.log(`Part 2: ${partTwo()}`);
