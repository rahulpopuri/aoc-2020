package aoc.day7

import java.io.File
import java.nio.charset.Charset
import java.util.*

fun main() {
    val input = loadInput("input_day7.txt")
    val rules = input.map { parseRule(it) }.toMap()
    println(rules)
    println(containedBy(rules, "shiny gold"))
    println(contains(rules, "shiny gold") - 1)
}

fun parseRule(rule: String): Pair<String, List<Bag>> {
    val regex = "^([a-z ]+) bags contain ([a-z0-9, ]+).\$".toRegex()
    val bagRegex = "^(\\d+) ([a-z ]+) bag[s]?$".toRegex()
    regex.matchEntire(rule)
        ?.destructured
        ?.let { (color, rule) ->
            return if (rule == "no other bags") {
                Pair(color, listOf())
            } else {
                val bags = mutableListOf<Bag>()
                for (r in rule.split(",")) {
                    bagRegex.matchEntire(r.trim())
                        ?.destructured
                        ?.let { (quantity, color, _) ->
                            bags.add(Bag(color, quantity.toInt()))
                        }
                        ?: throw IllegalArgumentException("Bad bag input '$r'")
                }
                Pair(color, bags)
            }
        }
        ?: throw IllegalArgumentException("Bad input '$rule'")
}

fun containedBy(rules: Map<String, List<Bag>>, target: String): Int {
    val containedBy = mutableSetOf<String>()
    val queue: Queue<String> = LinkedList()
    val seen = mutableSetOf(target)
    queue.add(target)
    while (queue.isNotEmpty()) {
        val t = queue.remove()
        for (rule in rules) {
            if (rule.value.stream().anyMatch { bag -> bag.color == t }) {
                containedBy.add(rule.key)
                if (!seen.contains(rule.key)) {
                    seen.add(rule.key)
                    queue.add(rule.key)
                }
            }
        }
    }
    return containedBy.size
}

fun contains(rules: Map<String, List<Bag>>, target: String): Int {
    val bags = rules[target]
    var count = 1
    if (bags != null) {
        for (bag in bags) {
            count += bag.quantity * contains(rules, bag.color)
        }
    }
    return count
}

data class Bag(val color: String, val quantity: Int)

fun loadInput(inputFile: String): List<String> {
    return File(ClassLoader.getSystemResource(inputFile).file).readLines(Charset.defaultCharset())
}