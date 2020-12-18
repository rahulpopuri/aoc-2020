package aoc

import java.util.*

fun main() {
    val input = toListOfString("input_day18.txt")
    println("Part 1: " + input.sumOf { part1(it) })
    println("Part 2: " + input.sumOf { part2(it) })
}

fun part1(line: String): Long {
    val input = line.replace(" ", "")
    val numStack = ArrayDeque<Long>()
    val opStack = ArrayDeque<Char>()
    var i = 0
    while (i < input.length) {
        val c = input[i]
        if (c.isDigit()) {
            var num = 0L
            while (i < input.length && input[i].isDigit()) {
                num = num * 10 + Character.getNumericValue(input[i])
                i++
            }
            numStack.push(num)
        } else {
            if (opStack.isEmpty()) {
                opStack.push(c)
                i++
            } else if (c == '+' || c == '*') {
                if (opStack.peek() == '(') {
                    opStack.push(c)
                    i++
                } else {
                    evaluate(numStack, opStack)
                }
            } else if (c == '(') {
                opStack.push(c)
                i++
            } else if (c == ')') {
                while (opStack.peek() != '(') {
                    evaluate(numStack, opStack)
                }
                opStack.pop()
                i++
            }
        }
    }
    while (!opStack.isEmpty()) {
        evaluate(numStack, opStack)
    }
    return numStack.peek()
}

fun part2(line: String): Long {
    val input = line.replace(" ", "")
    val numStack = ArrayDeque<Long>()
    val opStack = ArrayDeque<Char>()
    var i = 0
    while (i < input.length) {
        val c = input[i]
        if (c.isDigit()) {
            var num = 0L
            while (i < input.length && input[i].isDigit()) {
                num = num * 10 + Character.getNumericValue(input[i])
                i++
            }
            numStack.push(num)
        } else {
            if (opStack.isEmpty()) {
                opStack.push(c)
                i++
            } else if (c == '*') {
                if (opStack.peek() == '(') {
                    opStack.push(c)
                    i++
                } else {
                    evaluate(numStack, opStack)
                }
            } else if (c == '+') {
                when (opStack.peek()) {
                    '(' -> {
                        opStack.push(c)
                        i++
                    }
                    '+' -> {
                        evaluate(numStack, opStack)
                    }
                    '*' -> {
                        opStack.push(c)
                        i++
                    }
                }
            } else if (c == '(') {
                opStack.push(c)
                i++
            } else if (c == ')') {
                while (opStack.peek() != '(') {
                    evaluate(numStack, opStack)
                }
                opStack.pop()
                i++
            }
        }
    }
    while (!opStack.isEmpty()) {
        evaluate(numStack, opStack)
    }
    return numStack.peek()
}

fun evaluate(numStack: ArrayDeque<Long>, opStack: ArrayDeque<Char>) {
    val op = opStack.pop()
    val n2 = numStack.pop()
    val n1 = numStack.pop()

    val result = when (op) {
        '+' -> n1 + n2
        '*' -> n1 * n2
        else -> 0
    }
    numStack.push(result)
}