class Day18(val input: List<String>) {
    val commands =
        input.map { line -> line.splitIgnoreEmpty(" ", "(", ")", "#").let { Triple(it[0], it[1].toInt(), it[2]) } }

    fun solvePart1() = solve(commands.map { it.first to it.second })
    fun solvePart2() = solve(commands.map {cmd -> cmd.third.let {
        listOf("R", "D", "L", "U")[it.last().digitToInt()] to it.take(5).toInt(16) }
    })

    private fun solve(commands: List<Pair<String, Int>>): Long {
        var pos = Point2DWide(0, 0)
        val vectors = mutableSetOf<Pair<Point2DWide, Point2DWide>>()
        commands.forEach { (direction, steps) ->
            val newPos = when (direction) {
                "R" -> Point2DWide(x = pos.x + steps, y = pos.y)
                "D" -> Point2DWide(x = pos.x, y = pos.y + steps)
                "L" -> Point2DWide(x = pos.x - steps, y = pos.y)
                "U" -> Point2DWide(x = pos.x, y = pos.y - steps)
                else -> throw IllegalStateException("$direction is not found")
            }
            vectors.add(pos to newPos)
            pos = newPos
        }
        val points = (vectors.map { it.first } + vectors.last().second)
        val positive = points.zipWithNext { a, b -> a.x * b.y - a.y * b.x }

        val area = positive.sum() / 2
        //println("Shoelace: ${positive.sum()} / 2 = $area")
        val perimiter = vectors.sumOf { it.first.distance(it.second) }
        //println("Perimiter: $perimiter")
        val picks = area + perimiter / 2L + 1L
        //println("Picks: $picks")
        return picks
    }
}