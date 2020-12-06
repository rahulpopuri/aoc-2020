package aoc.day6

import java.io.File
import java.nio.charset.Charset

fun main() {
    val input = loadInput("input_day6.txt")
    println("Yes Count: " + input.map { it.getAnyYesCount() }.sum())
    println("All yes Count: " + input.map { it.getAllYesCount() }.sum())
}

data class Answer(val input: String) {
    private val yesMap: Map<Char, Int>

    init {
        yesMap = mutableMapOf()
        input.split("\n", "").forEach { line ->
            for (c in line.toCharArray()) {
                yesMap.merge(c, 1, Int::plus)
            }
        }
    }

    fun getAnyYesCount(): Int {
        return yesMap.keys.size
    }

    fun getAllYesCount(): Int {
        return yesMap.filterValues { value -> value == input.split("\n").size }.size
    }
}

fun loadInput(inputFile: String): List<Answer> {
    val text = File(ClassLoader.getSystemResource(inputFile).file).readText(Charset.defaultCharset())
    val result = mutableListOf<Answer>()
    text.split("\n\n").forEach { input ->
        result.add(Answer(input))
    }
    return result
}