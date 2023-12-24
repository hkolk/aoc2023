class Day24(val input: List<String>) {
    val lines = input.mapIndexed { idx, line -> line.splitIgnoreEmpty(" ", ",", "@").let {
        Line(idx, Point3DWide(it[0].toLong(), it[1].toLong(), it[2].toLong()), Point3DWide(it[3].toLong(), it[4].toLong(), it[5].toLong()))
    } }




    data class Line(val id: Int, val position: Point3DWide, val direction: Point3DWide) {

        fun isPointInFuture(x: Double): Boolean {
            if(x > position.x) {
                return direction.x > 0
            } else {
                return direction.x <= 0
            }
        }

        fun next(): Point3DWide {
            return this.position.move(direction)
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

        fun intersect3D(other: Line): Point3DDouble? {
            val xAndYIsect = intersect(position.getXY(), next().getXY(), other.position.getXY(), other.next().getXY())
            if(xAndYIsect == null) {
                return null
            }
            //println("X + Y: $xAndYIsect")
            val xAndZIsect = intersect(position.getXZ(), next().getXZ(), other.position.getXZ(), other.next().getXZ())
            if(xAndZIsect == null || xAndYIsect.x != xAndZIsect.x) {
                return null
            }
            //println("X + Z: $xAndYIsect")
            return Point3DDouble(xAndYIsect.x, xAndYIsect.y, xAndZIsect.y)

        }
        fun intersect(a: Point2DWide, b: Point2DWide, p: Point2DWide, q:Point2DWide): Point2DDouble? {
            //lines.forEach { println(it) }
            val A = a.toDouble()
            val B = b.toDouble()
            val C = p.toDouble()
            val D = q.toDouble()
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
            return Point2DDouble(isectX, isectY)
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
    }
    fun solvePart2(): Long {
        // find possible lines in 300 seconds
        //val lineA = Line(0, Point3DWide(0, 0, 0), Point3D(1, 1, 0))
        //val lineB = Line(0, Point3DWide(0, 2, 0), Point3D(1, 1, 0))

        val pointsA = lines[0].futurePoints(100).toList()
        val pointsB = lines[1].futurePoints(100).toList()

        val options = pointsA.flatMapIndexed { aSec, aPos ->
            if(aSec % 100 == 0) {
                println("aSec: $aSec")
            }
            pointsB.pmapIndexed{ bSec, bPos ->
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
            }.filterNotNull()
        }
        println("Options: ${options.size}")
        //options.forEach { println(it) }

        //val asdf = options.first().first()
        //val asdf2 = pointsA[asdf.first.first].move(asdf.second, asdf.first.second - asdf.first.first)
        //println("$asdf2 == ${pointsB[asdf.first.second]}")

        /*
        options.forEach { (secs, diff) ->
            val asdf = pointsA[secs.first].move(diff, secs.second - secs.first)
            println("$asdf == ${pointsB[secs.second]}")
        }

         */
        val optionLines = options.map {
            Line(0, pointsA[it.first.first]!!.move(it.second, -it.first.first), it.second)
        }
        val newLines = optionLines.filter {option ->
            lines.all {
                val intersects = option.intersect3D(it)
                intersects != null && it.isPointInFuture(intersects.x)
            }
        }

        newLines.forEach {line ->
            //println("Line: $line")
            lines.forEach {
                val isect = it.intersect3D(line)

                println("  Isect: $isect")
            }
        }
        println(newLines)
        val winner = newLines.first()
        return winner.position.x + winner.position.y + winner.position.z
        TODO()
    }

}