import kotlin.math.pow

class Day4(val input: List<String>) {
    fun solvePart1(): Int {
        val result = input.map { line ->
            val parts = line.splitIgnoreEmpty(" ")
            val winning = parts.drop(2).takeWhile { it != "|" }.map { it.toInt() }.toSet()
            val numbers = parts.dropWhile { it != "|" }.drop(1).map { it.toInt() }
            val power = numbers.count{it in winning}
            if(power > 0) {
                2.toDouble().pow(power - 1).toInt()
            } else {
                0
            }

        }
        return result.sum()
    }
    fun solvePart2(): Int {
        TODO()
    }
}