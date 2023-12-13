class Day13(val input: List<String>) {
    val maps = input.splitBy { it.length == 0 }.map { map ->
        map.flatMapIndexed{ y, line ->
            line.mapIndexed{ x, c -> Point2D(x+1, y+1) to c }.filter { it.second == '#' }.map { it.first }
        }
    }
    fun findRowReflection(map: List<Point2D>): Int? {
        val rows = map.groupBy { it.y }.map { it.key to it.value.map { it.x } }.toMap()
        println(rows)
        // check min
        val minY = rows.keys.min()
        val found = rows.firstNotNullOfOrNull { if(it.value == rows[minY] && it.key != minY) it.key else null }
        if(found != null) {
            val center = minY + (found - minY)/2
            println("Min-Y: $center")
            val matches = (0 .. center-found).map { println(it); rows[minY+it] == rows[found-it]}
            println(matches)
            if(matches.all { it }) {
                return center
            }
        }
        // check max
        val maxY = rows.keys.max()
        val found2 = rows.firstNotNullOfOrNull { if(it.value == rows[maxY] && it.key != maxY) it.key else null }
        if(found2 != null) {
            val center = found2 + (maxY - found2)/2
            println("Max-Y: $center")
            val matches = (0 .. center-found2).map { println(it); rows[found2+it] == rows[maxY-it]}
            println(matches)
            if(matches.all { it }) {
                return center
            }
        }
        return null
    }

    fun findColumnReflection(map: List<Point2D>): Int? {
        val columns = map.groupBy { it.x }.map { it.key to it.value.map { it.y } }.toMap()
        println(columns)
        // check min
        val minX = columns.keys.min()
        val found = columns.firstNotNullOfOrNull { if(it.value == columns[minX] && it.key != minX) it.key else null }
        if(found != null) {
            val center = minX + (found - minX)/2
            println("Min-X: $center")
            val matches = (0 .. center-found).map { println(it); columns[minX+it] == columns[found-it]}
            println(matches)
            if(matches.all { it }) {
                return center
            }
        }
        // check max
        val maxX = columns.keys.max()
        val found2 = columns.firstNotNullOfOrNull { if(it.value == columns[maxX] && it.key != maxX) it.key else null }
        if(found2 != null) {
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
        return maps.sumOf { map ->
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
        TODO()
    }
    fun solvePart2(): Int {
        TODO()
    }
}