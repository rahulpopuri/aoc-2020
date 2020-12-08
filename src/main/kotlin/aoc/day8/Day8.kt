package aoc.day8

import java.io.File
import java.io.InputStream

fun main() {
    val input = loadInput("input_day8.txt")
    println(accumulate(input))
    println(fix(input))
}

fun fix(input: Array<Instruction>): Int {
    // Swap jmp <-> nop and check if the returned offset == input.length
    for ((index, instruction) in input.withIndex()) {
        val oldInstruction = instruction.copy()
        if (instruction.command == "jmp") {
            input[index] = Instruction("nop", instruction.value)
        } else if (instruction.command == "nop") {
            input[index] = Instruction("jmp", instruction.value)
        } else {
            continue
        }
        val result = accumulate(input)
        if (result.first == input.size) {
            println("Swapped instruction $index")
            return result.second
        } else {
            input[index] = oldInstruction
        }
    }
    return -1
}

/**
 * Return value of acc until an infinite loop
 */
fun accumulate(input: Array<Instruction>): Pair<Int, Int> {
    val seen = mutableSetOf<Int>()
    var acc = 0
    var offset = 0
    while (!seen.contains(offset) && offset < input.size) {
        seen.add(offset)
        val instruction = input[offset]
        when (instruction.command) {
            "acc" -> {
                acc += instruction.value
                offset++
            }
            "jmp" -> offset += instruction.value
            "nop" -> offset++
        }
    }
    return Pair(offset, acc)
}

data class Instruction(val command: String, val value: Int)

fun loadInput(inputFile: String): Array<Instruction> {
    val inputStream: InputStream = File(ClassLoader.getSystemResource(inputFile).file).inputStream()
    val instructions = mutableListOf<Instruction>()
    inputStream.bufferedReader().forEachLine { line ->
        run {
            val split = line.split(" ")
            instructions.add(Instruction(split[0], split[1].replace("+", "").toInt()))
        }
    }
    return instructions.toTypedArray()
}