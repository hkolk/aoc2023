class Day24(val input: List<String>) {
    val lines = input.mapIndexed { idx, line -> line.splitIgnoreEmpty(" ", ",", "@").let {
        Line(idx, Point3DWide(it[0].toLong(), it[1].toLong(), it[2].toLong()), Point3DWide(it[3].toLong(), it[4].toLong(), it[5].toLong()))
    } }


    data class Point3DWide(val x: Long, val y: Long, val z: Long) {
        fun move(direction: Point3D, times: Int=1): Point3DWide = move( { Point3DWide(it.x+direction.x, it.y + direction.y, it.z + direction.z) }, times)

        fun move(direction: Point3DWide, times: Int=1): Point3DWide {
            if(times < 0) {
                return move({ Point3DWide(it.x - direction.x, it.y - direction.y, it.z - direction.z) }, -times)
            } else {
                return move({ Point3DWide(it.x + direction.x, it.y + direction.y, it.z + direction.z) }, times)
            }
        }

        fun move(direction: (Point3DWide) -> Point3DWide, times: Int=1): Point3DWide {
            assert(times > 0)
            return direction.repeated(times).fold(this) { acc, func -> func(acc) }
        }
        fun toDouble() = Point3DDouble(x.toDouble(), y.toDouble(), z.toDouble())
        fun diff(other: Point3DWide): Point3DWide {
            return Point3DWide(other.x - this.x, other.y - this.y, other.z - this.z )
        }

        fun divideByOrNull(interval: Int): Point3DWide? {
            if(x % interval != 0L || y % interval != 0L || z % interval != 0L) {
                return null
            }
            return Point3DWide(x/interval, y/interval, z/interval)

        }
    }

    data class Point3DDouble(val x: Double, val y: Double, val z: Double) {

    }
    data class Line(val id: Int, val position: Point3DWide, val direction: Point3DWide) {

        fun isPointInFuture(x: Double): Boolean {
            if(x > position.x) {
                return direction.x > 0
            } else {
                return direction.x <= 0
            }
        }


        fun intersect2D(other: Line): Pair<Double, Double>? {
            //lines.forEach { println(it) }
            val A = this.position.toDouble()
            val B = this.position.move(this.direction).toDouble()
            val C = other.position.toDouble()
            val D = other.position.move(other.direction).toDouble()
            val a1 = B.y - A.y
            val b1 = A.x - B.x
            val c1 = a1 * A.x + b1*A.y
            //val c1 = addExact(multiplyExact(a1, A.x), multiplyExact(b1, A.y))
            //println("Line 1: $a1, $b1, $c1")

            val a2 = D.y - C.y
            val b2 = C.x - D.x
            val c2 = a2 * C.x + b2 * C.y
            //val c2 = addExact(multiplyExact(a2, C.x), multiplyExact(b2, C.y))
            //println("Line 2: $a2, $b2, $c2")

            val determinant = a1*b2 - a2*b1 //multiplyExact(a1, b2) - multiplyExact(a2, b1)
            //println("Deteriminant: $determinant")
            if(determinant == 0.0) {
                return null
            }

            val isectX = (b2*c1 - b1*c2)/determinant
            val isectY = (a1*c2 - a2*c1)/determinant
            //println("Isect: $isectX, $isectY")
            return isectX to isectY

        }

        fun futurePoints(seconds: Int): Sequence<Point3DWide> {
            return sequence {
                var cur = position
                repeat(seconds) {
                    yield(cur)
                    cur = cur.move(direction)
                }
            }
        }

        fun intersect3D(line: Day24.Line): Point3DDouble? {


        }
    }
    fun solvePart1(box: ClosedFloatingPointRange<Double>): Int {
        var counter = 0
        lines.combinations(2).forEach {
            val isect = it[0].intersect2D(it[1])
            if(isect == null) {
                //println("Line ${it[0].id} and ${it[1].id}: parallel")
            } else {
                if(isect.first in box && isect.second in box) {
                    if(it[0].isPointInFuture(isect.first) && it[1].isPointInFuture(isect.first)) {
                        //println("Line ${it[0].id} and ${it[1].id}: $isect (IN BOX AND FUTURE!)")
                        counter++
                    } else {
                        //println("Line ${it[0].id} and ${it[1].id}: $isect (in box but not future)")
                    }
                } else {
                    //println("Line ${it[0].id} and ${it[1].id}: $isect")
                }
            }
        }
        return counter

        TODO()
    }
    fun solvePart2(): Int {
        // find possible lines in 300 seconds
        //val lineA = Line(0, Point3DWide(0, 0, 0), Point3D(1, 1, 0))
        //val lineB = Line(0, Point3DWide(0, 2, 0), Point3D(1, 1, 0))

        val pointsA = lines[0].futurePoints(10).toList()
        val pointsB = lines[1].futurePoints(10).toList()
        val pointsC = lines[2].futurePoints(10).toList()

        val options = pointsA.flatMapIndexed { aSec, aPos ->
            pointsB.mapIndexedNotNull{ bSec, bPos ->
                if(aSec == bSec) {
                    null
                } else {
                    val diff = aPos.diff(bPos)
                    val ret = diff.divideByOrNull(bSec - aSec)
                    if(ret != null) {
                        (aSec to bSec) to ret
                    } else {
                        null
                    }
                }
            }
        }
        options.forEach { println(it) }

        //val asdf = options.first().first()
        //val asdf2 = pointsA[asdf.first.first].move(asdf.second, asdf.first.second - asdf.first.first)
        //println("$asdf2 == ${pointsB[asdf.first.second]}")

        options.forEach { (secs, diff) ->
            val asdf = pointsA[secs.first].move(diff, secs.second - secs.first)
            println("$asdf == ${pointsB[secs.second]}")
        }
        val lines = options.map {
            Line(0, pointsA[it.first.first]!!, it.second)
        }
        lines.filter {
            it.intersect3D(lines[2])
        }
        TODO()
    }
}