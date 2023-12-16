class Day16(val input: List<String>) {
    val grid = input.flatMapIndexed{ y, line -> line.mapIndexedNotNull { x, c -> Point2D(x, y) to c}}.toMap()

    val directions = mapOf("NORTH" to Point2D.NORTH, "WEST" to Point2D.WEST, "SOUTH" to Point2D.SOUTH, "EAST" to Point2D.EAST)

    data class Beam(val pos: Point2D, val dir: String)
    fun solve(startBeam: Beam): Int {
        var beams = setOf(startBeam)
        val covered = mutableSetOf<Beam>()
        var step = 0
        while(beams.isNotEmpty()) {
            beams = beams.flatMap { beam ->
                val next = directions[beam.dir]!!(beam.pos)
                when(grid[next]) {
                    null -> listOf()
                    '.' -> listOf(Beam(next, beam.dir))
                    '/' -> when(beam.dir) {
                        "EAST" -> listOf(Beam(next, "NORTH"))
                        "WEST" -> listOf(Beam(next, "SOUTH"))
                        "NORTH" -> listOf(Beam(next, "EAST"))
                        "SOUTH" -> listOf(Beam(next, "WEST"))
                        else -> throw IllegalStateException()
                    }
                    '\\' -> when(beam.dir) {
                        "EAST" -> listOf(Beam(next, "SOUTH"))
                        "WEST" -> listOf(Beam(next, "NORTH"))
                        "NORTH" -> listOf(Beam(next, "WEST"))
                        "SOUTH" -> listOf(Beam(next, "EAST"))
                        else -> throw IllegalStateException()
                    }
                    '|' -> if(beam.dir in listOf("WEST", "EAST"))
                            listOf(Beam(next, "NORTH"), Beam(next, "SOUTH"))
                        else
                            listOf(Beam(next, beam.dir))
                    '-' -> if(beam.dir in listOf("NORTH", "SOUTH"))
                            listOf(Beam(next, "EAST"), Beam(next, "WEST"))
                        else
                            listOf(Beam(next, beam.dir))
                    else -> throw IllegalStateException()
                }
            }.filter { it !in covered }.toSet()
            if(!covered.addAll(beams)) {
                return covered.map { it.pos }.toSet().size
            }

            /*
            step++
            if(step % 100 == 0 ) {
                //println("Step: $step")
                //println("Beams: ${beams.size}")
                //println("Covered: ${covered.size}")
                //beams.forEach { println(it) }
                //covered.map { it.pos }.print()
            }

             */
        }
        TODO()
    }

    fun solvePart1() = solve(Beam(Point2D(-1, 0), "EAST"))

    fun solvePart2(): Int {
        val startBeams = input.indices.flatMap { y ->
            listOf(Beam(Point2D(input.first().length, y), "WEST"), Beam(Point2D(-1, y), "EAST"), )
        } + input.first().indices.flatMap { x ->
            listOf(Beam(Point2D(x, -1), "SOUTH"), Beam(Point2D(x, input.size), "NORTH"))
        }
        return startBeams.maxOf { solve(it) }
    }
}