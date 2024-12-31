'use strict';

import fs from 'fs';

const data = fs.readFileSync('./resources/day2.in', 'utf-8');

const measures = data.split("\n").map(
  (line) => line.split(" ").map((e) => parseInt(e))
).filter((e) => e.length > 1);

function direction(levelDiff) {
  if (levelDiff < 0) {
    return "up";
  }

  if (levelDiff === 0) {
    return "neutral";
  }

  return "down";
}

function checkMeasure(l1, l2, firstDirection) {
  const diff = l2 - l1;
  const measure = Math.abs(diff);

  return direction(diff) === firstDirection && measure > 0 && measure < 4;
}

function isSafe(levels) {
  const firstDirection = direction(levels[1] - levels[0]);

  return levels.slice(0, levels.length - 1).map((e, idx) => {
    return checkMeasure(e, levels[idx + 1], firstDirection);
  }).every((e) => e);
}

function partOne() {
  return measures.map(
    (levels) => isSafe(levels))
  .filter(
    (e) => e
  ).length;
}

console.log(`Part 1: ${partOne()}`);
