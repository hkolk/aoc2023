class Day13(val input: List<String>) {
    val maps = input.splitBy { it.length == 0 }.map { map ->
        map.flatMapIndexed{ y, line ->
            line.mapIndexed{ x, c -> Point2D(x+1, y+1) to c }.filter { it.second == '#' }.map { it.first }
        }
    }
    fun findRowReflection(map: List<Point2D>, mustContain: Point2D? = null): Int? {
        val rows = map.groupBy { it.y }.map { it.key to it.value.map { it.x } }.toMap()
        println(rows)
        // check min
        val minY = rows.keys.min()
        val matchesMin = rows.filter { it.value == rows[minY] && it.key != minY }
        //if(matchesMin.size > 1 ) { TODO("Double Matches!") }
        val results = matchesMin.map { it.key }.mapNotNull { found ->
            val center = minY + (found - minY)/2
            println("Min-Y: $center")
            val matches = (0 .. found-center).map { println(it); rows[minY+it] == rows[found-it]}
            println(matches)
            println((0 .. found-center))
            if(matches.all { it }) {
                center to matches.size
            } else {
                null
            }
        }
        if(results.size > 0) {
            println(results)
            return results.maxBy { it.second }.first
        }
        // check max
        val maxY = rows.keys.max()
        val matchesMax = rows.filter { it.value == rows[maxY] && it.key != maxY }
        //if(matchesMax.size > 1 ) { TODO("Double Matches!") }
        matchesMax.map { it.key }.forEach { found2 ->
            val center = found2 + (maxY - found2) / 2
            println("Max-Y: $center")
            val matches = (0..center - found2).map { println(it); rows[found2 + it] == rows[maxY - it] }
            println(matches)
            if (matches.all { it }) {
                if(mustContain == null || mustContain.y >= found2) {
                    return center
                }
            }
        }
        return null
    }

    fun findColumnReflection(map: List<Point2D>, mustContain: Point2D? = null): Int? {
        val columns = map.groupBy { it.x }.map { it.key to it.value.map { it.y } }.toMap()
        println(columns)
        // check min
        val minX = columns.keys.min()
        val matchesMin = columns.filter { it.value == columns[minX] && it.key != minX }
        //if(matchesMin.size > 1 ) { TODO("Double Matches!") }
        matchesMin.map { it.key }.forEach { found ->
            val center = minX + (found - minX)/2
            println("Min-X: $center")
            val matches = (0 .. found - center).map { println(it); columns[minX+it] == columns[found-it]}
            println(matches)
            if(matches.all { it }) {
                return center
            }
        }
        // check max
        val maxX = columns.keys.max()
        val matchesMax = columns.filter { it.value == columns[maxX] && it.key != maxX }
        //if(matchesMax.size > 1 ) { TODO("Double Matches!") }
        matchesMax.map { it.key }.forEach { found2 ->
            val center = found2 + (maxX - found2)/2
            println("Max-X: $center")
            val matches = (0 .. center-found2).map { println(it); columns[found2+it] == columns[maxX-it]}
            println(matches)
            if(matches.all { it }) {
                return center
            }
        }
        return null
    }
    fun solvePart1(): Int {
        val results = maps.map { map ->
            map.print()
            val vert = findColumnReflection(map)
            if(vert != null) {
                vert
            } else {
                val hor = findRowReflection(map)
                if(hor != null) {
                    hor * 100
                } else {
                    throw IllegalStateException("No reflection found")
                }
            }
        }
        results.forEach { println("Score: $it") }
        return results.sum()
    }
    infix fun <T> Set<T>.xor(that: Set<T>): Set<T> = (this - that) + (that - this)

    fun solvePart2(): Int {
        val results = maps.map { map ->
            var score = 0
            map.minAndMaxOf { it.y }.toRange().map { y ->
                map.minAndMaxOf { it.x }.toRange().map { x ->
                    val newMap = if(map.contains(Point2D(x, y))) {
                        map.filter { it != Point2D(x, y) }
                    } else {
                        map + listOf(Point2D(x, y))
                    }
                    println(map.toSet() xor newMap.toSet())
                    newMap.print()
                    val vert = findColumnReflection(newMap, Point2D(x, y))
                    if(vert != null) {
                        score = vert
                    } else {
                        val hor = findRowReflection(newMap, Point2D(x, y))
                        if(hor != null) {
                            score = hor * 100
                        }
                    }
                }
            }
            map.print()
            val vert = findColumnReflection(map)
            if(vert != null) {
                vert
            } else {
                val hor = findRowReflection(map)
                if(hor != null) {
                    hor * 100
                } else {
                    throw IllegalStateException("No reflection found")
                }
            }
        }
        results.forEach { println("Score: $it") }
        return results.sum()    }
}