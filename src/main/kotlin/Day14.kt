class Day14(val input: List<String>) {
    val map = input.flatMapIndexed{y, line -> line.mapIndexed{x, c -> Point2D(x, y) to c}.filter { it.second != '.' }}.toMap()
    fun rollNorth(startMap: Map<Point2D, Char>, direction: DIRECTION): Pair<Map<Point2D, Char>, Int> {
        val newMap = map.filter { it.value == '#' }.toMutableMap()
        var changes = 0
        startMap.filter { it.value == 'O' }.toSortedMap().forEach { (pos, _) ->
            if(direction(pos).y < 0) {
                // at the edge
                newMap[pos] = 'O'
            } else if (newMap.contains(direction(pos))) {
                // spot is taken already
                newMap[pos] = 'O'
            } else {
                // roll north
                newMap[direction(pos)] = 'O'
                changes += 1
            }
        }
        return newMap to changes
    }

    fun keepRolling(startMap: Map<Point2D, Char>, direction: DIRECTION): Map<Point2D, Char> {
        var changes = 1
        var newMap = map
        while(changes != 0) {
            rollNorth(newMap, direction).let {
                newMap = it.first
                changes = it.second
            }
        }
        return newMap
    }

    fun solvePart1(): Int {
        var changes = 1
        var newMap = map
        while(changes != 0) {
            rollNorth(newMap, Point2D.NORTH).let {
                newMap = it.first
                changes = it.second
            }
        }
        val maxY = newMap.maxOf { it.key.y }+1
        return newMap.filter { it.value == 'O' }.keys.sumOf { maxY - it.y }
    }
    fun solvePart2(): Int {
        TODO()
    }

}