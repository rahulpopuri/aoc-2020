package aoc

fun main() {
    val input = to2dCharList("input_day17.txt")

    val grid3d = mutableMapOf<Triple<Int, Int, Int>, Char>()
    val grid4d = mutableMapOf<Quadruple<Int, Int, Int, Int>, Char>()
    for (y in input.indices) {
        for (x in input[0].indices) {
            grid3d[Triple(x, y, 0)] = input[x][y]
            grid4d[Quadruple(x, y, 0, 0)] = input[x][y]
        }
    }
    for (i in 0 until 6) {
        part1(grid3d)
        part2(grid4d)
    }

    println("Part 1: " + grid3d.values.count { it == '#' })
    println("Part 2: " + grid4d.values.count { it == '#' })
}

data class Quadruple<X, Y, Z, K>(val x: X, val y: Y, val z: Z, val k: K)

fun part1(input: MutableMap<Triple<Int, Int, Int>, Char>) {
    val current = input.toMap()
    val range = arrayOf(-1, 0, 1)

    val pending = mutableSetOf<Triple<Int, Int, Int>>()
    input.keys.forEach {
        for (i in range) {
            for (j in range) {
                for (k in range) {
                    for (w in range) {
                        pending.add(Triple(it.first + i, it.second + j, it.third + k))
                    }
                }
            }
        }
    }
    input.clear()

    pending.forEach {
        var nearbyActive = 0
        for (i in range) {
            for (j in range) {
                for (k in range) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue
                    }
                    if (current[Triple(it.first + i, it.second + j, it.third + k)] == '#') {
                        nearbyActive++
                    }
                }
            }
        }
        when (current[it]) {
            '#' -> input[it] = when (nearbyActive) {
                2, 3 -> '#'
                else -> '.'
            }
            else -> input[it] = when (nearbyActive) {
                3 -> '#'
                else -> '.'
            }
        }
    }
}

private fun part2(input: MutableMap<Quadruple<Int, Int, Int, Int>, Char>) {
    val current = input.toMap()
    val range = arrayOf(-1, 0, 1)

    val pending = mutableSetOf<Quadruple<Int, Int, Int, Int>>()
    input.keys.forEach {
        for (i in range) {
            for (j in range) {
                for (k in range) {
                    for (l in range) {
                        pending.add(Quadruple(it.x + i, it.y + j, it.z + k, it.k + l))
                    }
                }
            }
        }
    }
    input.clear()

    pending.forEach {
        var nearbyActive = 0
        for (i in range) {
            for (j in range) {
                for (k in range) {
                    for (w in range) {
                        if (i == 0 && j == 0 && k == 0 && w == 0) {
                            continue
                        }
                        if (current[Quadruple(it.x + i, it.y + j, it.z + k, it.k + w)] == '#') {
                            nearbyActive++
                        }
                    }
                }
            }
        }
        when (current[it]) {
            '#' -> input[it] = when (nearbyActive) {
                2, 3 -> '#'
                else -> '.'
            }
            else -> input[it] = when (nearbyActive) {
                3 -> '#'
                else -> '.'
            }
        }
    }
}
