package aoc

fun main() {
    val input = arrayOf(6, 13, 1, 15, 2, 0)
    println("Part 1: " + play(input, 2020))
    println("Part 2: " + play(input, 30000000))
}

fun play(input: Array<Int>, iterations: Int): Int {
    val state = mutableMapOf<Int, List<Int>>()

    for (i in input.indices) {
        state[input[i]] = mutableListOf(i)
    }

    var last = input[input.size - 1]
    for (i in input.size until iterations) {
        val previous = state[last]
        if (previous != null) {
            val turn: Int = if (previous.size == 1) 0
            else (previous[0] - previous[1])
            last = turn
            state.merge(turn, listOf(i)) { a, b -> b.plus(a[0]) }
        }
    }
    return last
}