class Day23(val input: List<String>) {
    private val initialMap = input.flatMapIndexed { y, line -> line.mapIndexed { x, c -> Point2D(x, y) to c } }.toMap()
    private val maxY = input.size-1
    private val maxX = input.first().length-1
    private val origStart = initialMap.filter { it.key.y == 0 && it.value == '.' }.keys.first()
    private val origFinish = initialMap.filter { it.key.y == maxY && it.value == '.' }.keys.first()

    data class Path(val cur: Point2D, val path: Set<Point2D>) {
        fun steps(vertices: List<Triple<Point2D, Point2D, Int>>): Int {
            return (path + cur).windowed(2).sumOf { pathPart -> vertices.first { it.first == pathPart[0] && it.second == pathPart[1] }.third }
        }
    }
    fun solvePart1(): Int {
        //initialMap.printChars()
        var paths = listOf(Path(origStart, setOf(origStart)))
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
    fun findPathToIntersection(start: Point2D, prev: Point2D, intersections: Set<Point2D>): Triple<Point2D, Point2D, Int> {
        val visited = mutableSetOf(prev)
        var cur = start
        for(i in 1..1_000) {
            visited += cur
            val options = cur.adjacent().filter { it !in visited }.filter {
                when (initialMap[it]) {
                    '>' -> true
                    '<' -> true
                    '^' -> true
                    'v' -> true
                    '.' -> true
                    '#' -> false
                    null -> false
                    else -> throw IllegalStateException("Found char ${initialMap[it]}")
                }
            }.toList()
            if(options.size > 1) {
                println("$prev - $cur - $options - $visited")
            }
            assert(options.size == 1)
            val next = options.first()

            if (next in intersections) {
                return Triple(prev, next, visited.size)
            }
            cur = next
        }
        TODO()
    }
    fun solvePart2(): Int {
        //initialMap.printChars()
        val vertices = mutableListOf<Triple<Point2D, Point2D, Int>>()
        val intersections = initialMap.filter { it.value != '#' }.filter { it.key.adjacent().filter { (initialMap[it]?:'#') != '#' }.count() >= 3 }.keys + origStart + origFinish
        intersections.forEach { println(it) }
        intersections.forEach { isect ->
            val pathsFromIntersect = isect.adjacent().filter { (initialMap[it]?:'#') != '#' }.map { findPathToIntersection(it, isect, intersections) }
            vertices.addAll(pathsFromIntersect)
        }
        vertices.forEach{ println(it) }

        var paths = listOf(Path(origStart, setOf(origStart)))
        var longest = paths.first()
        var steps = 0
        while(paths.isNotEmpty()) {

            paths = paths.flatMap { path ->
                vertices.filter { it.first == path.cur }.filter { it.second !in path.path }
                .map { Path(it.second, path.path + path.cur) }
            }
            val finished = paths.filter { it.cur == origFinish }
            if(finished.isNotEmpty()) {
                val longestInThisRun = finished.maxBy { it.steps(vertices) }
                if (longestInThisRun.steps(vertices) > longest.steps(vertices)) {
                    longest = longestInThisRun
                }
            }
            paths = paths.filter { it.cur != origFinish }
            steps++
            if(steps % 1 == 0) {
                println("[$steps] - ${paths.size} - ${finished.size}")
            }
        }
        println(longest)
        return longest.steps(vertices)
        /*
        val print = initialMap.toMutableMap()
        print.putAll(longest.path.map { it to 'o' })
        print.printChars()
        return longest.path.size-1
         */
        TODO()
    }
}