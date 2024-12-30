'use strict';

import fs from 'fs';

const data = fs.readFileSync('./resources/day7.in', 'utf-8');

const values_numbers = data.split("\n")
  .map((line) => line.split(': '))
  .map((line) => line.length > 1 ? [line[0], line[1].split(' ')] : [])
  .filter((line) => line.length > 0)
  .map((numbers) => [parseInt(numbers[0]), numbers[1].map((e) => parseInt(e))]);

function possibilities(n, ops) {
  let init = [...ops];

  if (n === 1)
    return init;

  let permutations = [];

  for (let i = 1; i < n; i++) {
    for (let j = 0; j < init.length; j++) {
      const str = init[j];

      for (let o = 0; o < ops.length; o++) {
        permutations.push(str + ops[o]);
      }
    }

    if (i < n - 1) {
      init = permutations;
      permutations = [];
    }
  }

  return permutations;
}

function calculate(equations, permutations) {
  const results = [];
  for (let i = 0; i < permutations.length; i++) {
    const ops = permutations[i].split('');

    results.push(
      equations.reduce((acc, number) => {
        const op = ops.shift();

        if (op === 'c') {
          return parseInt(`${acc}${number}`);
        }

        if (op === '+') {
          return acc + number;
        } else {
          return acc * number;
        }
      })
    );
  }
  return results;
}

function processSymbols(symbols) {
  return values_numbers.map((entry) => {
    const target = entry[0];
    const equations = entry[1];
    const permutations = possibilities(equations.length - 1, symbols);
    const calculatedValues = calculate(equations, permutations);

    return calculatedValues.some((e) => e === target) ? target : 0;

  }).reduce(
    (acc, number) => acc + number
  );
}

function partOne() {
  return processSymbols(['+', '*']);
}

function partTwo() {
  // I'll be using 'c' as a concatanation symbol.
  return processSymbols(['+', '*', 'c']);
}

console.log(`Part 1: ${partOne()}`);
console.log(`Part 2: ${partTwo()}`);
