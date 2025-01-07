'use strict';

import fs from 'fs';

const data = fs.readFileSync('./resources/day8.in', 'utf-8');

const map = data.split('\n').map(
  (line) => line.split('')
).filter(
  (e) => e.length > 0
);

const ANTENNAS = {};

const MAX_X = map[0].length;
const MAX_Y = map.length;

class Point {
  constructor(x, y) {
    this.x = x;
    this.y = y;
  }

  toString() {
    return `Point [${this.x}, ${this.y}]`;
  }

  equals(other) {
    return this.x === other.x && this.y === other.y;
  }
}


function isInBound(point) {
  const x = point.x;
  const y = point.y;

  return x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y;
}


function mapAntennas() {
  for (let y = 0; y < map.length; y++) {
    const line = map[y];

    for (let x = 0; x < line.length; x++) {
      const antenna = line[x];

      if (antenna !== '.') {
        if (!ANTENNAS.hasOwnProperty(antenna)) {
          ANTENNAS[antenna] = [];
        }

        ANTENNAS[antenna].push(new Point(x, y));
      }
    }
  }
}

mapAntennas();


function partOne() {
  let knownAntinodes = [];

  return Object.entries(ANTENNAS).flatMap((entry) => {
    const [key, values] = entry;

    const antinodes = values.flatMap((point, idx) => {
      const [x, y] = [point.x, point.y];

      return values.slice(idx+1).flatMap((other) => {
        const [otherX, otherY] = [other.x, other.y];

        const diffX = x - otherX;
        const diffY = y - otherY;

        const newPoints = [
          new Point(x + diffX, y + diffY),
          new Point(otherX - diffX, otherY - diffY)
        ].filter(
          (np) => knownAntinodes.every((p) => !p.equals(np))
        );

        knownAntinodes = newPoints.concat(knownAntinodes);

        return newPoints;
      });

    }).filter(
      (e) => isInBound(e)
    );

    return antinodes;
  }).length;
}

console.log(`Part 1: ${partOne()}`);
