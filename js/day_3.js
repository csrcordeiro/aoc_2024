'use strict';

import fs from 'fs';

const expressions = fs.readFileSync('../resources/day3.in', 'utf-8');

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

function tokenize(tokens) {
  let mustCount = true;
  const selection = [];

  for (let i = 0; i < tokens.length; i++) {
    const token = tokens[i];

    if (token.includes("don't")) {
      mustCount = false;
      continue;
    }

    if (token.includes("do")) {
      mustCount = true;
      continue;
    }

    if (token.includes("mul") && mustCount) {
      selection.push(token);
    }
  }

  return selection;
}

function partOne() {
  const extract = /mul\(\d+,\s?\d+\)/gi;

  return extractAllPatterns(expressions, extract)
    .flatMap((find) => multiply(find))
    .reduce((acc, number) => acc + number);
}

function partTwo() {
  const extract = /(do\(\))|(don't\(\))|(mul\(\d+,\s?\d+\))/gi;

  return tokenize(
    extractAllPatterns(expressions, extract)
  )
    .flatMap((find) => multiply(find))
    .reduce((acc, number) => acc + number);
}

console.log(`Part 1: ${partOne()}`);
console.log(`Part 2: ${partTwo()}`);
