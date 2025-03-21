defmodule Day1 do
  def part_one() do
    {col1, col2} = read_input() |> String.split(~r/\n/) |> extract_columns
    distances(Enum.sort(col1), Enum.sort(col2)) |> Enum.sum
  end

  defp read_input do
    {:ok, content} = File.read("../resources/day1.in")
    content
  end

  defp distances(c1, c2) when length(c1) == 0 and length(c2) == 0, do: []
  defp distances(c1, c2) when length(c1) > 0 and length(c2) > 0, do: distances(c1, c2, [])
  defp distances(c1, c2, result) when length(c1) == 0 and length(c2) == 0, do: result
  defp distances([e1 | c1], [e2 | c2], result),  do: distances(c1, c2, result ++ [abs(e1 - e2)])

  defp extract_columns(lines) when length(lines) == 0, do: {}
  defp extract_columns(lines) when length(lines) > 0, do: extract_columns(lines, {[], []})
  defp extract_columns(lines, return) when length(lines) == 0, do: return
  defp extract_columns(lines = [first_line | other_lines], { column1, column2 }) when length(lines) > 0 do
    [c1, c2] = first_line |> String.split(~r/[^\d]+/)
    extract_columns(
      other_lines,
      { [String.to_integer(c1)] ++ column1, [String.to_integer(c2)] ++ column2 }
    )
  end
end

IO.puts "Part 1: #{Day1.part_one()}"
