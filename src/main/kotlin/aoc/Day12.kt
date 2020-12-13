package aoc

import java.util.*
import kotlin.math.abs

fun main() {
    val input = toListOfString("input_day12.txt")
    val directions = input.map { Direction(it) }

    // Part1
    var position = Position(0, 0, 'E')
    for (direction in directions) {
        position = direction.parse(position)
    }
    println("(Part 1) Manhattan Distance: " + position.manhattanDistance())

    // Part2
    var ship = Position(0L, 0L)
    var waypoint = Position(10L, 1L)
    for (direction in directions) {
        val result = direction.parse(ship, waypoint)
        ship = result.first
        waypoint = result.second
    }
    println("(Part 2) Manhattan Distance: " + ship.manhattanDistance())
}

data class Position(val x: Long, val y: Long, val dir: Char = 'N') {
    fun manhattanDistance(): Long {
        return abs(x) + abs(y)
    }
}

class Direction(private val input: String) {
    private val directions = listOf('N', 'E', 'S', 'W')

    fun parse(position: Position): Position {
        val num = input.filter { it.isDigit() }.toLong()
        return when (input.toCharArray()[0]) {
            'N' -> Position(position.x, position.y + num, position.dir)
            'S' -> Position(position.x, position.y - num, position.dir)
            'E' -> Position(position.x + num, position.y, position.dir)
            'W' -> Position(position.x - num, position.y, position.dir)
            'L' -> {
                val currentIndex = directions.indexOf(position.dir)
                Collections.rotate(directions, (num / 90).toInt())
                return Position(position.x, position.y, directions[currentIndex])
            }
            'R' -> {
                val currentIndex = directions.indexOf(position.dir)
                Collections.rotate(directions, (-num / 90).toInt())
                return Position(position.x, position.y, directions[currentIndex])
            }
            'F' -> {
                return when (position.dir) {
                    'N' -> Position(position.x, position.y + num, position.dir)
                    'S' -> Position(position.x, position.y - num, position.dir)
                    'E' -> Position(position.x + num, position.y, position.dir)
                    'W' -> Position(position.x - num, position.y, position.dir)
                    else -> throw IllegalArgumentException("Bad direction '$position.dir'")
                }
            }
            else -> throw IllegalArgumentException("Bad input '$input'")
        }
    }

    fun parse(ship: Position, waypoint: Position): Pair<Position, Position> {
        val num = input.filter { it.isDigit() }.toInt()
        return when (input.toCharArray()[0]) {
            'N' -> Pair(ship, Position(waypoint.x, waypoint.y + num))
            'S' -> Pair(ship, Position(waypoint.x, waypoint.y - num))
            'E' -> Pair(ship, Position(waypoint.x + num, waypoint.y))
            'W' -> Pair(ship, Position(waypoint.x - num, waypoint.y))
            'L' -> {
                val difference = Pair(waypoint.x - ship.x, waypoint.y - ship.y)
                val newDifference = when (num) {
                    90 -> Pair(-difference.second, difference.first)
                    180 -> Pair(-difference.first, -difference.second)
                    270 -> Pair(difference.second, -difference.first)
                    else -> throw IllegalArgumentException("Bad num $num")
                }
                return Pair(ship, Position(ship.x + newDifference.first, ship.y + newDifference.second))
            }
            'R' -> {
                val difference = Pair(waypoint.x - ship.x, waypoint.y - ship.y)
                val newDifference = when (num) {
                    90 -> Pair(difference.second, -difference.first)
                    180 -> Pair(-difference.first, -difference.second)
                    270 -> Pair(-difference.second, difference.first)
                    else -> throw IllegalArgumentException("Bad num $num")
                }
                return Pair(ship, Position(ship.x + newDifference.first, ship.y + newDifference.second))
            }
            'F' -> {
                val difference = Pair(waypoint.x - ship.x, waypoint.y - ship.y)
                val newShip = Position(ship.x + num * difference.first, ship.y + num * difference.second)
                return Pair(
                    newShip,
                    Position(difference.first + newShip.x, difference.second + newShip.y)
                )
            }
            else -> throw IllegalArgumentException("Bad input '$input'")
        }
    }
}
