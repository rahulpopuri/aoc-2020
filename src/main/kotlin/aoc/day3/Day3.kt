package aoc.day3

import java.io.File
import java.nio.charset.Charset

fun main() {
    val input = loadInput("input_day3.txt")
    val map = Map(input)
    // println(map.countTrees(3, 1))
    val slopes = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))
    println(slopes.map { map.countTrees(it.first, it.second) }
        .reduce(Long::times))
}

class Map(private val map: Array<Array<Char>>) {
    fun countTrees(slope_x: Int, slope_y: Int): Long {
        val numRows = map.size
        val numCols = map[0].size
        // Loop while y < numRows
        // If x >= numCols, reset x
        var numTrees = 0L
        var x = 0
        var y = 0
        while (y < numRows) {
            if (map[y][x % numCols] == '#') {
                numTrees++
            }
            y += slope_y
            x += slope_x
        }
        return numTrees
    }
}

fun loadInput(inputFile: String): Array<Array<Char>> {
    return File(ClassLoader.getSystemResource(inputFile).file).readLines(Charset.defaultCharset())
        .map { it.toCharArray().toTypedArray() }
        .toTypedArray()
}