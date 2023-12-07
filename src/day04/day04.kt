package day04

import println
import readInput
import kotlin.math.pow

fun main() {

    fun parseGame(game: String): Pair<String, Pair<List<String>, List<String>>> {
        val (gameHeader, gameData) = game.split(':')
        val (winningNumbers, myNumbers) = gameData.trim().split("|")
            .let { (winningNumbers, myNumbers) ->
                winningNumbers.trim().split(" ").filter { it.isNotEmpty() } to
                        myNumbers.trim().split(" ").filter { it.isNotEmpty() }
            }
        return (gameHeader to (winningNumbers to myNumbers))
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { game ->
            val (_, gameData) = parseGame(game)
            val (winningNumbers, myNumbers) = gameData

            val prise = myNumbers.count { it in winningNumbers }
            if (prise == 0) 0
            else 2.0.pow(prise - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val cardCopiesAndPrises = input.map { game ->
            val (_, gameData) = parseGame(game)
            val (winningNumbers, myNumbers) = gameData

            1 to myNumbers.count { it in winningNumbers }
        }.toMutableList()

        cardCopiesAndPrises.forEachIndexed { index, (copies, prise) ->
            repeat(prise) { offset ->
                val nextCardIndex = index + offset + 1
                if (nextCardIndex <= cardCopiesAndPrises.lastIndex) {
                    val (nextCardCopies, nextCardPrise) = cardCopiesAndPrises[nextCardIndex]
                    cardCopiesAndPrises[nextCardIndex] = nextCardCopies + copies to nextCardPrise
                }
            }
        }

        return cardCopiesAndPrises.sumOf { it.first }
    }

    val input = readInput("day04", "Day04_input")

    part1(input).println()
    part2(input).println()
}
