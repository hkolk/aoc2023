import java.util.*
import kotlin.math.abs

class Day17(val input: List<String>) {
    val map = input.flatMapIndexed{y, line -> line.mapIndexed{x, c -> Point2D(x, y) to c.digitToInt()}}.toMap()
    val yRange = map.keys.minAndMaxOf { it.y }.toRange()
    val xRange = map.keys.minAndMaxOf { it.x }.toRange()
    val start = Point2D(0, 0)
    val end = Point2D(input.first().length-1, input.size-1)

    private fun heuristic(a:Point2D, b:Point2D): Int {
        return abs(a.x - b.x) + abs(a.y - b.y)
    }

    private fun findShortestPath(start: Point2D, goal: Point2D, map: Map<Point2D, Int>, variant:Int=1): List<Stride>? {

        fun generatePath(currentPos: Stride, cameFrom: Map<Stride, Stride>): List<Stride> {
            val path = mutableListOf(currentPos)
            var current = currentPos
            while (cameFrom.containsKey(current)) {
                current = cameFrom.getValue(current)
                path.add(0, current)
            }
            return path.toList()
        }

        //val openVertices = mutableSetOf(start)
        val openVertices = PriorityQueue<Pair<Stride, Int>>(compareBy { it.second })
        val startStrides = (Stride(listOf(start), true).turns(variant) + Stride(listOf(start), false).turns(variant)).filter { it.within(xRange, yRange) }
        val costFromStart = mutableMapOf<Stride, Int>()
        val estimatedTotalCost = mutableMapOf<Stride, Int>()

        startStrides.forEach {
            openVertices.add(it to heuristic(it.path.last(), goal))
            costFromStart[it] = it.path.sumOf { map[it]!! }
            estimatedTotalCost[it] = heuristic(start, goal)
        }
        val closedVertices = mutableSetOf<Stride>()
        val cameFrom = mutableMapOf<Stride, Stride>()

        while(openVertices.isNotEmpty()) {
            val current = openVertices.remove().first
            if(current.path.last() == goal) {
                println("Cost from start: ${costFromStart[current]}")
                val path = generatePath(current, cameFrom)
                return path // First Route to finish will be optimum route
            }
            closedVertices.add(current)

            // next options =
            for(next in current.turns(variant).filter { it.within(xRange, yRange) }.filterNot { closedVertices.contains(it) }) {
                val newCost = costFromStart[current]!! + next.path.sumOf { map[it]!! }
                //val newCost = costFromStart[current]!! + 1
                if(!costFromStart.containsKey(next) || newCost < costFromStart[next]!!) {
                    costFromStart[next] = newCost
                    estimatedTotalCost[next] = newCost + heuristic(goal, next.path.last())
                    openVertices.add(next to (newCost + heuristic(goal, next.path.last())))
                    cameFrom[next] = current
                }
            }
            /*
            for(next in current.adjacent().filter { map.containsKey(it) && map[it]!! <= map[current]!! + 1 }.filterNot { closedVertices.contains(it) }) {
                val newCost = costFromStart[current]!! + 1
                if(!costFromStart.containsKey(next) || newCost < costFromStart[next]!!) {
                    costFromStart[next] = newCost
                    estimatedTotalCost[next] = newCost + heuristic(goal to map[goal]!!, next to map[next]!!)
                    openVertices.add(next to (newCost + heuristic(goal to map[goal]!!, next to map[next]!!)))
                    cameFrom[next] = current
                }
            }

             */
        }
        return null
    }

    data class Stride(val path: List<Point2D>, val vertical: Boolean) {

        fun within(xRange: IntRange, yRange: IntRange): Boolean {
            return path.last().let { it.y in yRange && it.x in xRange }
        }
        fun generateStridesV2(start: Point2D, direction: DIRECTION, vertical: Boolean): List<Stride> {
            val accu = mutableListOf<Point2D>()
            val ret = mutableListOf<List<Point2D>>()
            (1..10).forEach {i ->
                accu.add(start.move(direction, i))
                if(i >= 4) {
                    ret.add(accu.toList())
                }
            }
            return ret.map { Stride(it, vertical)}
        }
        fun generateStrides(start: Point2D, direction: DIRECTION, vertical: Boolean): List<Stride> {
            val accu = mutableListOf<Point2D>()
            val ret = mutableListOf<List<Point2D>>()
            (1..3).forEach {i ->
                accu.add(start.move(direction, i))
                ret.add(accu.toList())
            }
            return ret.map { Stride(it, vertical)}
        }
        fun turns(variant: Int): List<Stride> {
            val start = path.last()
            if(variant == 1) {
                return if (vertical) {
                    generateStrides(start, Point2D.EAST, false) + generateStrides(start, Point2D.WEST, false)
                } else {
                    generateStrides(start, Point2D.NORTH, true) + generateStrides(start, Point2D.SOUTH, true)
                }
            } else if(variant == 2) {
                return if (vertical) {
                    generateStridesV2(start, Point2D.EAST, false) + generateStridesV2(start, Point2D.WEST, false)
                } else {
                    generateStridesV2(start, Point2D.NORTH, true) + generateStridesV2(start, Point2D.SOUTH, true)
                }
            } else {
                throw IllegalStateException("Missing variant")
            }
        }
    }

    fun solvePart1(): Int {
        val result = findShortestPath(start, end, map)
        //(map.toList().map { it.first to it.second.toString().first() } + result!!.flatMap { it.path.map { it to '░'} }).toMap().printChars()
        val result2 = result!!.flatMap { it.path.map{ map[it]!!}}.sum()
        return result2
    }
    fun solvePart2(): Int {
        val result = findShortestPath(start, end, map, variant=2)
        //(map.toList().map { it.first to it.second.toString().first() } + result!!.flatMap { it.path.map { it to '░'} }).toMap().printChars()
        val result2 = result!!.flatMap { it.path.map{ map[it]!!}}.sum()
        return result2
    }
}