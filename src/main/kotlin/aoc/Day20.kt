package aoc

import java.io.File
import java.lang.Exception
import java.nio.charset.Charset
import kotlin.math.sqrt

fun main() {
    val input = parseTiles("input_day20.txt").toMutableList()
    val edgeCountMap = buildEdgeCountMap(input)
    val corners = input.filter { it.isCorner(edgeCountMap) }
    println("Part 1: " + corners.map { it.number.toLong() }.fold(1L) { acc, e -> acc * e })

    val monster = listOf(
        "                  # ",
        "#    ##    ##    ###",
        " #  #  #  #  #  #   "
    )

    val sqrt = sqrt(input.size.toDouble()).toInt()
    val tileGrid = Array(sqrt) { arrayOfNulls<Tile>(sqrt) }

    // Place first corner at 0,0
    for (tile in input) {
        if (!tile.isCorner(edgeCountMap)) {
            continue
        }
        while (edgeCountMap[tile.getEdgeVal(Tile.EdgeType.FIRST)] != 1) {
            tile.rotate()
        }
        if (edgeCountMap[tile.getEdgeVal(Tile.EdgeType.LEFT)] != 1) {
            // This is hardcoded - need to be smarter about how to get FIRST, LEFT counts = 1
            tile.flip()
            tile.rotate()
            tile.flip()
        }
        tileGrid[0][0] = tile
        input.remove(tile)
        break
    }

    // Place first row
    for (y in 1 until sqrt) {
        val target =
            tileGrid[0][y - 1]?.getEdgeVal(Tile.EdgeType.RIGHT) ?: throw Exception("Couldn't find target at 0 $y")
        for (tile in input) {
            if (tile.edges.contains(target)) {
                for (type in Tile.EdgeType.values()) {
                    if (tile.getEdgeVal(Tile.EdgeType.LEFT) == target) {
                        break
                    }
                    tile.rotate()
                }
                if (tile.getEdgeVal(Tile.EdgeType.LEFT) != target) {
                    tile.flip()
                }
                for (type in Tile.EdgeType.values()) {
                    if (tile.getEdgeVal(Tile.EdgeType.LEFT) == target) {
                        break
                    }
                    tile.rotate()
                }
                if (tile.getEdgeVal(Tile.EdgeType.LEFT) != target) {
                    throw Exception("Couldn't match ${tile.number}")
                }
                tileGrid[0][y] = tile
                input.remove(tile)
                break
            }
        }
    }
    // Place remaining
    for (y in 0 until sqrt) {
        for (x in 1 until sqrt) {
            val target = tileGrid[x - 1][y]?.getEdgeVal(Tile.EdgeType.LAST)
                ?: throw Exception("Couldn't find target at ${x - 1} $y")
            for (tile in input) {
                if (tile.edges.contains(target)) {
                    for (type in Tile.EdgeType.values()) {
                        if (tile.getEdgeVal(Tile.EdgeType.FIRST) == target) {
                            break
                        }
                        tile.rotate()
                    }
                    if (tile.getEdgeVal(Tile.EdgeType.FIRST) != target) {
                        tile.flip()
                    }
                    for (type in Tile.EdgeType.values()) {
                        if (tile.getEdgeVal(Tile.EdgeType.FIRST) == target) {
                            break
                        }
                        tile.rotate()
                    }
                    if (tile.getEdgeVal(Tile.EdgeType.FIRST) != target) {
                        throw Exception("Couldn't match ${tile.number}")
                    }
                    tileGrid[x][y] = tile
                    input.remove(tile)
                    break
                }
            }
        }
    }
    var trimmed = trimBorders(tileGrid)

    val usedGrid = Array(trimmed.size) { Array(trimmed.size) { '-' } }
    var count = 0
    for (type in Tile.EdgeType.values()) {
        val v = findMonster(trimmed, monster, usedGrid)
        val gm = gm(trimmed, usedGrid)
        if (v > 0) {
            count = gm
        }
        trimmed = trimmed.rotate()
    }
    trimmed = trimmed.flip()
    for (type in Tile.EdgeType.values()) {
        val v = findMonster(trimmed, monster, usedGrid)
        val gm = gm(trimmed, usedGrid)
        if (v > 0) {
            count = gm
        }
        trimmed = trimmed.rotate()
    }

    println("Part 2: $count")
}

