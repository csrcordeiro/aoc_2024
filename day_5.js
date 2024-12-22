'use strict';

import fs from 'fs';

const data = fs.readFileSync('./resources/day5.in', 'utf-8');

const lines = data.split(/\n/);

const indexEmptyLine = lines.indexOf('');

const rules = lines.slice(0, indexEmptyLine).map((rule) => {
  return rule.split('|').map((pageNumber) => parseInt(pageNumber));
});

const pages = lines.slice(indexEmptyLine + 1, -1)
  .map(
    (sequence) => {
      return sequence.split(',').map(
        (pageNumber) => parseInt(pageNumber)
      );
    }
);

function computeDependencies() {
  const dependencies = new Map();

  rules.forEach((rule) => {
    const [first, second] = rule;

    if (dependencies.has(second)) {
      dependencies.get(second).push(first);
    } else {
      dependencies.set(second, [first]);
    }
  });

  return dependencies;
}

function hasDependency(pageNumber, dependencies) {
  return dependencies.indexOf(pageNumber) > -1
}

function hasNoLock(subPages, dependencies) {
  if (dependencies === void 0 || dependencies.length == 0) {
    return false;
  }

  return subPages.every((pageNumber) => !hasDependency(pageNumber, dependencies));
}

function partOne() {
  const dependencies = computeDependencies();

  return pages.filter((sequence) => {

    return sequence.every(
      (page, idx) => {
        const nextPagesInSequence = sequence.slice(idx + 1, sequence.length);
        const dependenciesForCurrentPage = dependencies.get(page);

        return hasNoLock(nextPagesInSequence, dependenciesForCurrentPage);
      });

  }).map(
    (sequence) => sequence[parseInt(sequence.length/2)])
  .reduce(
    (acc, value) => acc + value
  );
}

console.log(`Part 1: ${partOne()}`);