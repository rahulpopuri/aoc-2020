package aoc

import java.io.File
import java.io.InputStream
import kotlin.math.pow

fun main() {
    val input = loadInput("input_day14.txt")
    var memory = mapOf<Long, Long>()
    for (instruction in input) {
        memory = instruction.apply(memory)
    }
    println("Part 1: " + memory.values.sum())

    memory = mapOf()
    for (instruction in input) {
        memory = instruction.applyV2(memory)
    }

    println("Part 2: " + memory.values.sum())
}

data class InstructionSet(val mask: String) {
    private val instructions = mutableListOf<Pair<Int, Int>>()

    fun addInstruction(input: Pair<Int, Int>) {
        instructions.add(input)
    }

    fun apply(memory: Map<Long, Long>): Map<Long, Long> {
        val output = HashMap(memory)
        for (instruction in instructions) {
            val binary = toBinary(instruction.second)
            val result = mask(binary)
            output[instruction.first.toLong()] = toDecimal(result)
        }
        return output
    }

    fun applyV2(memory: Map<Long, Long>): Map<Long, Long> {
        val output = HashMap(memory)
        for (instruction in instructions) {
            val binary = toBinary(instruction.first)
            val result = maskV2(binary)
            val addresses = computeAddresses(result, setOf())
            for (address in addresses) {
                output[address] = instruction.second.toLong()
            }
        }
        return output
    }

    private fun mask(input: String): String {
        val result = CharArray(input.length)
        for (i in input.toCharArray().indices) {
            if (mask[i] == 'X') {
                result[i] = input[i]
            } else {
                result[i] = mask[i]
            }
        }
        return result.joinToString("")
    }

    private fun maskV2(input: String): CharArray {
        val result = CharArray(input.length)
        for (i in input.toCharArray().indices) {
            if (mask[i] == '0') {
                result[i] = input[i]
            } else {
                result[i] = mask[i]
            }
        }
        return result
    }

    private fun computeAddresses(input: CharArray, values: Set<Long>): Set<Long> {
        val firstFloating = input.indexOfFirst { it == 'X' }
        if (firstFloating == -1) {
            val r = HashSet(values)
            r.add(toDecimal(input.joinToString("")))
            return r
        }
        val result = HashSet(values)
        val copy = input.copyOf()
        input[firstFloating] = '0'
        result.addAll(computeAddresses(input, result))
        copy[firstFloating] = '1'
        result.addAll(computeAddresses(copy, result))
        return result
    }

    private fun toBinary(num: Int): String {
        return Integer.toBinaryString(num)
            .padStart(36, '0')
    }

    private fun toDecimal(input: String): Long {
        var sum = 0L
        input.reversed().forEachIndexed { k, v ->
            sum += (v.toString().toInt() * 2.0.pow(k.toDouble())).toLong()
        }
        return sum
    }
}

fun loadInput(inputFile: String): List<InstructionSet> {
    val inputStream: InputStream = File(ClassLoader.getSystemResource(inputFile).file).inputStream()
    val instructions = mutableListOf<InstructionSet>()
    var currentInstruction = InstructionSet("")
    for (line in inputStream.bufferedReader().readLines()) {
        if (line.startsWith("mask")) {
            if (currentInstruction.mask.isNotBlank()) {
                instructions.add(currentInstruction)
            }
            val mask = line.split(" ")[2]
            currentInstruction = InstructionSet(mask)
        } else {
            val key = line.split(" ")[0]
                .replace("mem[", "")
                .replace("]", "")
                .toInt()
            val value = line.split(" ")[2].toInt()
            currentInstruction.addInstruction(Pair(key, value))
        }
    }
    instructions.add(currentInstruction)
    return instructions
}