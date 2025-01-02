'use strict';

import fs from 'fs';

const expressions = fs.readFileSync('./resources/day3.in', 'utf-8');

function multiply(expression) {
  const numbersPattern = /\d+/g;
  const numbers = extractAllPatterns(expression, numbersPattern)

  const solvedMuls = [];
  for (let i = 0; i < numbers.length; i=i+2) {
    solvedMuls.push(numbers[i] * numbers[i+1]);
  }

  return solvedMuls;
}

function extractAllPatterns(expression, regex) {
  const matches = [];
  let newMatch;

  while((newMatch = regex.exec(expression)) !== null) {
    matches.push(newMatch[0]);
  }

  return matches;
}

function partOne() {
  const extract = /mul\(\d+,\s?\d+\)/gi;

  return extractAllPatterns(expressions, extract)
    .flatMap((find) => multiply(find))
    .reduce((acc, number) => acc + number);
}

console.log(`Part 1: ${partOne()}`);
