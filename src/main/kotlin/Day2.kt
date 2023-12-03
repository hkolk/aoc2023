class Day2(val input: List<String>) {
    fun solvePart1(): Int {
        val validate = mapOf("red" to 12, "green" to 13, "blue" to 14)

        var sum = 0
        input.forEach { line ->
            val parts =  line.filter { it.isLetterOrDigit() || it.isWhitespace() }.splitIgnoreEmpty(" ")
            var possible = true
            parts.chunked(2).drop(1).forEach {
                val amount = it[0].toInt()
                if(!validate.contains(it[1]) || validate[it[1]]!! < amount) {
                    possible = false
                }
            }
            if(possible) {
                sum += parts[1].toInt()
            }
        }
        return sum
    }
    fun solvePart2(): Int {
        var sum = 0
        input.forEach { line ->
            val parts =  line.filter { it.isLetterOrDigit() || it.isWhitespace() }.splitIgnoreEmpty(" ")
            val bag = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            parts.chunked(2).drop(1).forEach {
                val amount = it[0].toInt()
                if(bag[it[1]]!! < amount) {
                    bag[it[1]] = amount
                }
            }
            sum += bag.values.fold(1) { acc, it -> it * acc}
        }
        return sum
    }
}