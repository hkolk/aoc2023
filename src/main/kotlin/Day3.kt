class Day3(val input: List<String>) {
    val map = input.flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x, c -> Point2D(x, y) to c}
    }.toMap()

    fun adjacentToSymbol(coords: Set<Point2D>): Boolean {
        return coords.any{coord ->
            (coord.diag() + coord.adjacent()).any {
                map.contains(it) && map[it] != '.' && !map[it]!!.isDigit()
            }
        }
    }
    fun solvePart1() = findNumbers().sumOf { it.first }
    fun solvePart2(): Long {
        val found = findNumbers()
        val gears = map.filter { it.value == '*' }.map { (point, _) ->
            val surroundingNumbers = found.filter {numLoc ->  numLoc.second.any {
                    it.surrounding().any{
                        it == point
                    }
                }
            }.map { it.first }
            if(surroundingNumbers.size == 2) {
                surroundingNumbers.multiply()
            } else {
                0
            }
        }
        return gears.sum()
    }

    private fun findNumbers(): MutableList<Pair<Int, Set<Point2D>>> {
        val found = mutableListOf<Pair<Int, Set<Point2D>>>()
        input.forEachIndexed { y, line ->
            // find number and it's locations
            var acc = ""
            var coords = mutableSetOf<Point2D>()
            line.forEachIndexed { x, c ->
                if (c.isDigit()) {
                    acc += c
                    coords.add(Point2D(x, y))
                } else {
                    if (acc != "") {
                        if (adjacentToSymbol(coords)) {
                            found.add(acc.toInt() to coords)
                        }
                        acc = ""
                        coords = mutableSetOf()
                    }
                }
            }
            if (acc != "") {
                if (adjacentToSymbol(coords)) {
                    found.add(acc.toInt() to coords)
                }
            }

        }
        return found
    }
}