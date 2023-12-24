import com.microsoft.z3.Context
import com.microsoft.z3.Status

class Day24(val input: List<String>) {
    val lines = input.mapIndexed { idx, line -> line.splitIgnoreEmpty(" ", ",", "@").let {
        Line(idx, Point3DWide(it[0].toLong(), it[1].toLong(), it[2].toLong()), Point3DWide(it[3].toLong(), it[4].toLong(), it[5].toLong()))
    } }




    data class Line(val id: Int, val position: Point3DWide, val direction: Point3DWide) {

        fun isPointInFuture(point: Point3DDouble, ignoreZ: Boolean=false): Boolean {
            if(point.x > position.x) {
                return direction.x > 0
            } else if(point.x < position.x) {
                return direction.x < 0
            } else {
                if(point.y > position.y) {
                    return position.y > 0
                } else if(point.y < position.y) {
                    return direction.y < 0
                } else {
                    if(point.z > position.z && !ignoreZ) {
                        return position.z > 0
                    } else if(point.z < position.z && !ignoreZ) {
                        return direction.z < 0
                    } else {
                        return true
                    }
                }
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
                    if(it[0].isPointInFuture(Point3DDouble(isect.first, isect.second, 0.0), true) && it[1].isPointInFuture(Point3DDouble(isect.first, isect.second, 0.0), true)) {
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
    fun solvePart2_old(): Long {
        // find possible lines in 300 seconds
        //val lineA = Line(0, Point3DWide(0, 0, 0), Point3D(1, 1, 0))
        //val lineB = Line(0, Point3DWide(0, 2, 0), Point3D(1, 1, 0))

        val pointsA = lines[0].futurePoints(100_0000).toList()
        val pointsB = lines[1].futurePoints(100_000).toList()

        val options = pointsA.forEachIndexed { aSec, aPos ->
            if(aSec % 100 == 0) {
                println("aSec: $aSec")
            }
            pointsB.forEachIndexed{ bSec, bPos ->
                if(aSec != bSec) {
                    val diff = aPos.diff(bPos)
                    val ret = diff.divideByOrNull(bSec - aSec)
                    if(ret != null && ret.x in -500..500 && ret.y in -500..500 && ret.z in -500..500) {
                        (aSec to bSec) to ret
                        val option = Line(0, pointsA[aSec]!!.move(ret, -aSec), ret)
                        val winner = lines.all {
                            val intersects = option.intersect3D(it)
                            intersects != null && it.isPointInFuture(intersects)
                        }
                        if(winner) {
                            val answer = option.position.x + option.position.y + option.position.z
                            println("Answer: $answer")

                        }
                    }
                }
            }
        }
        TODO()
    }

    fun yieldVelocities(range: LongRange, xInvalid: Set<Long>, yInvalid: Set<Long>, zInvalid: Set<Long>) = sequence {
        (range).forEach { x ->
            if(x !in xInvalid) {
                range.forEach { y ->
                    if(y !in yInvalid) {
                        range.forEach { z ->
                            if(z !in zInvalid) {
                                yield(Point3DWide(x, y, z))
                            }
                        }
                    }
                }
            }
        }
    }

    // Based on David Sharick's python code:
    // https://gitlab.com/davidsharick/advent-of-code-2023/-/blob/main/day24/day24.py
    // Used the below Z3 Solver to get the answer to help with debugging
    fun solvePart2(): Long {
        val xInvalid = mutableSetOf<Long>()
        val yInvalid = mutableSetOf<Long>()
        val zInvalid = mutableSetOf<Long>()

        lines.forEach { lineA ->
            lines.forEach { lineB ->
                if(lineA != lineB) {
                    if(lineA.position.x > lineB.position.x && lineA.direction.x > lineB.direction.x) {
                        xInvalid.addAll((lineB.direction.x..lineA.direction.x))
                    }
                    if(lineA.position.y > lineB.position.y && lineA.direction.y > lineB.direction.y) {
                        yInvalid.addAll((lineB.direction.y..lineA.direction.y))
                    }
                    if(lineA.position.z > lineB.position.z && lineA.direction.z > lineB.direction.z) {
                        zInvalid.addAll((lineB.direction.z..lineA.direction.z))
                    }
                }
            }
        }

        //val possibleVelocities = (range).flatMap {x -> range.flatMap { y -> range.map { z -> Point3DWide(x, y, z) } }   }
        val range = -500L..500L
        //listOf(Point3DWide(x=107, y=-114, z=304)).forEach {  option ->
        yieldVelocities(range, xInvalid, yInvalid, zInvalid).forEach {option ->
            val linesNew = lines.map { Line(it.id, it.position, it.direction - option) }
            val lineA = linesNew[0]
            val lineB = linesNew[1]
            //val isect = lineA.intersect3D(lineB)
            //println(isect)
            //println(lineA)
            //println(lineB)
            //println(lineA.intersect2D(lineB).let { it!!.first.toLong() to it!!.second.toLong() })
            val isect = lineA.intersect2D(lineB)
            //println(isect!!.second % 1 == 0.0)
            //println(isect!!.second % 1)
            if(isect != null && lineA.isPointInFuture(Point3DDouble(isect.first, isect.second, 0.0), true)) {
                val isectLong = Point3DDouble(isect.first, isect.second, 0.0).toLong()
                val t0 = (isectLong.x - lineA.position.x) / lineA.direction.x
                val t1 = (isectLong.x - lineB.position.x) / lineB.direction.x
                val z0 = lineA.position.z + (lineA.direction.z * t0)
                val z1 = lineB.position.z + (lineB.direction.z * t1)
                //println("$z0, $z1")
                if(z0 == z1) {
                    //val goal = Point3DWide(isectLong.x, isectLong.y, z0)
                    val goal = Point3DWide(isectLong.x, isectLong.y, z0)
                    var debug = false
                    //    // x=242369545669096, y=339680097675927, z=102145685363875, vx=107, vy=-114, vz=304
                    if (goal == Point3DWide(242369545669096, 339680097675927, 102145685363875)) {
                        println("At least its a goal")
                        debug = true
                    }
                    //println("$option, $isectLong,  ---- $t0 - $t1 - $z0 - $z1")
                    var good = true
                    linesNew.drop(2).forEach { lineC ->
                        if (goal.x == lineC.position.x && lineC.direction.x == 0L) {
                            // continue
                        } else if (lineC.direction.x == 0L || (goal.x - lineC.position.x) % lineC.direction.x != 0L) {
                            if (debug) {
                                println("Failed the X test: ${(goal.x - lineC.position.x) % lineC.direction.x}")
                                println("(${goal.x} - ${lineC.position.x}) % ${lineC.direction.x}")
                            }
                            good = false
                        }
                        if (goal.y == lineC.position.y && lineC.direction.y == 0L) {
                            // continue
                        } else if (lineC.direction.y == 0L || (goal.y - lineC.position.y) % lineC.direction.y != 0L) {
                            if (debug) {
                                println("Failed the Y test")
                            }

                            good = false
                        }
                        if (goal.z == lineC.position.z && lineC.direction.z == 0L) {
                            // continue
                        } else if (lineC.direction.z == 0L || (goal.z - lineC.position.z) % lineC.direction.z != 0L) {
                            if (debug) {
                                println("Failed the Z test")
                            }

                            good = false
                        }
                    }
                    if (good) {
                        println("Found: $goal")
                        return goal.x + goal.y + goal.z
                    }
                }
            }
        }
        TODO()
    }

    fun solvePart2_solver(): Any {
        val hail = input.map { it.split(" @ ", ", ").map { it.trim().toLong() } }
        val ctx = Context()
        val solver = ctx.mkSolver()
        val mx = ctx.mkRealConst("mx")
        val my = ctx.mkRealConst("my")
        val mz = ctx.mkRealConst("mz")
        val mxv = ctx.mkRealConst("mxv")
        val myv = ctx.mkRealConst("myv")
        val mzv = ctx.mkRealConst("mzv")
        repeat(3) {
            val (sx, sy, sz, sxv, syv, szv) = hail[it]
            val t = ctx.mkRealConst("t$it")
            solver.add(ctx.mkEq(ctx.mkAdd(mx, ctx.mkMul(mxv, t)), ctx.mkAdd(ctx.mkReal(sx.toString()), ctx.mkMul(ctx.mkReal(sxv.toString()), t))))
            solver.add(ctx.mkEq(ctx.mkAdd(my, ctx.mkMul(myv, t)), ctx.mkAdd(ctx.mkReal(sy.toString()), ctx.mkMul(ctx.mkReal(syv.toString()), t))))
            solver.add(ctx.mkEq(ctx.mkAdd(mz, ctx.mkMul(mzv, t)), ctx.mkAdd(ctx.mkReal(sz.toString()), ctx.mkMul(ctx.mkReal(szv.toString()), t))))
        }
        if (solver.check() == Status.SATISFIABLE) {
            val model = solver.model
            val solution = listOf(mx, my, mz).sumOf { model.eval(it, false).toString().toDouble() }
            println(model)
            return solution.toLong()
        }
        TODO()
    }

    operator fun <T> List<T>.component1(): T {
        return this[0]
    }
    operator fun <T> List<T>.component2(): T {
        return this[1]
    }
    operator fun <T> List<T>.component3(): T {
        return this[2]
    }
    operator fun <T> List<T>.component4(): T {
        return this[3]
    }
    operator fun <T> List<T>.component5(): T {
        return this[4]
    }
    operator fun <T> List<T>.component6(): T {
        return this[5]
    }

}