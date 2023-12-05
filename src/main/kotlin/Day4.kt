import kotlin.math.pow

class Day4(input: List<String>) {
    private val cardValues = input.associate { line ->
        val parts = line.splitIgnoreEmpty(" ")
        val cardNumber = parts[1].filter { it.isDigit() }.toInt()
        val winning = parts.drop(2).takeWhile { it != "|" }.map { it.toInt() }.toSet()
        val numbers = parts.dropWhile { it != "|" }.drop(1).map { it.toInt() }
        val power = numbers.count{it in winning}
        if(power > 0) {
            cardNumber to (2.toDouble().pow(power - 1).toInt() to power)
        } else {
            cardNumber to (0 to 0)
        }
    }
    fun solvePart1() = cardValues.values.sumOf{it.first}


    fun solvePart2(): Int {
        val cardCounter = mutableMapOf<Int, Int>()
        cardValues.forEach{ (cardId, value) ->
            cardCounter[cardId] = cardCounter[cardId]?.plus(1) ?: 1
            val numCards = cardCounter[cardId]!!
            (cardId+1..cardId+value.second).forEach {
                cardCounter[it] = cardCounter[it]?.plus(numCards) ?: numCards
            }
        }
        return cardCounter.values.sum()
    }
}