class Day8(val input: List<String>) {
    val paths = input.drop(2).associate { line -> line.splitIgnoreEmpty(" ", "(", ")", ",").let { it[0] to Pair(it[2], it[3]) } }
    val initialDirections = input.first().toList()

    fun solve(start: String, state:List<Char>): Triple<Int, String, List<Char>> {
        val directions = ArrayDeque(state)
        var cur = start
        repeat(1_000_000) {step ->
            if(directions.isEmpty()) {
                directions.addAll(input.first().toList())
            }
            cur = when(directions.removeFirst()) {
                'L' -> paths[cur]!!.first
                'R' -> paths[cur]!!.second
                else -> throw IllegalStateException()
            }
            if(cur.endsWith("Z")) {
                return Triple(step+1, cur, directions)
            }
        }
        TODO()
    }
    fun solvePart1() = solve("AAA", initialDirections).first

    fun solvePart2(): Long {
        var starts = paths.keys.filter { it.endsWith("A") }
        val times = starts.map { solve(it, initialDirections) }
        val div = times.map { it.first / initialDirections.size }
        //println(div)
        //println(div.multiply())
        //println(initialDirections.size * div.multiply())
        return initialDirections.size * div.multiply()

    }
}