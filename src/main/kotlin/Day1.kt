class Day1(val input: List<String>) {
    fun solvePart1(): Int {
        return input.map {line ->
            line.filter { it.isDigit() }.let { (""+it.first() + it.last()).toInt() }
        }.sum()
    }
    fun solvePart2(): Int {
        TODO()
    }
}