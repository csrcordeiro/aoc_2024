'use strict';

import fs from 'fs';

const data = fs.readFileSync('../resources/day12.in', 'utf-8');

const matrix = data.split('\n').map((line) => line.split(''));

const MAX_X = matrix[0].length;
const MAX_Y = matrix.length;


function inBound(x, y) {
  return x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y;
}

function nextPoint(x, y, direction) {
  switch(direction) {
    case 'up':
      return [x, y - 1];

    case 'right':
      return [x + 1, y];

    case 'down':
      return [x, y + 1];

    case 'left':
      return [x - 1, y];

    default:
      throw new Error("Unsuported direction " + direction);
  }
}

function walkMatrix(x, y) {
  const points = [[x,y]];
  const visited = new Set([`${x},${y}`]);
  const plant = matrix[y][x];

  let queue = [[x,y]];
  while(queue.length > 0) {
    const [x, y] = queue.pop();

    const next = [];
    for (const direction of ['up', 'right', 'down', 'left']) {
      const [px, py] = nextPoint(x, y, direction);
      const coord = `${px},${py}`;

      if (inBound(px, py) && !visited.has(coord) && matrix[py][px] === plant) {
        next.push([px, py]);
      }

      visited.add(coord);
    }

    for (const e of next) {
      queue.push(e);
      points.push(e);
    }
  }

  return points;
}

function countFreeSides(currentPoint, points) {
  let emptyCounter = 0;
  const [x, y] = currentPoint;

  for (const direction of ['up', 'right', 'down', 'left']) {
    const point = nextPoint(x, y, direction);

    if (!points.some((e) => (e[0] === point[0] && e[1] === point[1]))) {
      emptyCounter++;
    }
  }

  return emptyCounter;
}

function mapPlants() {
  const plants = new Map();
  const visited = new Set();

  for (let y = 0; y < matrix.length; y++) {
    const line = matrix[y];

    for (let x = 0; x < line.length; x++) {
      const pointRepr = `${x},${y}`;

      if (visited.has(pointRepr))
        continue;

      const plant = line[x];

      if (!plants.has(plant))
        plants.set(plant, []);

      const points = walkMatrix(x, y);

      points.forEach((point) => visited.add(`${point[0]},${point[1]}`));

      plants.get(plant).push(points);
    }
  }

  return plants;
}


function partOne() {
  const plants = mapPlants();
  const clusters = plants.values();

  return clusters.flatMap((clusters) => {
    return clusters.map((cluster) => {
      const area = cluster.length;
      const perimeter = cluster
        .map((point) => countFreeSides(point, cluster))
        .reduce((acc, number) => acc + number, 0);

      return area * perimeter;
    });
  }).reduce((acc, number) => acc + number);
}


console.log(`Part 1: ${partOne()}`);

