class Day7(val input: List<String>) {

    data class Hand(val hand: String, val rules:Int = 1): Comparable<Hand> {
        val score = handScore()
        private fun handScore(): Int {
            val grouped = hand.filter { it != '*' }.groupBy { it }.map { it.value.size }.sorted().reversed()
            return when (grouped) {
                // 5 jokers
                listOf<Int>() -> 7
                // 4 jokers
                listOf(1) -> 7
                // 3 jokers
                listOf(2) -> 7
                listOf(1, 1) -> 6
                // 2 jokers
                listOf(3) -> 7
                listOf(2, 1) -> 6
                listOf(1, 1, 1,) -> 4
                // 1 joker
                listOf(4) -> 7
                listOf(3, 1) -> 6
                listOf(2, 2) -> 5
                listOf(2, 1, 1) -> 4
                listOf(1, 1, 1, 1) -> 2
                // no jokers
                listOf(5) -> 7
                listOf(4, 1) -> 6
                listOf(3, 2) -> 5
                listOf(3, 1, 1) -> 4
                listOf(2, 2, 1) -> 3
                listOf(2, 1, 1, 1) -> 2
                listOf(1, 1, 1, 1, 1) -> 1
                else -> throw IllegalStateException("This should not happen: $grouped for $hand")
            }
        }

        fun convertCard(cardChar: Char) = when (cardChar) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            '*' -> 1
            else -> cardChar.digitToInt()
        }

        override fun compareTo(other: Hand): Int {
            val score = score.compareTo(other.score)
            if (score != 0) {
                return score
            } else {
                hand.zip(other.hand).forEach { (aChar, bChar) ->
                    val score = convertCard(aChar).compareTo(convertCard(bChar))
                    if (score != 0) {
                        return score
                    }
                }
                return 0
            }

        }
    }

    private fun solve(input: List<String>): Long {
        val cards = input.map { it.splitIgnoreEmpty(" ").let { Hand(it[0]) to it[1].toInt() } }
        val results = cards.sortedBy{it.first}
        return results.mapIndexed { index: Int, pair: Pair<Hand, Int> -> (index+1) * pair.second.toLong() }.sum()

    }
    fun solvePart1() = solve(input)
    fun solvePart2() = solve(input.map { it.replace('J', '*') })
}