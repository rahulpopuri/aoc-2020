package aoc.day1

import java.io.File
import java.io.InputStream
import java.util.*

fun main() {
    val input = loadInput("input_day1.txt")
    println(twoSumMultiply(input, 2020))
    println(threeSumMultiply(input, 2020))
}

/**
 * Find 2 numbers that sum to "target" and multiply them
 */
fun twoSumMultiply(input: Array<Int>, target: Int): Int {
    val pair = twoSum(input, target)
    if (pair != null) {
        println("" + pair.first + " + " + pair.second + " = " + target)
        return pair.first * pair.second
    }
    return 0
}

fun twoSum(a: Array<Int>, target: Int): Pair<Int, Int>? {
    for (i in 0 until a.size - 2) {
        if (a[i] <= target) {
            for (j in i + 1 until a.size - 1) {
                val sum = a[i] + a[j]
                if (sum == target) return Pair(a[i], a[j])
                if (sum > target) break
            }
        } else {
            break
        }
    }
    return null
}

/**
 * Find 3 numbers that sum to "target" and multiply them
 */
fun threeSumMultiply(input: Array<Int>, target: Int): Int {
    val result = threeSum(input, target)
    if (result != null) {
        println("3Sum to " + target + ":" + Arrays.toString(result))
        return result.fold(1) { acc, e -> acc * e }
    }
    return 0
}

fun threeSum(a: Array<Int>, target: Int): IntArray? {
    var sum: Int
    for (i in 0 until a.size - 2) {
        var j = i + 1
        var k = a.size - 1
        while (j < k) {
            sum = a[i] + a[j] + a[k]
            when {
                sum == target -> {
                    return intArrayOf(a[i], a[j], a[k])
                }
                sum < target -> {
                    j++
                }
                else -> {
                    k--
                }
            }
        }
    }
    return null
}

fun loadInput(inputFile: String): Array<Int> {
    val inputStream: InputStream = File(ClassLoader.getSystemResource(inputFile).file).inputStream()
    val lineList = mutableListOf<Int>()
    inputStream.bufferedReader().forEachLine { line -> lineList.add(line.toInt()) }
    val result = lineList.toTypedArray()
    result.sort()
    return result
}