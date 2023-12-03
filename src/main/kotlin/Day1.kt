class Day1(private val input: List<String>) {
    private val numbersMap = mapOf("one" to "1",
        "two" to   "2",
        "three" to "3",
        "four" to  "4",
        "five" to  "5",
        "six" to   "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to  "9")
    fun solvePart1() = input.sumOf { line ->
            line.filter { it.isDigit() }.let { ("" + it.first() + it.last()).toInt() }
        }
    fun solvePart2() = input.sumOf { line ->
            val first = line.findAnyOf(numbersMap.keys + numbersMap.values)!!
                .let { (numbersMap[it.second] ?: it.second).toInt() }
            val last = line.findLastAnyOf(numbersMap.keys + numbersMap.values)!!
                .let { (numbersMap[it.second] ?: it.second).toInt() }
            first * 10 + last
        }
}