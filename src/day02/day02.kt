package day02

import println
import readInput
import kotlin.math.max

fun main() {

    val cubeLimits = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    fun part1(input: List<String>): Int {
        return input.sumOf { game ->
            val (gameHeader, gameData) = game.split(':')
            val sets = gameData.split(";")
            val isGamePossible = sets.map { set ->
                set.split(",").map { revealedCubes ->
                    val (number, color) = revealedCubes.trim().split(" ")
                    number.toInt() <= (cubeLimits[color] ?: throw NoSuchElementException())
                }.reduce { acc, next ->
                    acc && next
                }
            }.reduce { acc, next ->
                acc && next
            }
            if (isGamePossible) {
                gameHeader.split(" ").last().toInt()
            } else {
                0
            }
        }
    }

    fun maxReduceMaps(
        acc: Map<String, Int>,
        nextSetMin: Map<String, Int>
    ): Map<String, Int> {
        val keys = buildSet {
            addAll(acc.keys)
            addAll(nextSetMin.keys)
        }
        return keys.associateWith { color ->
            val accValue = acc[color] ?: 0
            val nextValue = nextSetMin[color] ?: 0

            max(accValue, nextValue)
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { game ->
            val (_, gameData) = game.split(':')
            val sets = gameData.split(";")
            val gameMinimum = sets.map { set ->
                val minimumPerColor = set.split(",").associate { revealedCubes ->
                    val (number, color) = revealedCubes.trim().split(" ")
                    color to number.toInt()
                }
                minimumPerColor
            }.reduce { acc, nextSetMin ->
                maxReduceMaps(acc, nextSetMin)
            }
            val gamePower = gameMinimum.values.reduce { acc, i -> acc * i }
            gamePower
        }
    }

    val input = readInput("day02", "Day02_input")

    part1(input).println()
    part2(input).println()
}
