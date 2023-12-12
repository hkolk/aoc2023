class Day11(val input: List<String>) {

    fun Pair<Int, Int>.toRange() = this.first..this.second
    fun solvePart1() = solvePart2(2)
    fun solvePart2(resize: Int): Long {
        val map = input.flatMapIndexed { y, line -> line.mapIndexed{x, c -> Point2D(x, y) to c}}.filter { it.second == '#' }.map{it.first}
        //map.print()
        val newMap = mutableListOf<Point2D>()
        var yIncrement = 0
        for(y in map.minAndMaxOf { it.y }.toRange()) {
            val onY = map.filter { it.y == y }
            if(onY.isEmpty()) {
                // empty Y
                yIncrement += 1
            } else {
                newMap.addAll(onY.map { it.copy(x=it.x, y = y+(resize * yIncrement - yIncrement)) })
            }
        }
        val newestMap = mutableListOf<Point2D>()
        var xIncrement = 0
        for(x in newMap.minAndMaxOf { it.x }.toRange()) {
            val onX = newMap.filter { it.x == x }
            if(onX.isEmpty()) {
                // empty X
                xIncrement += 1
            } else {
                newestMap.addAll(onX.map { it.copy(y=it.y, x = x+(resize * xIncrement - xIncrement)) })
            }
        }
        //println("x: $xIncrement, y: $yIncrement")
        val pairs = newestMap.combinations(2)
        return pairs.sumOf {
            //println("${it[0]} to ${it[1]} -> ${it[0].distance(it[1])}")
            it[0].distance(it[1]).toLong()
        }

    }
}