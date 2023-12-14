class Day14(val input: List<String>) {
    val map = input.flatMapIndexed{y, line -> line.mapIndexed{x, c -> Point2D(x, y) to c}.filter { it.second != '.' }}.toMap()
    val yRange = 0..< input.size
    val xRange = 0..< input.first().length
    val directions = mapOf("NORTH" to Point2D.NORTH, "WEST" to Point2D.WEST, "SOUTH" to Point2D.SOUTH, "EAST" to Point2D.EAST)

    fun roll(startMap: Map<Point2D, Char>, dir: String): Pair<Map<Point2D, Char>, Int> {
        val direction = directions[dir]!!
        val newMap = map.filter { it.value == '#' }.toMutableMap()
        var changes = 0
        var roundRocks = startMap.filter { it.value == 'O' }.keys.sortedBy { if(dir in listOf("WEST", "EAST")) it.x else it.y }
        if(dir in listOf("SOUTH", "EAST")) {
            roundRocks = roundRocks.reversed()
        }
        roundRocks.forEach { pos ->
            val newPoint = direction(pos).let {
                Point2D(x=it.x.coerceIn(xRange), y=it.y.coerceIn(yRange))
            }
            if (newMap.contains(newPoint)) {
                // spot is taken already
                newMap[pos] = 'O'
            } else {
                // roll north
                newMap[newPoint] = 'O'
                if(newPoint != pos) {
                    changes += 1
                }
            }
        }
        return newMap to changes
    }

    fun keepRolling(startMap: Map<Point2D, Char>, direction: String): Map<Point2D, Char> {
        var changes = 1
        var newMap = startMap
        while(changes != 0) {
            roll(newMap, direction).let {
                newMap = it.first
                changes = it.second
            }
        }
        //println("===== Rolling: ${direction}")
        //newMap.printChars()
        return newMap
    }

    fun solvePart1(): Int {
        val newMap = keepRolling(map, "NORTH")
        val maxY = newMap.maxOf { it.key.y }+1
        return newMap.filter { it.value == 'O' }.keys.sumOf { maxY - it.y }
    }
    fun solvePart2(): Int {
        val cycle = listOf("NORTH", "WEST", "SOUTH", "EAST")
        var newMap = map
        val cache = mutableListOf<Map<Point2D, Char>>()
        for(step in 0 ..< 10000000) {
            newMap = cycle.fold(newMap) { acc, dir -> keepRolling(acc, dir)}
            if(cache.contains(newMap)) {
                val prevRound = cache.indexOf(newMap)
                val cycle = step - prevRound
                val idx = prevRound + (1_000_000_000-1 - prevRound) % cycle
                println("idx: $idx")
                val maxY = newMap.maxOf { it.key.y }+1
                return cache[idx].filter { it.value == 'O' }.keys.sumOf { maxY - it.y }
            } else {
                cache.add(newMap)
            }
        }
        TODO()
    }

}