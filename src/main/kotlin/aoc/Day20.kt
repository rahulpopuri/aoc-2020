package aoc

import java.io.File
import java.nio.charset.Charset

fun main() {
    val input = parseTiles("input_day20.txt")
    val corners = getCorners(input)
    println("Part 1: " + corners.map { it.number.toLong() }.fold(1L) { acc, e -> acc * e })

    val seaMonster = "                  # \n" +
            "#    ##    ##    ###\n" +
            " #  #  #  #  #  #   "
}

private fun getCorners(tiles: List<Tile>): List<Tile> {
    val map = mutableMapOf<Int, Int>()
    for (tile in tiles) {
        for (edge in tile.edges) {
            map.merge(edge, 1, Int::plus)
        }
    }
    return tiles.filter { it.edgeMatchCount(map) > 2 }
}


private data class Tile(val number: Int, val image: Array<Array<Char>>) {
    var edges = mutableListOf<Int>()

    fun edgeMatchCount(input: Map<Int, Int>): Int {
        var result = 0
        for (edge in edges) {
            if (input[edge] == 1) {
                result++
            }
        }
        return result
    }

    private fun encode(input: Array<Char>) {
        var result = ""
        for (i in input.indices) {
            result += if (input[i] == '#') {
                "1"
            } else {
                "0"
            }
        }
        edges.add(Integer.parseInt(result, 2))
        edges.add(Integer.parseInt(result.reversed(), 2))
    }

    init {
        encode(image.first())
        encode(image.last())
        val left = CharArray(image.size)
        for (i in image.indices) {
            left[i] = image[i][0]
        }
        encode(left.toTypedArray())
        val right = CharArray(image.size)
        for (i in image[image.size - 1].indices) {
            right[i] = image[i][image.size - 1]
        }
        encode(right.toTypedArray())
    }
}

private fun parseTiles(inputFile: String): List<Tile> {
    val text = File(ClassLoader.getSystemResource(inputFile).file).readText(Charset.defaultCharset())
    val result = mutableListOf<Tile>()
    text.split("\n\n")
        .forEach { section ->
            run {
                val numRegex = "Tile ([0-9]+):".toRegex()
                var tileNum = 0
                val lines = section.split("\n")
                numRegex.matchEntire(lines[0])
                    ?.destructured
                    ?.let { (num) ->
                        tileNum = num.toInt()
                    }
                    ?: throw IllegalArgumentException("Bad input '$section'")

                val image = lines.drop(1)
                    .map { it.toCharArray().toTypedArray() }
                    .toTypedArray()
                result.add(Tile(tileNum, image))
            }
        }
    return result
}