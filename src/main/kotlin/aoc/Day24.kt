package aoc

fun main() {
    val input = parseDirectionData()
    // 0 = white, 1 = black. All tiles start at 0
    // Assume 100 x 100 grid

    val grid = Array(100) { Array(100) { 0 } }
    for (direction in input) {
        val offset = direction.calculateOffset()
        grid[offset.first][offset.second] = 1 - grid[offset.first][offset.second]
    }
    println("Part 1: " + grid.sumBy { it.sum() })

    var grid2 = grid.copyOf()
    for (i in 0 until 100) {
        grid2 = flipGrid(grid2)
        println(grid2.sumBy { it.sum() })
    }
    println("Part 2: " + grid2.sumBy { it.sum() })
}

private fun flipGrid(grid: Array<Array<Int>>): Array<Array<Int>> {
    val copy = grid.copyOf()
    var flipToWhite = 0
    var flipToBlack = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val blackTiles = countAdjacentBlackTiles(grid, Pair(i, j))
            if (grid[i][j] == 0 && blackTiles == 2) {
                copy[i][j] = 1
                flipToBlack++
            } else if (grid[i][j] == 1 && (blackTiles == 0 || blackTiles > 2)) {
                copy[i][j] = 0
                flipToWhite++
            }
        }
    }
   // println("White: $flipToWhite, Black: $flipToBlack")
    return copy
}

private fun countAdjacentBlackTiles(grid: Array<Array<Int>>, tile: Pair<Int, Int>): Int {
    var count = 0
    val x = tile.first
    val y = tile.second
    if (x + 1 < grid[0].size && grid[x + 1][y] == 1) count++
    if (x >= 1 && grid[x - 1][y] == 1) count++

    if (y % 2 == 0) {
        if (y + 1 < grid.size && grid[x][y + 1] == 1) count++
        if (x >= 1 && y + 1 < grid.size && grid[x - 1][y + 1] == 1) count++
        if (x >= 1 && y >= 1 && grid[x - 1][y - 1] == 1) count++
        if (y >= 1 && grid[x][y - 1] == 1) count++
    } else {
        if (x + 1 < grid[0].size && y + 1 < grid.size && grid[x + 1][y + 1] == 1) count++
        if (y + 1 < grid.size && grid[x][y + 1] == 1) count++
        if (y >= 1 && grid[x][y - 1] == 1) count++
        if (x + 1 < grid[0].size && y >= 1 && grid[x + 1][y - 1] == 1) count++
    }
    return count
}

private data class DirectionData(val hexDirections: List<HexDirection>) {
    fun calculateOffset(): Pair<Int, Int> {
        var x = 50
        var y = 50
        for (direction in hexDirections) {
            when (direction) {
                HexDirection.E -> x++
                HexDirection.SE -> {
                    // if row is odd, increment both, else increment y
                    if (y % 2 != 0) {
                        x++
                    }
                    y++
                }
                HexDirection.SW -> {
                    // if row is even, x--, y++ else y++
                    if (y % 2 == 0) {
                        x--
                    }
                    y++
                }
                HexDirection.W -> x--
                HexDirection.NW -> {
                    if (y % 2 == 0) {
                        x--
                    }
                    y--
                }
                HexDirection.NE -> {
                    if (y % 2 != 0) {
                        x++
                    }
                    y--
                }
            }
        }
        return Pair(x, y)
    }
}

private enum class HexDirection {
    E, SE, SW, W, NW, NE
}

private fun parseDirectionData(): List<DirectionData> {
    val input = toListOfString("input_day24.txt")
    val result = mutableListOf<DirectionData>()
    for (line in input) {
        val hexDirections = mutableListOf<HexDirection>()
        var i = 0
        while (i < line.length) {
            if (line[i] == 'e') {
                hexDirections.add(HexDirection.E)
            } else if (line[i] == 'w') {
                hexDirections.add(HexDirection.W)
            } else if (line[i] == 'n' && line[i + 1] == 'e') {
                hexDirections.add(HexDirection.NE)
                i++
            } else if (line[i] == 'n' && line[i + 1] == 'w') {
                hexDirections.add(HexDirection.NW)
                i++
            } else if (line[i] == 's' && line[i + 1] == 'e') {
                hexDirections.add(HexDirection.SE)
                i++
            } else if (line[i] == 's' && line[i + 1] == 'w') {
                hexDirections.add(HexDirection.SW)
                i++
            }
            i++
        }
        result.add(DirectionData(hexDirections))
    }
    return result
}