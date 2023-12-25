class Day21(val input: List<String>) {
    val map = input.flatMapIndexed{y, line -> line.mapIndexed { x, c -> Point2D(x, y) to c  }}.toMap()
    val maxX = input.first().length-1
    val maxY = input.size-1
    val mapsize = input.size
    val origStart = map.filterValues { it == 'S' }.keys.first()

    fun fillSquare(start: Point2D, stepsLeft: Int, replacementMap: Map<Point2D, Char>?=null): Pair<Int, List<Pair<Int, Int>>> {
        if(stepsLeft < 0) {
            // shouldn't even have taken this first step..
            return 0 to listOf(0 to 0)
        }
        val map = replacementMap ?: map
        var edge = setOf(start)
        val counter = mutableMapOf(0 to setOf(start))
        for(step in 1.. stepsLeft) {
            edge = edge.flatMap{ cur ->
                val newPos = cur.adjacent().filter { map[it] in listOf('.', 'S')}.toList()
                newPos
            }.toSet()
            if(counter.values.contains(edge)) {
                //println("Found duplicate!")
                break
            }
            counter.put(step, edge)
        }
        val stepsCounter = counter.map{ (steps, edge) -> steps to edge.size}

        //stepsCounter.forEach { (steps, size)-> println("  $steps -> $size")}
        //edge.print()
        if(replacementMap != null && true) {
            val grouped = edge.groupBy {
                val moved = it.move(Point2D(mapsize*4, mapsize*4))
                Point2D(moved.x / mapsize, moved.y/mapsize)
            }.map { it.key to it.value.size }.toMap()
            //grouped.forEach { println("Quadrant ${it.key} = ${it.value}") }
            grouped.keys.minAndMaxOf { it.y }.toRange().forEach { y ->
                println("")
                grouped.keys.minAndMaxOf { it.x }.toRange().forEach { x ->
                    val alligned = (grouped[Point2D(x, y)]?:0).toString().padStart(3)
                    print(" [$alligned] ")
                }
                println("")
            }
        }
        if(replacementMap != null && false) {
            val toPrint = map.toMutableMap()
            toPrint.putAll(edge.map { it to '░' })
            toPrint.printChars()
        }
        return edge.size to stepsCounter
    }
    fun solvePart1(steps:Int=64): Int = fillSquare(origStart, steps).first


    fun solvePart2(): Int {
        /* for checking if the middle rows are traversable */
        //println(maxX/2)
        //val middlerow = map.filter { it.key.x == maxX/2 }.values.groupBy { it }.map { it.key to it.value.size }
        //println(middlerow)
        //TODO()
        val maps = mapOf(
            "C" to origStart,
            "W" to Point2D(origStart.x, maxY),
            "E" to Point2D(origStart.x, 0),
            "N" to Point2D(maxX, origStart.y),
            "S" to Point2D(0, origStart.y),
            "NW" to Point2D(maxX, maxY),
            "NE" to Point2D(0, maxY),
            "SE" to Point2D(0, 0),
            "SW" to Point2D(maxX, 0)
        )
        val filledMaps = maps.map { it.key to fillSquare(it.value, 5000) }.toMap()

        filledMaps.forEach{
            //println("${it.key}: ${it.value.first} - ${it.value.second.last()}")
        }



        //bigMap.printChars()
        println(((maxX+1) + (maxX/2)))
        //val steps = 196 // diamond with center covered and the edges partially. Count = 33652
        //val steps = 195 // diamond with center covered and the edges partially. Count = 33248
        //val steps = 197 // count = 34034 but not correct! We are missing the new west/east/sout/north
        //val steps = 131 // count = 15082
        //val steps = 22 // count = 529
        val steps = 44

        val toTheLeft = steps - ((mapsize-1)/2)
        println("other side: ${toTheLeft / mapsize}")
        println("other side Left: ${toTheLeft % mapsize}")
/*
        val count = listOf(filledMaps["C"]!!.second[mapsize-1-((steps)%2)].second,
                fillSquare(maps["W"]!!, steps - ((mapsize-1)/2) -1 ).first,
                fillSquare(maps["E"]!!, steps - ((mapsize-1)/2) -1 ).first,
                fillSquare(maps["S"]!!, steps - ((mapsize-1)/2) -1 ).first,
                fillSquare(maps["N"]!!, steps - ((mapsize-1)/2) -1 ).first,
                fillSquare(maps["NW"]!!, steps - ((mapsize-1)/2*2) -2 ).first,
                fillSquare(maps["NE"]!!, steps - ((mapsize-1)/2*2) -2 ).first,
                fillSquare(maps["SW"]!!, steps - ((mapsize-1)/2*2) -2 ).first,
                fillSquare(maps["SE"]!!, steps - ((mapsize-1)/2*2) -2 ).first
        )
        count.forEach { println("$it") }
        println(count.sum())
        */


        val centerToEdge = ((mapsize-1)/2)
        val centerToCorner = ((mapsize-1)/2*2)
        val verticalToEdge = mapsize
        val verticalToCorner = (mapsize+((mapsize-1)/2))
        val cornerToCorner = (mapsize*2)

        val count2 = mutableListOf<Pair<String, Int>>()
        // center
        count2 += if(steps > centerToCorner) {
            "Center-Full" to filledMaps["C"]!!.second[mapsize-1-((steps)%2)].second
        } else {
            "Center-partial" to fillSquare(maps["C"]!!, steps ).first
        }

        // vertical
        val straightLeft = steps - centerToEdge
        // full squares
        val fullSquares = ((straightLeft-verticalToCorner+verticalToEdge) / verticalToEdge).coerceAtLeast(0)
        println("fullSquares: $fullSquares ( (($straightLeft-$verticalToCorner+$verticalToEdge) / $verticalToEdge) )")

        val straightLeftMinusSquare = (straightLeft - (verticalToEdge*fullSquares))
        println("straightLeftMinusSquare: $straightLeftMinusSquare ($straightLeft - ($verticalToEdge*$fullSquares))")

        // full squares
        // TODO: Switch between even and uneven
        count2 += listOf("W", "E", "S", "N").map { "$it-full-$fullSquares" to filledMaps[it]!!.first * fullSquares }
        // far edge
        count2 += listOf("W", "E", "S", "N").map { "$it-semi-edge" to fillSquare(maps[it]!!, straightLeftMinusSquare - verticalToEdge - 1 ).first }
        // square before
        count2 += listOf("W", "E", "S", "N").map { "$it-edge" to fillSquare(maps[it]!!, straightLeftMinusSquare - 1 ).first }

        // diagonal
        val diagStraightLeft = steps - centerToCorner
        println("diagStraightLeft = $diagStraightLeft")
        val diagStraightLeftEdge = diagStraightLeft % cornerToCorner
        println("diagStraightLeftEdge = $diagStraightLeftEdge")

        val fulldiag = triangular(fullSquares)
        println("fulldiag: $fulldiag")

        count2 += listOf("NW", "NE", "SW", "SE").map { "$it-full-$fulldiag" to filledMaps[it]!!.first * fulldiag }

        count2 += listOf("NW", "NE", "SW", "SE").map { "$it-diag-straight-$fullSquares" to fillSquare(maps[it]!!, diagStraightLeftEdge - 2 ).first * fullSquares }


        println("Count2: ${count2.sumOf { it.second }}($count2)")

        val bigMap = map.flatMap { (pos, char) ->
            (Point2D.DIRECTIONS + Point2D.DIRECTIONSDIAG).map { pos.move(it, mapsize) to char } + (pos to char)
        }.toMap()

        val megaMap = bigMap.flatMap { (pos, char) ->
            (Point2D.DIRECTIONS + Point2D.DIRECTIONSDIAG).map { pos.move(it, mapsize*3) to char } + (pos to char)
        }.toMap()

        val asdf = fillSquare(origStart, steps, megaMap)
        println(asdf.first)
        // edge of map = from east to west: 11 (mapsize)
        //               from east to covered: 11+5 (mapsize+((mapsize-1)/2))
        //               from center to edge: 5 ((mapsize-1)/2)
        //               from center to corner: 10 ((mapsize-1)/2*2)
        //               from es to nw: 22 (mapsize*2)


        //println(filledMaps)

        //val center = fillSquare(origStart, 26501365)
        //println(center)
        TODO()
    }
    fun triangular(from: Int) = (0..< from).sum()
}