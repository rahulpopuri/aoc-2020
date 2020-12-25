package aoc

fun main() {
    val keys = listOf(14082811L, 5249543L)
    var value = 1L
    var loopCount = 0
    while (!keys.contains(value)) {
        value *= 7
        value %= 20201227
        loopCount++
    }
    val subject = keys.filter { it != value }[0]
    value = 1L
    for (i in 0 until loopCount) {
        value *= subject
        value %= 20201227
    }
    println("Part 1: $value")
}