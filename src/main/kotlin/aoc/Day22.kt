package aoc

import java.io.File
import java.nio.charset.Charset

fun main() {
    val game = parseInput()
    println("Part 1: " + game.play())
    println("Part 2: " + game.recursivePlay())
}

private data class Game(val player1: List<Int>, val player2: List<Int>) {
    fun play(): Long {
        val p1 = player1.toMutableList()
        val p2 = player2.toMutableList()
        while (p1.isNotEmpty() && p2.isNotEmpty()) {
            if (p1[0] > p2[0]) {
                p1.addAll(listOf(p1[0], p2[0]))
            } else {
                p2.addAll(listOf(p2[0], p1[0]))
            }
            p1.removeAt(0)
            p2.removeAt(0)
        }
        return if (p1.isEmpty()) {
            calculateScore(p2)
        } else {
            calculateScore(p1)
        }
    }

    fun recursivePlay(): Long {
        val result = recurse(player1, player2)
        return if (result.first.isEmpty()) {
            calculateScore(result.second)
        } else {
            calculateScore(result.first)
        }
    }

    private fun recurse(list1: List<Int>, list2: List<Int>): Pair<List<Int>, List<Int>> {
        val p1 = list1.toMutableList()
        val p2 = list2.toMutableList()
        val previousRounds = mutableSetOf<String>()
        while (p1.isNotEmpty() && p2.isNotEmpty()) {
            val encoded = encodeRound(p1, p2)
            if (previousRounds.contains(encoded)) {
                p2.clear()
                break
            }
            if (p1[0] <= p1.size - 1 && p2[0] <= p2.size - 1) {
                val recurseResult = recurse(p1.subList(1, p1[0] + 1), p2.subList(1, p2[0] + 1))
                if (recurseResult.second.isEmpty()) {
                    p1.addAll(listOf(p1[0], p2[0]))
                } else {
                    p2.addAll(listOf(p2[0], p1[0]))
                }
            } else {
                if (p1[0] > p2[0]) {
                    p1.addAll(listOf(p1[0], p2[0]))
                } else {
                    p2.addAll(listOf(p2[0], p1[0]))
                }
            }
            p1.removeAt(0)
            p2.removeAt(0)
            previousRounds.add(encoded)
        }
        return Pair(p1, p2)
    }

    private fun encodeRound(p1: List<Int>, p2: List<Int>): String {
        return p1.joinToString(",") { it.toString() }
            .plus(p2.joinToString(",") { it.toString() })
    }

    private fun calculateScore(nums: List<Int>): Long {
        var result = 0L
        for (i in nums.indices) {
            result += (i + 1) * nums[nums.size - i - 1]
        }
        return result
    }
}

private fun parseInput(): Game {
    val text = File(ClassLoader.getSystemResource("input_day22.txt").file).readText(Charset.defaultCharset())
    val decks = mutableListOf<List<Int>>()
    text.split("\n\n")
        .forEach { section ->
            val deck = section.split("\n")
                .filter { !it.startsWith("Player") }
                .map { it.toInt() }
            decks.add(deck)
        }
    return Game(decks[0], decks[1])
}