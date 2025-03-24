defmodule Day7 do
  def part_one do
    entries()
    |> Stream.map(fn row -> process_row(row, ["+", "*"]) end)
    |> Enum.sum
  end

  def part_two do
    entries()
    |> Stream.map(fn row -> process_row(row, ["+", "*", "c"]) end)
    |> Enum.sum
  end

  defp entries do
    read_input()
    |> String.split(~r/\n/)
    |> Enum.filter(fn x -> String.length(x) > 0 end)
    |> extract_values
  end

  defp process_row([target | rest], operations) do
    numbers = hd(rest)
    all_ops = possibilities(operations, length(numbers) - 1)
    reduce_ops(numbers, all_ops, target, 0)
  end

  defp reduce_ops(_, _, target, current_result) when target == current_result, do: target
  defp reduce_ops(_, [], target, current_result) when target != current_result, do: 0
  defp reduce_ops(numbers, [current_ops | all_ops], target, _) do
    result = apply_ops(numbers, current_ops, target)
    reduce_ops(numbers, all_ops, target, result)
  end

  defp apply_ops([first | rest], ops, target) do
    apply_ops(rest, ops, first, target)
  end
  defp apply_ops(_, [], result, _), do: result
  defp apply_ops(_, _, result, target) when result > target, do: -1
  defp apply_ops([first | numbers], [op | ops], result, target) do
    apply_ops(numbers, ops, possible_ops(op).([result, first]), target)
  end

  defp possible_ops("+"), do: &Enum.sum/1
  defp possible_ops("*"), do: fn numbers -> Enum.reduce(numbers, fn n, acc -> acc * n end) end
  defp possible_ops("c") do
    fn numbers -> Enum.reduce(numbers, fn n, acc -> append_number(acc, n) end) end
  end

  def append_number(acc, n) do
    acc * :math.pow(10, ndigits(n)) + n
  end

  defp ndigits(x), do: trunc(:math.floor(:math.log(x)/:math.log(10))+1)

  defp possibilities(operations, 1), do: Enum.map(operations, fn x -> [x] end)
  defp possibilities(operations, op_slot) when op_slot > 1 do
    append_op(operations, operations, op_slot)
  end

  defp append_op(_, items, 1) do
    items
    |> Enum.map(fn item -> String.split(item, "") end)
    |> Enum.map(fn line -> Enum.filter(line, fn x -> String.length(x) > 0 end) end)
  end
  defp append_op(operations, items, count) do
    new_items = Enum.flat_map(operations, fn op -> Enum.map(items, fn item -> item <> op end) end)
    append_op(operations, new_items, count - 1)
  end

  defp extract_values([line | lines]) do
    extract_values(lines, [extract_value(line)])
  end
  defp extract_values([], result), do: result
  defp extract_values(lines = [line | rest], result) when length(lines) > 0 do
    extract_values(rest, [extract_value(line)] ++ result)
  end
  defp extract_value(line) do
    [target, numbers] = line |> String.split(":")

    entries = numbers
            |> String.split(" ")
            |> Enum.filter(fn x -> String.length(x) > 0 end)
            |> Enum.map(&String.to_integer/1)

    [ String.to_integer(target), entries ]
  end

  defp read_input do
    {:ok, content} = File.read("../resources/day7.in")
    content
  end
end

IO.puts "Part 1: #{Day7.part_one()}"
IO.puts "Part 2: #{Day7.part_two()}"
