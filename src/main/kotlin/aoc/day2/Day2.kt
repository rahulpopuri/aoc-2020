package aoc.day2

import java.io.File
import java.io.InputStream

fun main() {
    val input = loadInput("input_day2.txt")
    println(input.count { it.isValid() })
    println(input.count { it.isValidV2() })
}

data class Expression(val min: Int, val max: Int, val character: Char, val password: String) {
    fun isValid(): Boolean {
        return password.count { it == character } in min..max
    }

    fun isValidV2(): Boolean {
        val chars = password.toCharArray()
        return (chars[min - 1] == character) xor (chars[max - 1] == character)
    }
}

fun loadInput(inputFile: String): List<Expression> {
    val inputStream: InputStream = File(ClassLoader.getSystemResource(inputFile).file).inputStream()
    val expressions = mutableListOf<Expression>()
    inputStream.bufferedReader().forEachLine { line ->
        run {
            val regex = "(\\d+)-(\\d+) ([a-z]): (\\w+)".toRegex()
            val expression = regex.matchEntire(line)
                ?.destructured
                ?.let { (min, max, character, password) ->
                    Expression(min.toInt(), max.toInt(), character.elementAt(0), password)
                }
                ?: throw IllegalArgumentException("Bad input '$line'")
            expressions.add(expression)
        }
    }
    return expressions
}