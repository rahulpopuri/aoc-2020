package aoc

import java.io.File
import java.io.InputStream
import java.nio.charset.Charset


fun to2dCharArray(inputFile: String): Array<Array<Char>> {
    return File(ClassLoader.getSystemResource(inputFile).file).readLines(Charset.defaultCharset())
        .map { it.toCharArray().toTypedArray() }
        .toTypedArray()
}

fun to2dCharList(inputFile: String): List<List<Char>> {
    return File(ClassLoader.getSystemResource(inputFile).file).readLines(Charset.defaultCharset())
        .map { line ->
            line.toCharArray().toList()
        }
}

fun toListOfString(inputFile: String): List<String> {
    return File(ClassLoader.getSystemResource(inputFile).file).readLines(Charset.defaultCharset())
}

fun toListOfLong(inputFile: String): List<Long> {
    val inputStream: InputStream = File(ClassLoader.getSystemResource(inputFile).file).inputStream()
    val result = mutableListOf<Long>()
    inputStream.bufferedReader().forEachLine { line ->
        run {
            result.add(line.toLong())
        }
    }
    return result
}

fun toIntArray(inputFile: String): IntArray {
    val inputStream: InputStream = File(ClassLoader.getSystemResource(inputFile).file).inputStream()
    val lineList = mutableListOf<Int>()
    inputStream.bufferedReader().forEachLine { line -> lineList.add(line.toInt()) }
    return lineList.toIntArray()
}

fun Array<Array<Int>>.print() {
    for (i in this.indices) {
        println(this[i].contentToString())
    }
    println()
}

fun Array<Array<Char>>.print() {
    for (i in this.indices) {
        println(this[i].contentToString())
    }
    println()
}

fun Array<Array<Char>>.copy() = Array(size) { get(it).clone() }

fun Array<Array<Char>>.flip(): Array<Array<Char>> {
    val result = this.copy()
    for (i in 0 until result.size / 2) {
        val tmp = result[result.size - i - 1]
        result[result.size - i - 1] = result[i]
        result[i] = tmp
    }
    return result
}

fun Array<Array<Char>>.rotate(): Array<Array<Char>> {
    val result = this.copy()
    val width = this.size
    for (i in result.indices) {
        for (j in result[i].indices) {
            result[i][j] = this[width - 1 - j][i]
        }
    }
    return result
}

fun MutableMap<Int, Set<String>>.prune() {
    while (this.values.any { it.size > 1 }) {
        for (allergen in this.entries) {
            if (allergen.value.size == 1) {
                // remove from other entries
                this.entries
                    .filter { it.key != allergen.key }
                    .filter { it.value.contains(allergen.value.elementAt(0)) }
                    .forEach { e ->
                        val set = HashSet(e.value)
                        set.remove(allergen.value.elementAt(0))
                        this[e.key] = set
                    }
            }
        }
    }
}

// Figure out how to combine with above
fun MutableMap<String, Set<String>>.prune2() {
    while (this.values.any { it.size > 1 }) {
        for (allergen in this.entries) {
            if (allergen.value.size == 1) {
                // remove from other entries
                this.entries
                    .filter { it.key != allergen.key }
                    .filter { it.value.contains(allergen.value.elementAt(0)) }
                    .forEach { e ->
                        val set = HashSet(e.value)
                        set.remove(allergen.value.elementAt(0))
                        this[e.key] = set
                    }
            }
        }
    }
}