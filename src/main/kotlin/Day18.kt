class Day18(val input: List<String>) {
    val commands =
        input.map { line -> line.splitIgnoreEmpty(" ", "(", ")", "#").let { Triple(it[0], it[1].toInt(), it[2]) } }

    fun solvePart1(): Int {
        var pos = Point2D(0, 0)
        val dug = mutableSetOf(pos)
        commands.forEach { (direction, steps, _) ->
            val newPos = when (direction) {
                "R" -> pos.move(Point2D.EAST, steps)
                "D" -> pos.move(Point2D.SOUTH, steps)
                "L" -> pos.move(Point2D.WEST, steps)
                "U" -> pos.move(Point2D.NORTH, steps)
                else -> throw IllegalStateException("$direction is not found")
            }
            dug.addAll(pos.allPointsTo(newPos))
            pos = newPos
        }
        //dug.print()
        // flood fill
        val queue = ArrayDeque<Point2D>()
        queue.add(Point2D(1, 1))
        while (queue.isNotEmpty()) {
            val pos = queue.removeFirst()
            if (pos !in dug) {
                dug.add(pos)
                queue.addAll(pos.adjacent())
            }
        }
        dug.print()
        return dug.size

        TODO()
    }

    data class Point2DWide(val x: Long, val y: Long) {
        fun distance(other: Point2DWide): Long {
            return x.coerceAtLeast(other.x) - x.coerceAtMost(other.x) + y.coerceAtLeast(other.y) - y.coerceAtMost(other.y)
        }
    }

    fun solvePart2(): Long {
        var pos = Point2DWide(0, 0)
        val vectors = mutableSetOf<Pair<Point2DWide, Point2DWide>>()
        //commands.forEach { (direction, steps, command) ->
        commands.forEach { (_, _, command) ->
            val steps = command.take(5).toInt(16)
            val direction = listOf("R", "D", "L", "U")[command.last().digitToInt()]
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
        var prev = points.first()
        val positive = points.drop(1).map {
            val ret = prev.x * it.y - prev.y * it.x
            prev = it
            ret
        }
        prev = points.first()

        //println(points.zipWithNext { a, b -> a.x * b.y - a.y * b.x })

        val area = positive.sum() / 2
        println("Shoelace: ${positive.sum()} / 2 = $area")
        val perimiter = vectors.sumOf { it.first.distance(it.second).toLong() }
        println("Perimiter: $perimiter")
        val picks = area + perimiter / 2L + 1L
        println("Picks: $picks")
        return picks
    }
}