defmodule Day1 do
  def part_one do
    {col1, col2} = extract_columns()
    distances(Enum.sort(col1), Enum.sort(col2)) |> Enum.sum
  end

  def part_two do
    {col1, col2} = extract_columns()
    find_similarity(col1, col2)
  end

  defp read_input do
    {:ok, content} = File.read("../resources/day1.in")
    content
  end

  defp find_similarity(c1, c2) do
    find_similarity(c1, c2, [])
  end
  defp find_similarity([e1 | rest], c2, result) do
    ocurrences_number = count_ocurrence(e1, c2)
    find_similarity(rest, c2, [e1 * ocurrences_number] ++ result)
  end
  defp find_similarity([], _, result), do: Enum.sum(result)

  defp count_ocurrence(e1, [e2 | c2]) do
    init_value = if(e1 == e2, do: 1, else: 0)
    count_ocurrence(e1, c2, init_value)
  end

  defp count_ocurrence(e1, [e2 | c2], count) when is_number(e1) do
    new_count = if(e1 == e2, do: count + 1, else: count)
    count_ocurrence(e1, c2, new_count)
  end
  defp count_ocurrence(_, [], count) when is_number(count), do: count

  defp distances(c1, c2) when length(c1) == 0 and length(c2) == 0, do: []
  defp distances(c1, c2) when length(c1) > 0 and length(c2) > 0, do: distances(c1, c2, [])
  defp distances(c1, c2, result) when length(c1) == 0 and length(c2) == 0, do: result
  defp distances([e1 | c1], [e2 | c2], result),  do: distances(c1, c2, result ++ [abs(e1 - e2)])

  defp extract_columns, do: read_input() |> String.split(~r/\n/) |> extract_columns
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
IO.puts "Part 2: #{Day1.part_two()}"
