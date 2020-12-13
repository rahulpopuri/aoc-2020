package aoc

fun main() {
    val input = toListOfString("input_day13.txt")
    val earliestTimestamp = input[0].toLong()

    val earliestBus = input[1].split(",")
        .filter { it != "x" }
        .map { it.toLong() }
        .associateBy({ it }, { earliestTimestamp + (it - earliestTimestamp % it) })
        .minByOrNull { it.value }
    if (earliestBus != null) {
        println("Part 1: " + earliestBus.key * (earliestBus.value - earliestTimestamp))
    }

    val buses = input[1].split(",").toTypedArray()
    val map = mutableMapOf<Long, Long>()
    for (i in buses.indices) {
        if (buses[i] != "x") {
            val j = buses[i].toLong()
            map[j] = i % j
        }
    }
    var min = 0L
    var product = 1L
    for (entry in map.entries) {
        while ((min + entry.value) % entry.key != 0L) {
            min += product
        }
        product *= entry.key
    }
    println("Part 2: $min")
}
