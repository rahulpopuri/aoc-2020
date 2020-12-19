package aoc

import java.io.File
import java.nio.charset.Charset

fun main() {
    val input = parseRules("input_day19.txt")
    println("Part 1: " + input.parse())

    // Note: For Part 2, I just changed the input to add more rules to deal with loops
    val input2 = parseRules("input_day19_2.txt")
    println("Part 2: " + input2.parse())
}

private data class Rules(val ruleInput: Array<String>, val input: List<String>) {
    private var rules: Array<String> = Array(ruleInput.size) { "" }

    init {
        for (i in ruleInput.indices) {
            parse(i)
        }
    }

    private fun parse(i: Int) {
        if (rules[i] != "") {
            return
        }
        when {
            ruleInput[i].contains("\"") -> {
                val char = ruleInput[i].replace("\"", "")[0]
                rules[i] = "[$char]"
            }
            ruleInput[i].contains("|") -> {
                val ors = ruleInput[i].split("|")
                val parts = mutableListOf<String>()
                for (or in ors) {
                    or.split(" ").filter { it.isNotEmpty() }.forEach { parse(it.toInt()) }
                    parts.add(or.split(" ")
                        .filter { it.isNotEmpty() }
                        .joinToString("") { j -> rules[j.toInt()] })
                }
                val regex = parts.joinToString("|")
                rules[i] = "($regex)"
            }
            else -> {
                ruleInput[i].split(" ")
                    .filter { it.isNotEmpty() }
                    .forEach { parse(it.toInt()) }
                rules[i] = ruleInput[i].split(" ")
                    .filter { it.isNotEmpty() }
                    .joinToString("") { j -> rules[j.toInt()] }
            }
        }
    }

    fun parse(): Int {
        // println(rules.contentToString())
        return input.count { rules[0].toRegex().matches(it) }
    }
}

private fun parseRules(inputFile: String): Rules {
    val text = File(ClassLoader.getSystemResource(inputFile).file).readText(Charset.defaultCharset())
    val ruleRegex = "^([0-9]+): ([a-z0-9 |\"]+)".toRegex()
    val sections = text.split("\n\n")

    val input = sections[1].split("\n")
    val rulesMap = Array(1000) { "" }
    for (row in sections[0].split("\n")) {
        ruleRegex.matchEntire(row)
            ?.destructured
            ?.let { (i, expression) ->
                rulesMap[i.toInt()] = expression
            }
            ?: throw IllegalArgumentException("Bad input '$row'")
    }

    return Rules(rulesMap, input)
}