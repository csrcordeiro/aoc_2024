'use strict';

import fs from 'fs';

const data = fs.readFileSync('./resources/day7.in', 'utf-8');

const values_numbers = data.split("\n")
  .map((line) => line.split(': '))
  .map((line) => line.length > 1 ? [line[0], line[1].split(' ')] : [])
  .filter((line) => line.length > 0)
  .map((numbers) => [parseInt(numbers[0]), numbers[1].map((e) => parseInt(e))]);

function possibilities(n) {
  if (n === 1) {
    return ['+*'];
  }

  let init = [
    '++',
    '+*',
    '*+',
    '**',
  ]

  if (n === 2) {
    return init;
  }

  let permutations = [];
  for (let i = 2; i < n; i++) {
    for (let j = 0; j < init.length; j++) {
      const str = init[j];
      permutations.push(init[j] + '+');
      permutations.push(init[j] + '*');
    }

    if (i < n - 1) {
      init = permutations;
      permutations = [];
    }
  }

  return permutations;
}

function calculate(equations, permutations) {
  if (equations.length === 2) {
    return [
      equations[0] + equations[1],
      equations[0] * equations[1],
    ];
  }

  const results = [];
  for (let i = 0; i < permutations.length; i++) {
    const ops = permutations[i].split('');

    results.push(
      equations.reduce((acc, number) => {
        const op = ops.shift();

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

function partOne() {
  return values_numbers.map((entry) => {
    const target = entry[0];
    const equations = entry[1];
    const permutations = possibilities(equations.length - 1);
    const calculatedValues = calculate(equations, permutations);

    return calculatedValues.some((e) => e === target) ? target : 0;

  }).reduce(
    (acc, number) => acc + number
  );
}

console.log(`Part 1: ${partOne()}`);

