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

fun print2dArray(input: Array<Array<Char>>) {
    for (i in input.indices) {
        println(input[i].contentToString())
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