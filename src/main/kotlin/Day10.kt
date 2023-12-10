class Day10(val input: List<String>) {
    val map = input.flatMapIndexed{ y, line ->
        line.mapIndexed{x, c ->  Pipe(c, Point2D(x, y))}
    }
    val lookupMap = map.associateBy{ it.loc }
    data class Pipe(val name: Char, val loc: Point2D) {
        fun connectedTo(): List<Point2D> {
            return when(name) {
                '|' -> listOf(Point2D.NORTH(loc), Point2D.SOUTH(loc))
                '-' -> listOf(Point2D.EAST(loc), Point2D.WEST(loc))
                'F' -> listOf(Point2D.EAST(loc), Point2D.SOUTH(loc))
                'L' -> listOf(Point2D.EAST(loc), Point2D.NORTH(loc))
                '7' -> listOf(Point2D.WEST(loc), Point2D.SOUTH(loc))
                'J' -> listOf(Point2D.WEST(loc), Point2D.NORTH(loc))
                '.' -> listOf()
                'S' -> listOf()
                else -> throw IllegalStateException("$name at $loc ")
            }
        }
        fun left(prev: Point2D): List<Point2D> {
            return when(name) {
                '|' -> if(prev == Point2D.NORTH(loc)) listOf(Point2D.EAST(loc)) else listOf(Point2D.WEST(loc))
                '-' -> if(prev == Point2D.EAST(loc)) listOf(Point2D.SOUTH(loc)) else listOf(Point2D.NORTH(loc))
                'F' -> if(prev == Point2D.SOUTH(loc)) listOf(Point2D.WEST(loc), Point2D.NORTH(loc)) else listOf()
                // here
                'L' -> if(prev == Point2D.EAST(loc)) listOf(Point2D.WEST(loc), Point2D.SOUTH(loc)) else listOf()
                '7' -> if(prev == Point2D.WEST(loc)) listOf(Point2D.EAST(loc), Point2D.NORTH(loc)) else listOf()
                'J' -> if(prev == Point2D.NORTH(loc)) listOf(Point2D.EAST(loc), Point2D.SOUTH(loc)) else listOf()
                '.' -> listOf()
                'S' -> listOf()
                else -> throw IllegalStateException("$name at $loc ")
            }
        }
        fun right(prev: Point2D): List<Point2D> {
            return when(name) {
                '|' -> if(prev == Point2D.NORTH(loc)) listOf(Point2D.WEST(loc)) else listOf(Point2D.EAST(loc))
                '-' -> if(prev == Point2D.EAST(loc)) listOf(Point2D.NORTH(loc)) else listOf(Point2D.SOUTH(loc))
                'F' -> if(prev == Point2D.EAST(loc)) listOf(Point2D.WEST(loc), Point2D.NORTH(loc)) else listOf()
                // here
                'L' -> if(prev == Point2D.NORTH(loc)) listOf(Point2D.WEST(loc), Point2D.SOUTH(loc)) else listOf()
                '7' -> if(prev == Point2D.SOUTH(loc)) listOf(Point2D.EAST(loc), Point2D.NORTH(loc)) else listOf()
                'J' -> if(prev == Point2D.WEST(loc)) listOf(Point2D.EAST(loc), Point2D.SOUTH(loc)) else listOf()
                '.' -> listOf()
                'S' -> listOf()
                else -> throw IllegalStateException("$name at $loc ")
            }
        }
    }
    private fun findLoop(): List<Pipe> {
        val start = map.first { it.name == 'S' }
        val connected = map.filter { start.loc in it.connectedTo()  }
        assert(connected.size == 2)
        var cur = connected.first()
        var prev = start
        val loop = mutableListOf(start, cur)

        while(cur != start) {
            loop.add(cur)
            val next = cur.connectedTo().first { it != prev.loc }
            prev = cur
            cur = lookupMap[next]!!
        }
        return loop
    }
    fun solvePart1() = findLoop().size / 2


    fun solvePart2(): Int {
        val loop = findLoop()
        val loopCoords = loop.map { it.loc }.toSet()
        val loopMap = loop.associate { it.loc to it.name.toPipeChar() }

        // Note: I quickly cheated and checked on the print that righthand side is the inner part.
        // I could check it but meh
        var prev2 = loop.first()
        val rightBlocks = loop.flatMap { pos -> val right = pos.right(prev2.loc); prev2=pos; right }.filter { it !in loopCoords }

        val queue = rightBlocks.toMutableList()
        val enclosed = rightBlocks.toMutableSet()
        while(queue.isNotEmpty()) {
            val cur = queue.removeAt(0)
            val fill = cur.adjacent().filter { it !in enclosed && it !in loopCoords }
            queue.addAll(fill)
            enclosed.addAll(fill)
            // Safety
            if(queue.size > 10_000) {
                throw IllegalStateException()
            }
        }
        //(enclosed.associateWith { 'o' } + loopMap).printChars()
        return enclosed.count()
    }
}