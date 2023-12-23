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