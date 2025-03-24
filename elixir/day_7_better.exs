# This is the best solution I found for this problem.
# It basically ignores all symbols and test every possible combination.
#
# Got from here https://elixirforum.com/t/advent-of-code-2024-day-7/67938/2

defmodule Day7 do
  defp produced?(test_value, [x | numbers], part), do: produced?(test_value, numbers, x, part)
  defp produced?(test_value, [], value, _part), do: value == test_value
  defp produced?(test_value, _numbers, value, _part) when value > test_value, do: false
  defp produced?(test_value, [x | numbers], value, :part1) do
    produced?(test_value, numbers, value * x, :part1) ||
    produced?(test_value, numbers, value + x, :part1)
  end
  defp produced?(test_value, [x | numbers], value, :part2) do
    produced?(test_value, numbers, value * x, :part2) ||
    produced?(test_value, numbers, value + x, :part2) ||
    produced?(test_value, numbers, value * 10 ** ndigits(x) + x, :part2)
  end

  defp ndigits(x), do: trunc(:math.floor(:math.log(x)/:math.log(10))+1)

  defp get_lines_stream do
    File.stream!("../resources/day7.in")
    |> Stream.map(fn line -> Regex.scan(~r/\d+/, line) |> Enum.map(fn [x] -> String.to_integer(x) end) end)
  end

  defp solve(part) do
    get_lines_stream()
    |> Enum.reduce(0, fn [test_value | numbers], sum ->
      if produced?(test_value, numbers, part), do: sum+test_value, else: sum
    end)
  end

  def part_1 do
    solve(:part1)
  end

  def part_2 do
    solve(:part2)
  end
end


IO.puts "Part 1: #{Day7.part_1}"
IO.puts "Part 2: #{Day7.part_2}"