private fun findMonster(grid: Array<Array<Char>>, monster: List<String>, usedGrid: Array<Array<Char>>): Int {
    var count = 0
    for (x in 0 until grid.size - (monster.size - 1)) {
        for (y in 0 until grid[x].size - (monster[0].length - 1)) {
            var giveUp = false
            for (mx in monster.indices) {
                if (giveUp) {
                    break
                }
                for (my in monster[mx].indices) {
                    if (monster[mx][my] == '#') {
                        if (grid[x + mx][y + my] == '.') {
                            giveUp = true
                            break
                        }
                    }
                }
            }
            if (giveUp) {
                continue
            }
            count++
            for (mx in monster.indices) {
                for (my in monster[mx].indices) {
                    if (monster[mx][my] == '#') {
                        usedGrid[x + mx][y + my] = '!'
                    }

                }
            }
        }
    }
    return count
}

private fun gm(grid: Array<Array<Char>>, usedGrid: Array<Array<Char>>): Int {
    var result = 0
    for (y in grid.indices) {
        for (x in grid.indices) {
            if (grid[x][y] == '#' && usedGrid[x][y] != '!') {
                result++
            }
        }
    }
    return result
}

private fun trimBorders(grid: Array<Array<Tile?>>): Array<Array<Char>> {
    val newWidth = grid.size * (10 - 2)
    val result = Array(newWidth) { Array(newWidth) { '-' } }
    for (tx in grid.indices) {
        for (ty in grid.indices) {
            for (x in 0 until 8) {
                for (y in 0 until 8) {
                    result[tx * 8 + x][ty * 8 + y] =
                        grid[tx][ty]?.image?.get(x + 1)?.get(y + 1) ?: '-'
                }
            }
        }
    }
    return result
}

private fun printGrid(grid: Array<Array<Tile?>>) {
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == null) {
                print(" - ")
            } else {
                print(" ${grid[i][j]!!.number} ")
            }
        }
        println()
    }
    println()
}

private fun getRawGrid(grid: Array<Array<Tile?>>): Array<Array<Char>> {
    val raw = Array(10 * grid.size) { Array(10 * grid.size) { '-' } }
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val image = grid[i][j]?.image ?: Array(10) { Array(10) { '-' } }
            for (k in image.indices) {
                for (l in image[k].indices) {
                    raw[10 * i + k][10 * j + l] = image[k][l]
                }
            }
        }
    }
    return raw
}

private fun buildEdgeCountMap(tiles: List<Tile>): Map<Int, Int> {
    val edgeCountMap = mutableMapOf<Int, Int>()
    for (tile in tiles) {
        for (edge in tile.edges) {
            edgeCountMap.merge(edge, 1, Int::plus)
        }
    }
    return edgeCountMap
}

private data class Tile(val number: Int, var image: Array<Array<Char>>) {
    var edges = mutableListOf<Int>()

    enum class EdgeType {
        FIRST, LAST, LEFT, RIGHT
    }

    fun isCorner(map: Map<Int, Int>): Boolean {
        var count = 0
        for (i in edges) {
            if (map[i] == 1) {
                count++
            }
        }
        return count > 2
    }

    fun rotate() {
        image = image.rotate()
    }

    fun flip() {
        image = image.flip()
    }

    fun getEdgeVal(type: EdgeType): Int {
        return encode(getEdge(type))[0]
    }

    fun getEdge(type: EdgeType): Array<Char> {
        return when (type) {
            EdgeType.FIRST -> image.first()
            EdgeType.LAST -> image.last()
            EdgeType.LEFT -> {
                val left = CharArray(image.size)
                for (i in image.indices) {
                    left[i] = image[i][0]
                }
                return left.toTypedArray()
            }
            EdgeType.RIGHT -> {
                val right = CharArray(image.size)
                for (i in image[image.size - 1].indices) {
                    right[i] = image[i][image.size - 1]
                }
                return right.toTypedArray()
            }
        }
    }

    private fun encode(input: Array<Char>): List<Int> {
        var result = ""
        for (i in input.indices) {
            result += if (input[i] == '#') {
                "1"
            } else {
                "0"
            }
        }
        return listOf(Integer.parseInt(result, 2), Integer.parseInt(result.reversed(), 2))
    }

    init {
        for (edge in EdgeType.values()) {
            edges.addAll(encode(getEdge(edge)))
        }
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