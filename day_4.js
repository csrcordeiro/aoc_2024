'use strict';

import fs from 'fs';

const data = fs.readFileSync('./resources/day4.in', 'utf-8');

const lines = data.split("\n").filter((e) => e.length > 0);
const matrix = lines.map((e) => e.split(''));

function extractXsPositions() {
  return matrix.flatMap((line, y) => {
    return line.map((charr, x) => {
      if (charr === 'X')
        return [x, y];
    });
  }).filter(
    (truthy) => truthy
  );
}

function count(xIndexes) {
  let counter = 0;

  for (const coord of xIndexes) {
    const xx = coord[0];
    const xy = coord[1];

    counter += countHorizontal(xx, xy);
    counter += countVertical(xx, xy);
    counter += countDiagonals(xx, xy);
  }

  return counter;
}

function countDiagonals(xx, xy) {
  const firstDiagonal = frontSlice(xy).map((line, idx) => {
    return line[xx + idx];
  }).filter((truthy) => truthy);

  const secondDiagonal = backSlice(xy).reverse().map((line, idx) => {
    const index = xx - idx;

    return index >= 0 ? line[index] : '';
  }).filter((truthy) => truthy);

  const thirdDiagonal = frontSlice(xy).map((line, idx) => {
    const index = xx - idx;

    return index >= 0 ? line[index] : '';
  }).filter((truthy) => truthy);

  const fourthDiagonal = backSlice(xy).reverse().map((line, idx) => {
    return line[xx + idx];
  }).filter((truthy) => truthy);

  return countXmas(
    arrayToString(firstDiagonal),
    arrayToString(secondDiagonal),
    arrayToString(thirdDiagonal),
    arrayToString(fourthDiagonal),
  );
}

function backSlice(xy) {
  return matrix.slice(xy-3, xy+1);
}

function frontSlice(xy) {
  return matrix.slice(xy, xy+4);
}

function countVertical(xx, xy) {
  const vSlice = matrix.slice(xy, xy+4).map((line) => line[xx]);
  const vBackSlice = matrix.slice(xy-3, xy+1).map((line) => line[xx]);

  return countXmas(
    arrayToString(vSlice),
    arrayToString(vBackSlice),
  );
}

function countHorizontal(xx, xy) {
  const hSlice = matrix[xy].slice(xx, xx+4);
  const hBackSlice = matrix[xy].slice(xx-3, xx+1);

  return countXmas(
    arrayToString(hSlice),
    arrayToString(hBackSlice),
  );
}

function countXmas(...strings) {
  return strings.map(
    (slice) => isXmas(slice)
  ).filter(
    (truthy) => truthy
  ).length;
}

function arrayToString(array) {
  return array.join('');
}

function isXmas(string) {
  return string === 'XMAS' || string === 'SAMX';
}

const indexes = extractXsPositions();

console.log(`Part 1: ${count(indexes)}`);

