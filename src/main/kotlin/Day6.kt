class Day6(val input: List<String>) {
    fun solvePart1(): Long {
        val times = input[0].splitIgnoreEmpty(" ").drop(1).map { it.toInt() }
        val distances = input[1].splitIgnoreEmpty(" ").drop(1).map { it.toInt() }
        val pairs = times.zip(distances)
        val options = pairs.map { (time, record) ->
            val distances = (0..time).map { holdTime ->
                (time-holdTime) * holdTime
            }
            distances.count { it > record }
        }
        return options.multiply()
    }
    fun solvePart2(): Int {
        val time = input[0].filter { it.isDigit() }.toLong()
        val record = input[1].filter { it.isDigit() }.toLong()
        val distances = (0..time).map { holdTime ->
            (time-holdTime) * holdTime
        }
        return distances.count { it > record }
    }
}