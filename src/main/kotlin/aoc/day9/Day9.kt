package aoc.day9

import aoc.toListOfLong

fun main() {
    val input = toListOfLong("input_day9.txt")
    println(firstInvalidNum(input, 25))
    println(contiguousSum(input, 393911906))
}

fun firstInvalidNum(input: List<Long>, preamble: Int): Long {
    for (i in preamble until input.size - 1) {
        val subList = input.subList(i - preamble, i)
        if (!twoSumExists(subList, input[i])) {
            return input[i]
        }
    }
    return -1
}

fun twoSumExists(a: List<Long>, target: Long): Boolean {
    for (i in a) {
        if (a.contains(target - i)) return true
    }
    return false
}

fun contiguousSum(a: List<Long>, target: Long): Long {
    val sum = LongArray(a.size + 1)
    sum[0] = 0
    for (i in 1 until a.size) {
        sum[i] = sum[i - 1] + a[i - 1]
    }
    for (start in 0 until a.size - 1) {
        for (end in start + 1 until a.size) {
            if (sum[end] - sum[start] == target) {
                val subList = a.subList(start + 1, end + 1).sorted()
                return subList[0] + subList[subList.size - 1]
            }
        }
    }
    return 0
}
