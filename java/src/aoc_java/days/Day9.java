package aoc_java.days;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> input = Common.getInput("day9.in");

		System.out.println(new Day9Solution(input).partOne());
		System.out.println(new Day9Solution(input).partTwo());
	}
}

class Day9Solution {

	private List<String> blocks;

	public Day9Solution(List<String> input) {
		this.blocks = input.stream()
			.flatMap((memBlocks) -> {
				return memBlocks.chars().mapToObj(c -> Character.toString((char) c));
			})
			.collect(Collectors.toList());
	}

	public Long partTwo() {
		List<Long> disk = defragWithBlock(createDisk());
		return checksum(disk);
	}


	public Long partOne() {
		List<Long> disk = defrag(createDisk());
		return checksum(disk);
	}

	private Long checksum(List<Long> disk) {
		return IntStream.range(0, disk.size())
			.mapToLong((index) -> {
				Long value = disk.get(index);
				if (value < 0) {
					return 0l;
				}

				return Long.valueOf(index * value);
			})
			.sum();
	}

	private List<Long> defragWithBlock(List<Long> disk) {
		List<Long> diskCopy = disk.stream()
			.map((e) -> e)
			.collect(Collectors.toList());

		List<Day9MemoryBlock> fileBlocks = new ArrayList<>();
		List<Day9MemoryBlock> freeBlocks = new ArrayList<>();

		extractBlocks(fileBlocks, freeBlocks);

		for (int i = fileBlocks.size() - 1; i >= 0; i--) {
			Day9MemoryBlock fileMem = fileBlocks.get(i);

			for (int j = 0; j < freeBlocks.size(); j++) {
				Day9MemoryBlock freeMem = freeBlocks.get(j);

				if (fileMem.getSize() <= freeMem.getSize() && freeMem.getStartIndex() < fileMem.getStartIndex()) {

					for (int m = 0; m < fileMem.getSize(); m++) {
						diskCopy.set(freeMem.getStartIndex(), diskCopy.get(fileMem.getStartIndex() + m));

						diskCopy.set(fileMem.getStartIndex() + m, -1l);

						freeMem.moveIndex();
					}

					freeMem.updateMemorySize(fileMem);
					fileMem.setSize(0l);
				}
			}
		}

		return diskCopy;
	}

	private void extractBlocks(List<Day9MemoryBlock> fileBlocks, List<Day9MemoryBlock> freeBlocks) {
		int blockIndex = 0;
		for (int index = 0; index < this.blocks.size(); index++) {
			Long blockValue = Long.valueOf(this.blocks.get(index));

			var memoryBlock = new Day9MemoryBlock(blockIndex, blockValue);

			if (index % 2 == 0) {
				fileBlocks.add(memoryBlock);
			} else {
				freeBlocks.add(memoryBlock);
			}

			blockIndex += blockValue;
		}
	}

	private List<Long> defrag(List<Long> disk) {
		List<Long> diskCopy = disk.stream()
			.map((e) -> e)
			.collect(Collectors.toList());

		int freeIndex = 0;
		int fileIndex = diskCopy.size() - 1;

		while (freeIndex + 1 < fileIndex -1) {
			while (diskCopy.get(freeIndex) != -1)  {
				freeIndex++;
			}

			while (diskCopy.get(fileIndex) == -1)  {
				fileIndex--;
			}

			diskCopy.set(freeIndex, diskCopy.get(fileIndex));
			diskCopy.set(fileIndex, -1l);
		}

		return diskCopy;
	}

	private List<Long> createDisk() {
		List<Long> disk = new ArrayList<>();

		Long fileCounter = 0l;
		for (int i = 0; i < this.blocks.size(); i++) {
			Long block = Long.valueOf(this.blocks.get(i));

			if (i % 2 == 0) {

				for (int j = 0; j < block; j++) {
					disk.add(fileCounter);
				}

				fileCounter++;
			} else {
				for (int j = 0; j < block; j++) {
					disk.add(-1l);
				}
			}
		}

		return disk;
	}
}

class Day9MemoryBlock {
	private Integer startIndex;
	private Long size;

	public Day9MemoryBlock(Integer startIndex, Long size) {
		this.startIndex = startIndex;
		this.size = size;
	}

	public void updateMemorySize(Day9MemoryBlock fileMem) {
		this.size -= fileMem.getSize();
	}

	public void moveIndex() {
		this.startIndex++;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public String toString() {
		return "Day9MemoryBlock [startIndex=" + startIndex + ", size=" + size + "]";
	}

}