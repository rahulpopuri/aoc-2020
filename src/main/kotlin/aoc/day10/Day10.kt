package aoc.day10

import aoc.toListOfLong

fun main() {
    val input = toListOfLong("input_day10.txt").sorted()
    val differences = joltDifferences(input)
    println(differences[3]?.let { differences[1]?.times(it) })
    println(numCombinations(input))
}

fun joltDifferences(input: List<Long>): Map<Long, Long> {
    val result = mutableMapOf<Long, Long>()
    result.merge(input[0], 1, Long::plus)
    for (i in 1 until input.size) {
        result.merge(input[i] - input[i - 1], 1, Long::plus)
    }
    result.merge(3, 1, Long::plus)
    return result
}

fun numCombinations(input: List<Long>): Long {
    val nums = input.toMutableList()
    nums.add(0, 0)
    val dp = LongArray(nums.size) { 0L }
    dp[nums.size - 1] = 1
    for (i in nums.size - 2 downTo 0) {
        dp[i] = dp[i + 1]
        if (i < nums.size - 3 && nums[i + 3] <= nums[i] + 3) {
            dp[i] += dp[i + 3]
        }
        if (i < nums.size - 2 && nums[i + 2] <= nums[i] + 3) {
            dp[i] += dp[i + 2]
        }
    }
    return dp[0]
}