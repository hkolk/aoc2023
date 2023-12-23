class Day23(val input: List<String>) {
    private val initialMap = input.flatMapIndexed { y, line -> line.mapIndexed { x, c -> Point2D(x, y) to c } }.toMap()
    private val maxY = input.size-1
    private val maxX = input.first().length-1
    private val origStart = initialMap.filter { it.key.y == 0 && it.value == '.' }.keys.first()
    private val origFinish = initialMap.filter { it.key.y == maxY && it.value == '.' }.keys.first()

    data class Path(val cur: Point2D, val path: List<Point2D>)
    fun solvePart1(): Int {
        //initialMap.printChars()
        var paths = listOf(Path(origStart, listOf(origStart)))
        val finished = mutableListOf<Path>()
        for(steps in 1.. 10_000) {
            paths = paths.flatMap { path ->
                // find next options
                path.cur.adjacent().filter { it !in path.path }.filter {
                    when(initialMap[it]) {
                        '>' -> Point2D.WEST(it) == path.cur
                        '<' -> Point2D.EAST(it) == path.cur
                        '^' -> Point2D.SOUTH(it) == path.cur
                        'v' -> Point2D.NORTH(it) == path.cur
                        '.' -> true
                        '#' -> false
                        null -> false
                        else -> throw IllegalStateException("Found char ${initialMap[it]}")
                    }
                }.map { Path(it, path.path + path.cur) }
            }
            finished.addAll(paths.filter { it.cur == origFinish })
            paths = paths.filter { it.cur != origFinish }
        }
        val longest = finished.sortedBy { it.path.size }.last()
        val print = initialMap.toMutableMap()
        print.putAll(longest.path.map { it to 'o' })
        print.printChars()
        println(finished.map { it.path.size-1 })
        return longest.path.size-1
        TODO()
    }
    fun solvePart2(): Int {
        TODO()
    }
}