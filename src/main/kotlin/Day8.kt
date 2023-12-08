class Day8(val input: List<String>) {
    val paths = input.drop(2).associate { line -> line.splitIgnoreEmpty(" ", "(", ")", ",").let { it[0] to Pair(it[2], it[3]) } }
    val initialDirections = input.first().toList()

    fun solvePart1(): Int {
        val directions = ArrayDeque(initialDirections)
        var cur = "AAA"
        repeat(1_000_000) {step ->
            if(cur == "ZZZ") {
                return step
            }
            if(directions.isEmpty()) {
                directions.addAll(input.first().toList())
            }
            cur = when(directions.removeFirst()) {
                'L' -> paths[cur]!!.first
                'R' -> paths[cur]!!.second
                else -> throw IllegalStateException()
            }
        }
        TODO()
    }
    fun solvePart1() = solve("AAA")
    fun solvePart2(): Int {
        val paths = input.drop(2).associate { line -> line.splitIgnoreEmpty(" ", "(", ")", ",").let { it[0] to Pair(it[2], it[3]) } }
        val directions = ArrayDeque(input.first().toList())
        var starts = paths.keys.filter { it.endsWith("A") }
        repeat(1_000_000_000) {step ->
            if(step % 1_000_000 == 0) {
                println("Step: $step, state: $starts")
            }
            if(starts.all { it.endsWith("Z") }) {
                return step
            }
            if(directions.isEmpty()) {
                directions.addAll(input.first().toList())
            }
            val nextStep = directions.removeFirst()
            starts = starts.map { cur ->
                when(nextStep) {
                    'L' -> paths[cur]!!.first
                    'R' -> paths[cur]!!.second
                    else -> throw IllegalStateException()
                }
            }
        }
        TODO()
    }
}