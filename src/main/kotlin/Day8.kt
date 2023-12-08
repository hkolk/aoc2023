class Day8(input: List<String>) {
    private val paths = input.drop(2).associate { line -> line.splitIgnoreEmpty(" ", "(", ")", ",").let { it[0] to Pair(it[2], it[3]) } }
    private val initialDirections = input.first().toList()

    private fun solve(start: String, state:List<Char>): Int {
        val directions = CircularIterator(state)
        var cur = start
        repeat(1_000_000) {step ->
            cur = when(directions.next()) {
                'L' -> paths[cur]!!.first
                'R' -> paths[cur]!!.second
                else -> throw IllegalStateException()
            }
            if(cur.endsWith("Z")) {
                return step+1
            }
        }
        throw IllegalStateException("Solution not found after 1_000_000 steps")
    }
    fun solvePart1() = solve("AAA", initialDirections)

    fun solvePart2(): Long {
        var starts = paths.keys.filter { it.endsWith("A") }
        val times = starts.map { solve(it, initialDirections).toLong() }
        return times.lcm()
    }

}