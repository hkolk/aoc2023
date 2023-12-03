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
    fun solvePart1(): Int {
        val found = mutableListOf<Int>()
        input.forEachIndexed {y, line ->
            // find number and it's locations
            var x = 0;
            var acc = ""
            var coords = mutableSetOf<Point2D>()
            while(x < line.length) {
                if(line[x].isDigit()) {
                    acc += line[x]
                    coords.add(Point2D(x, y))
                } else {
                    if(acc != "") {
                        if(adjacentToSymbol(coords)) {
                            found.add(acc.toInt())
                        }
                        acc = ""
                        coords = mutableSetOf<Point2D>()
                    }
                }
                x++
            }
            if(acc != "") {
                if(adjacentToSymbol(coords)) {
                    found.add(acc.toInt())
                }
            }

        }
        map.printChars()
        println(found)
        return found.sum()
        TODO()
    }
    fun solvePart2(): Int {
        TODO()
    }
}