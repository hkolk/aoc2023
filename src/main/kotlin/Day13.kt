class Day13(input: List<String>) {
    private val maps = input.splitBy { it.isEmpty() }.map { map ->
        map.flatMapIndexed{ y, line ->
            line.mapIndexed{ x, c -> Point2D(x+1, y+1) to c }.filter { it.second == '#' }.map { it.first }
        }
    }

    private fun findColumnReflection(map: List<Point2D>, mustContain: Point2D? = null): Int? {
        val columns = map.groupBy { it.x }.map { it.key to it.value.map { it.y } }.toMap()

        // check min
        val minX = columns.keys.min()
        val matchesMin = columns.filter { it.value == columns[minX] && it.key != minX }
        matchesMin.map { it.key }.forEach { found ->
            val center = minX + (found - minX)/2
            val matches = (0 .. found - center).map { columns[minX+it] == columns[found-it]}
            if(matches.all { it } && (found - minX) % 2 == 1) {
                if(mustContain == null || mustContain.x <= found) {
                    return center
                }
            }
        }

        // check max
        val maxX = columns.keys.max()
        val matchesMax = columns.filter { it.value == columns[maxX] && it.key != maxX }
        matchesMax.map { it.key }.forEach { found2 ->
            val center = found2 + (maxX - found2)/2
            val matches = (0 .. center-found2).map { columns[found2+it] == columns[maxX-it]}
            if(matches.all { it } && (maxX - found2) % 2 == 1) {
                if(mustContain == null || mustContain.x >= found2) {
                    return center
                }
            }
        }
        return null
    }
    fun solvePart1(): Int {
        val results = maps.map { map ->
            val vert = findColumnReflection(map)
            if(vert != null) {
                vert
            } else {
                val hor = findColumnReflection(map.map { Point2D(it.y, it.x)   })
                if(hor != null) {
                    hor * 100
                } else {
                    throw IllegalStateException("No reflection found")
                }
            }
        }
        return results.sum()
    }

    fun solvePart2(): Int {
        val results = maps.map { map ->
            var score = 0
            map.minAndMaxOf { it.y }.toRange().map { y ->
                map.minAndMaxOf { it.x }.toRange().map { x ->
                    var innerScore = 0
                    val newMap = if(map.contains(Point2D(x, y))) {
                        map.filter { it != Point2D(x, y) }
                    } else {
                        map + listOf(Point2D(x, y))
                    }
                    val vert = findColumnReflection(newMap, Point2D(x, y))
                    if(vert != null) {
                        innerScore = vert
                    } else {
                        val hor = findColumnReflection(newMap.map { Point2D(it.y, it.x) }, Point2D(y, x))
                        if(hor != null) {
                            innerScore = hor * 100
                        }
                    }
                    if(innerScore > 0) {
                        score = innerScore
                    }
                }
            }
            score
        }
        return results.sum()
    }
}