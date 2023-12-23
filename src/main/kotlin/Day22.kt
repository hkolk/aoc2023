import kotlin.math.min

class Day22(val input: List<String>) {

    val bricks = input.mapIndexed {id, line -> line.splitIgnoreEmpty("~", ",").map{it.toInt()}.let {
        Brick(id, Point3D(it[0], it[1], it[2]), Point3D(it[3], it[4], it[5]))
    } }.sortedBy { min(it.begin.z, it.end.z) }

    val stable = mutableMapOf<Point3D, Int>()
    val connections = mutableSetOf<Pair<Int, Int>>()
    val settled = mutableListOf<Brick>()
    init {
        bricks.forEach { brickDONOTUSE ->
            // landed?
            var brickMutable = brickDONOTUSE
            while (!(brickMutable.contents.any { it.z <= 1 || it.copy(z = it.z - 1) in stable })) {
                brickMutable = brickMutable.lower()
            }
            val landedOn = brickMutable.contents.map { it.copy(z = it.z - 1) }.mapNotNull { stable[it] }
                .map { it to brickMutable.id }
            connections.addAll(landedOn)
            // only add it to stable/settled after we counted connections
            stable.putAll(brickMutable.contents.map { it to brickMutable.id })
            settled.add(brickMutable.copy())

        }
    }

    val supports = connections.groupBy { it.first }.map { it.key to it.value.map { it.second }.toSet() }.toMap()
    val supportedBy = connections.groupBy { it.second }.map { it.key to it.value.map { it.first }.toSet() }.toMap()


    data class Brick(val id: Int, val begin: Point3D, val end: Point3D) {
        val contents = begin.allPointsTo(end)

        fun lower() = Brick(id, begin.copy(z=begin.z-1), end.copy(z=end.z-1))

    }
    fun solvePart1(): Int {

        val freeStanding = settled.filter {brick ->
            if((supports[brick.id]?.size ?: 0) > 0) {
                supports[brick.id]!!.all { supportedBy[it]!!.size > 1}
            } else {
                true
            }
        }
        return freeStanding.size

    }

    fun disintegrate(id: Int): Int {
        val queue = mutableListOf<Int>()
        val removed = mutableSetOf(id)
        var ret = 0
        queue.addAll(supports[id]?: listOf())
        while(queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if(next !in removed) {
                if (supportedBy[next]!!.all { it in removed }) {
                    // no support left, so yes
                    // add chidldren to the queue if so
                    removed.add(next)
                    queue.addAll(supports[next] ?: listOf())
                    ret += 1
                } // else: still stablised by something else
            }
        }
        return ret
    }

    fun solvePart2() = bricks.sumOf { disintegrate(it.id) }
}