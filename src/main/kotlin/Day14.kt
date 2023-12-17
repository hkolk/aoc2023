class Day14(val input: List<String>) {
    val map = input.flatMapIndexed{y, line -> line.mapIndexed{x, c -> Point2D(x, y) to c}.filter { it.second != '.' }}.toMap()
    val blocks = map.filter { it.value == '#' }.map { it.key }.toSet()
    val yMax = input.size -1
    val xMax = input.first().length -1
    val directions = mapOf("NORTH" to Point2D.NORTH, "WEST" to Point2D.WEST, "SOUTH" to Point2D.SOUTH, "EAST" to Point2D.EAST)

    fun roll(startMap: Map<Point2D, Char>, dir: String): Pair<Map<Point2D, Char>, Int> {
        val direction = directions[dir]!!
        val newRoundRocks = mutableSetOf<Point2D>()
        val newMap = blocks.associateWith { '#' }.toMutableMap()
        var changes = 0
        var roundRocks = startMap.filter { it.value == 'O' }.keys.sortedBy { if(dir in listOf("WEST", "EAST")) it.x else it.y }
        if(dir in listOf("SOUTH", "EAST")) {
            roundRocks = roundRocks.reversed()
        }
        roundRocks.forEach { pos ->
            val newPoint = direction(pos).let {
                val x = it.x.coerceIn(0, xMax)
                val y = it.y.coerceIn(0, yMax)
                Point2D(x=x, y=y)
            }
            if (blocks.contains(newPoint) || newMap.contains(newPoint)) {
                // spot is taken already
                //newRoundRocks.add(pos)
                newMap[pos] = 'O'
            } else {
                // roll north
                //newRoundRocks.add(newPoint)
                newMap[newPoint] = 'O'
                if(newPoint != pos) {
                    changes += 1
                }
            }
        }
        //return (blocks.associateWith{ '#' } + newRoundRocks.associateWith {'O'}) to changes
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
                val maxY = newMap.maxOf { it.key.y }+1
                return cache[idx].filter { it.value == 'O' }.keys.sumOf { maxY - it.y }
            } else {
                cache.add(newMap)
            }
        }
        TODO()
    }

}