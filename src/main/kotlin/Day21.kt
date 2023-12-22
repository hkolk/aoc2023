class Day21(val input: List<String>) {
    val map = input.flatMapIndexed{y, line -> line.mapIndexed { x, c -> Point2D(x, y) to c  }}.toMap()
    val start = map.filterValues { it == 'S' }.keys.first()
    fun solvePart1(steps:Int=64): Int {
        val visited = mutableSetOf<Point2D>()
        var edge = setOf(start)
        repeat(steps) { step ->
            edge = edge.flatMap{ cur ->
                val newPos = cur.adjacent().filter { map[it] in listOf('.', 'S')}.toList()
                //visited.addAll(newPos)
                newPos
            }.toSet()
        }
        edge.print()
        return edge.size

    }
    fun solvePart2(): Int {
        TODO()
    }
}