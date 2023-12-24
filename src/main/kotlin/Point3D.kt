import kotlin.math.absoluteValue
data class Point3D(val x:Int, val y:Int, val z:Int) {

    fun distance(other: Point3D): Point3D {
        return Point3D((x - other.x).absoluteValue, (y - other.y).absoluteValue, (z - other.z).absoluteValue)
    }

    fun manhattan(other: Point3D): Int {
        return (other.x - x).absoluteValue + (other.y - y).absoluteValue + (other.z - z).absoluteValue
    }

    fun adjacent(): List<Point3D> {
        return listOf(
            Point3D(x-1, y, z),
            Point3D(x+1, y, z),
            Point3D(x, y-1, z),
            Point3D(x, y+1, z),
            Point3D(x, y, z-1),
            Point3D(x, y, z+1)
        )
    }
    fun allPointsTo(other: Point3D) = sequence {
        for(x in this@Point3D.x.coerceAtMost(other.x)..this@Point3D.x.coerceAtLeast(other.x)) {
            for(y in this@Point3D.y.coerceAtMost(other.y)..this@Point3D.y.coerceAtLeast(other.y)) {
                for(z in this@Point3D.z.coerceAtMost(other.z)..this@Point3D.z.coerceAtLeast(other.z)) {
                    yield(Point3D(x, y, z))
                }
            }
        }
    }

    companion object {
        fun fromString(coords: String): Point3D {
            val (x, y, z) = coords.splitIgnoreEmpty(",").map { it.toInt() }
            return Point3D(x, y, z)
        }
    }
}

data class Point3DWide(val x: Long, val y: Long, val z: Long) {
    fun move(direction: Point3D, times: Int=1): Point3DWide = move( { Point3DWide(it.x+direction.x, it.y + direction.y, it.z + direction.z) }, times)

    fun getXY() = Point2DWide(x, y)
    fun getXZ() = Point2DWide(x, z)
    fun getYZ() = Point2DWide(y, z)

    fun move(direction: Point3DWide, times: Int=1): Point3DWide {
        if(times < 0) {
            return move({ Point3DWide(it.x - direction.x, it.y - direction.y, it.z - direction.z) }, -times)
        } else {
            return move({ Point3DWide(it.x + direction.x, it.y + direction.y, it.z + direction.z) }, times)
        }
    }

    fun move(direction: (Point3DWide) -> Point3DWide, times: Int=1): Point3DWide {
        assert(times >= 0)
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

    operator fun minus(other: Point3DWide): Point3DWide {
        return Point3DWide(x - other.x, y - other.y, z - other.z)

    }
}
data class Point3DDouble(val x: Double, val y: Double, val z: Double) {
    fun isExact() = (x % 1 == 0.0 && y % 1 == 0.0 && z % 1 == 0.0)
    fun toLong() = Point3DWide(x.toLong(), y.toLong(), z.toLong())

}