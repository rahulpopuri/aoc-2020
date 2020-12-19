package aoc

import java.io.File
import java.nio.charset.Charset

fun main() {
    val input = loadTicketData("input_day16.txt")
    input.process()

    println("Part 1: " + input.sumInvalidNearbyTickets())
    println("Part 2: " + input.multiplyDepartureTicketValues())
}

private data class TicketData(private val rules: List<Rule>, val yourTickets: Array<Int>, val nearbyTickets: List<Array<Int>>) {
    private val validNearbyTickets = mutableListOf<Array<Int>>()
    private var invalidNearbyTicketsSum = 0

    fun process() {
        for (tickets in nearbyTickets) {
            var isValid = true
            for (ticket in tickets) {
                if (rules.none { it.contains(ticket) }) {
                    invalidNearbyTicketsSum += ticket
                    isValid = false
                }
            }
            if (isValid) {
                validNearbyTickets.add(tickets)
            }
        }
    }

    fun sumInvalidNearbyTickets(): Int {
        return invalidNearbyTicketsSum
    }

    fun multiplyDepartureTicketValues(): Long {
        val ruleMap = mutableMapOf<Int, Set<String>>()
        for (i in rules.indices) {
            ruleMap[i] = rules.map { it.name }.toSet()
        }
        for (tickets in validNearbyTickets) {
            for (t in tickets.indices) {
                rules.filter { !it.contains(tickets[t]) }
                    .forEach { rule ->
                        val validRules = HashSet(ruleMap.getOrDefault(t, setOf()))
                        validRules.remove(rule.name)
                        ruleMap[t] = validRules
                    }
            }
        }
        while (ruleMap.values.any { it.size > 1 }) {
            for (rule in ruleMap.entries) {
                if (rule.value.size == 1) {
                    // remove from other entries
                    ruleMap.entries
                        .filter { it.key != rule.key }
                        .filter { it.value.contains(rule.value.elementAt(0)) }
                        .forEach { e ->
                            val validRules = HashSet(e.value)
                            validRules.remove(rule.value.elementAt(0))
                            ruleMap[e.key] = validRules
                        }
                }
            }
        }
        val positions = ruleMap.filterValues { it -> it.any { it.startsWith("departure") } }.keys
        var result = 1L
        for (t in yourTickets.indices) {
            if (positions.contains(t)) {
                result *= yourTickets[t].toLong()
            }
        }
        return result
    }
}

private data class Rule(val name: String) {
    private val ranges = mutableListOf<Pair<Int, Int>>()

    fun addRange(input: Pair<Int, Int>) {
        ranges.add(input)
    }

    fun contains(num: Int): Boolean {
        for (range in ranges) {
            if (num >= range.first && num <= range.second) {
                return true
            }
        }
        return false
    }
}

private fun loadTicketData(inputFile: String): TicketData {
    val text = File(ClassLoader.getSystemResource(inputFile).file).readText(Charset.defaultCharset())
    val rules = mutableListOf<Rule>()
    val sections = text.split("\n\n")
    val ruleRegex = "^([a-z ]+): ([0-9]+)-([0-9]+) or ([0-9]+)-([0-9]+)".toRegex()
    for (row in sections[0].split("\n")) {
        ruleRegex.matchEntire(row)
            ?.destructured
            ?.let { (name, r1, r2, r3, r4) ->
                val rule = Rule(name)
                rule.addRange(Pair(r1.toInt(), r2.toInt()))
                rule.addRange(Pair(r3.toInt(), r4.toInt()))
                rules.add(rule)
            }
            ?: throw IllegalArgumentException("Bad input '$row'")
    }

    val yourTickets = sections[1].split("\n")[1].split(",").map { it.toInt() }.toTypedArray()

    val nearbyTickets = mutableListOf<Array<Int>>()

    sections[2].split("\n")
        .filter { !it.startsWith("nearby") }
        .forEach { s ->
            run {
                nearbyTickets.add(s.split(",").map { it.toInt() }.toTypedArray())
            }
        }

    return TicketData(rules, yourTickets, nearbyTickets)
}