package aoc

import java.util.*

val cups = TreeSet<Node>()

fun main() {
    val input = listOf(6, 4, 3, 7, 1, 9, 2, 5, 8)
    //val input = listOf(3, 8, 9, 1, 2, 5, 4, 6, 7)

    var input1 = input.toList()
    for (i in 0 until 100) {
        input1 = play(input1)
    }
    Collections.rotate(input1, (input1.indexOf(1) * -1) - 1)
    println("Part 1: " + input1.filter { it != 1 }.joinToString("") { it.toString() })

    val inp = input.map { it.toLong() }.toMutableList()
    inp.addAll(((inp.maxOrNull() ?: 0) + 1..1000000))

    val origHead = inp.toLinkedList()

    var head = origHead
    for (i in 0 until 10000000) {
        val toMove = listOf(head.next, head.next.next, head.next.next.next)
        val next = head.next.next.next.next

        head.next = next
        cups.removeAll(toMove)

        val dest = cups.lower(head) ?: cups.last()
        val after = dest.next
        dest.next = toMove[0]
        toMove[2].next = after

        cups.addAll(toMove)
        head = next
    }
    val one = cups.floor(Node(1))!!
    println(one.next.data)
    println(one.next.next.data)
    println("Part 2: " + one.next.data * one.next.next.data)
}

fun play(input: List<Int>): List<Int> {
    var nums = input.toMutableList()
    // Current cup will always be at 0
    val current = nums.removeAt(0)
    val pickUp = nums.subList(0, 3)
    nums = nums.drop(3).toMutableList()
    var destination = current - 1
    val limit = nums.minOrNull() ?: 0
    while (destination >= limit) {
        if (pickUp.contains(destination)) {
            destination--
        } else if (nums.contains(destination)) {
            break
        }
    }
    if (destination < limit) {
        destination = nums.maxOrNull() ?: 0
    }
    nums.add(0, current)
    val destinationIndex = nums.indexOf(destination)
    for (i in 0 until 3) {
        nums.add(destinationIndex + i + 1, pickUp[i])
    }
    Collections.rotate(nums, -1)
    return nums
}

private fun List<Long>.toLinkedList(): Node {
    val head = Node(this[0])
    var prev = head
    cups.add(head)
    for (i in 1 until size) {
        val next = Node(this[i])
        cups.add(next)
        prev.next = next
        prev = next
    }

    prev.next = head
    return head
}

class Node(val data: Long) : Comparable<Node> {
    lateinit var next: Node

    override fun compareTo(other: Node): Int {
        return data.compareTo(other.data)
    }
}