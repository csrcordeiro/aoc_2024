'use strict';

import fs from 'fs';

const data = fs.readFileSync('../resources/day8.in', 'utf-8');

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

function extractAntiNodes(propagateSignal = false) {
  let knownAntinodes = [];

  return Object.entries(ANTENNAS).flatMap((entry) => {
    const [key, values] = entry;

    const antinodes = values.flatMap((point, idx) => {

      return values.slice(idx+1).flatMap((other) => {

        const newPoints = generatePoint(point, other, propagateSignal).filter(
          (np) => knownAntinodes.every((p) => !p.equals(np)) && isInBound(np)
        );

        knownAntinodes = newPoints.concat(knownAntinodes);

        return newPoints;
      });

    }).filter(
      (e) => isInBound(e)
    );

    return antinodes;
  });
}

function generatePoint(point, otherPoint, propagateSignal) {
  const diffX = point.x - otherPoint.x;
  const diffY = point.y - otherPoint.y;

  const points = [
    new Point(point.x + diffX, point.y + diffY),
    new Point(otherPoint.x - diffX, otherPoint.y - diffY)
  ];

  if (!propagateSignal)
    return points;

  let [pa, pb] = points;
  let signal = 0;
  while (signal < MAX_X) {
    pa = new Point(pa.x + diffX, pa.y + diffY)
    pb = new Point(pb.x - diffX, pb.y - diffY)

    points.push(pa, pb);

    signal++;
  }

  points.push(point, otherPoint);

  return points;
}

function partOne() {
  return extractAntiNodes(false).length;
}

function partTwo() {
  return extractAntiNodes(true).length;
}

console.log(`Part 1: ${partOne()}`);
console.log(`Part 2: ${partTwo()}`);
