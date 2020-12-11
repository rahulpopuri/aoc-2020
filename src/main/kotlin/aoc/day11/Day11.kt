package aoc.day11

import aoc.copy
import aoc.to2dCharArray

fun main() {
    val input = to2dCharArray("input_day11.txt")
    val seatMap = SeatMap(input)
    // Part 1
    println("Part 1: " + seatMap.transformPart1())

    // Part 2
    println("Part 2: " + seatMap.transformPart2())
}

class SeatMap(private val seats: Array<Array<Char>>) {
    private val directions = arrayOf(
        Pair(-1, -1),
        Pair(-1, 0),
        Pair(-1, 1),
        Pair(0, -1),
        Pair(0, 1),
        Pair(1, -1),
        Pair(1, 0),
        Pair(1, 1),
    )

    fun transformPart1(): Int {
        var result = transform(seats, canEmpty, canOccupy)
        while (result.first > 0) {
            result = transform(result.second, canEmpty, canOccupy)
        }
        return countOccupied(result.second)
    }

    fun transformPart2(): Int {
        var result = transform(seats, canEmpty2, canOccupy2)
        while (result.first > 0) {
            result = transform(result.second, canEmpty2, canOccupy2)
        }
        return countOccupied(result.second)
    }

    private fun transform(
        input: Array<Array<Char>>,
        canEmpty: (input: Array<Array<Char>>, i: Int, j: Int) -> Boolean,
        canOccupy: (input: Array<Array<Char>>, i: Int, j: Int) -> Boolean
    ): Pair<Int, Array<Array<Char>>> {
        val output = input.copy()
        var changes = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == 'L' && canOccupy(input, i, j)) {
                    output[i][j] = '#'
                    changes++
                } else if (input[i][j] == '#' && canEmpty(input, i, j)) {
                    output[i][j] = 'L'
                    changes++
                }
            }
        }
        return Pair(changes, output)
    }

    // 4 or more adjacent seats are occupied
    private var canEmpty = { input: Array<Array<Char>>, i: Int, j: Int ->
        getAdjacentSeats(input, i, j).count { it == '#' } >= 4
    }

    // No adjacent occupied seats
    private var canOccupy = { input: Array<Array<Char>>, i: Int, j: Int ->
        !getAdjacentSeats(input, i, j).contains('#')
    }

    // 5 or more visible seats are occupied
    private var canEmpty2 = { input: Array<Array<Char>>, i: Int, j: Int ->
        getVisibleSeats(input, i, j).count { it == '#' } >= 5
    }

    // No visible occupied seats
    private var canOccupy2 = { input: Array<Array<Char>>, i: Int, j: Int ->
        !getVisibleSeats(input, i, j).contains('#')
    }

    private var countOccupied = { input: Array<Array<Char>> ->
        input.map { i -> i.count { it == '#' } }.sum()
    }

    private fun getAdjacentSeats(input: Array<Array<Char>>, i: Int, j: Int): List<Char> {
        val result = mutableListOf<Char>()
        for (k in 0..7) {
            val x = i + directions[k].first
            val y = j + directions[k].second
            if (x in input.indices && y in input[i].indices) {
                result.add(input[x][y])
            }
        }
        return result
    }

    private fun getVisibleSeats(input: Array<Array<Char>>, i: Int, j: Int): List<Char> {
        val result = mutableListOf<Char>()
        for (k in 0..7) {
            var x = i + directions[k].first
            var y = j + directions[k].second
            while (x in input.indices && y in input[i].indices && input[x][y] == '.') {
                x += directions[k].first
                y += directions[k].second
            }
            if (x in input.indices && y in input[i].indices) {
                result.add(input[x][y])
            }
        }
        return result
    }
}


