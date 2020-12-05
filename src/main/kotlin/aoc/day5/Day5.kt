package aoc.day5

import java.io.File
import java.io.InputStream

fun main() {
    val input = loadInput("input_day5.txt")
    println("Highest seat: " + input.map { it.getSeatId() }.maxOrNull())
    val seats = input.map { it.getSeatId() }.toSet()
    for (i in seats) {
        if (seats.contains(i + 2) && !seats.contains(i + 1)) {
            println("My seat: " + (i + 1))
            break
        }
    }
}

data class Seat(val input: String) {
    fun getSeatId(): Int {
        return binarySearchRow() * 8 + binarySearchCol()
    }

    private fun binarySearchRow(): Int {
        return binarySearch(127, input.toCharArray(endIndex = 7), 'F')
    }

    private fun binarySearchCol(): Int {
        return binarySearch(7, input.toCharArray(startIndex = 7), 'L')
    }

    private fun binarySearch(size: Int, input: CharArray, lowChar: Char): Int {
        var low = 0
        var high = size
        var mid: Int
        for (c in input) {
            mid = (high - low + 1) / 2
            if (c == lowChar) {
                high -= mid
            } else {
                low += mid
            }
        }
        return low
    }
}

fun loadInput(inputFile: String): List<Seat> {
    val inputStream: InputStream = File(ClassLoader.getSystemResource(inputFile).file).inputStream()
    val seatList = mutableListOf<Seat>()
    inputStream.bufferedReader().forEachLine { line -> seatList.add(Seat(line)) }
    return seatList
}