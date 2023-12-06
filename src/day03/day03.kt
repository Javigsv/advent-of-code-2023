package day03

import println
import readInput

private sealed interface Symbol {
    data object Period : Symbol
    data object EngineSymbol : Symbol
    data class Digit(val value: Char) : Symbol
}

fun main() {

    val directions = listOf(
        (-1 to -1),
        (-1 to 0),
        (-1 to 1),
        (0 to -1),
        (0 to 1),
        (1 to -1),
        (1 to 0),
        (1 to 1),
    )

    fun findToTheRightOf(
        rowInx: Int,
        colIdx: Int,
        matrix: MutableList<MutableList<Symbol>>
    ): String {
        val symbol = matrix.getOrNull(rowInx)?.getOrNull(colIdx + 1) ?: return ""
        if (symbol !is Symbol.Digit) return ""
        matrix[rowInx][colIdx + 1] = Symbol.Period
        return symbol.value + findToTheRightOf(rowInx, colIdx + 1, matrix)
    }

    fun findToTheLeftOf(
        rowInx: Int,
        colIdx: Int,
        matrix: MutableList<MutableList<Symbol>>
    ): String {
        val symbol = matrix.getOrNull(rowInx)?.getOrNull(colIdx - 1) ?: return ""
        if (symbol !is Symbol.Digit) return ""
        matrix[rowInx][colIdx - 1] = Symbol.Period
        return findToTheLeftOf(rowInx, colIdx - 1, matrix) + symbol.value
    }

    fun findNumber(rowInx: Int, colIdx: Int, matrix: MutableList<MutableList<Symbol>>): Int? {
        val symbol = matrix.getOrNull(rowInx)?.getOrNull(colIdx) ?: return null
        return if (symbol is Symbol.Digit) {
            val left = findToTheLeftOf(rowInx, colIdx, matrix)
            val right = findToTheRightOf(rowInx, colIdx, matrix)
            (left + symbol.value + right).toInt()
        } else null
    }


    fun createMatrix(input: List<String>): MutableList<MutableList<Symbol>> = input.map { line ->
        line.split("")
            .filter { it.isNotEmpty() }
            .map { symbol ->
                when (symbol) {
                    "." -> Symbol.Period
                    else -> symbol.toIntOrNull()?.let {
                        Symbol.Digit(it.digitToChar())
                    } ?: Symbol.EngineSymbol
                }
            }.toMutableList()
    }.toMutableList()

    fun part1(input: List<String>): Int {
        val matrix = createMatrix(input)

        val engineNumbers = buildList {
            matrix.forEachIndexed { rowIdx, row ->
                row.forEachIndexed { colIdx, symbol ->
                    if (symbol is Symbol.EngineSymbol) {
                        directions.forEach { offsets ->
                            val engineNumber = findNumber(
                                rowInx = rowIdx + offsets.first,
                                colIdx = colIdx + offsets.second,
                                matrix = matrix
                            )
                            engineNumber?.let { add(it) }
                        }
                    }
                }
            }
        }

        return engineNumbers.sum()
    }

    fun part2(input: List<String>): Int {
        val matrix = createMatrix(input)

        val engineNumbers = buildList {
            matrix.forEachIndexed { rowIdx, row ->
                row.forEachIndexed { colIdx, symbol ->
                    if (symbol is Symbol.EngineSymbol) {
                        val engineNumbers = directions.mapNotNull { offsets ->
                            val engineNumber = findNumber(
                                rowInx = rowIdx + offsets.first,
                                colIdx = colIdx + offsets.second,
                                matrix = matrix
                            )
                            engineNumber
                        }
                        if (engineNumbers.size == 2) {
                            add(engineNumbers.reduce { acc, i -> acc * i })
                        }
                    }
                }
            }
        }

        return engineNumbers.sum()
    }

    val input = readInput("day03", "Day03_input")

    part1(input).println()
    part2(input).println()
}
