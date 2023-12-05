class Day5(val input: List<String>) {

    data class Range(val from: Long, val to: Long, val size: Long) {
        val fromRange = from..< from+size
        val toRange = to ..< to+size
    }
    data class Mapper(val from: String, val to: String, val ranges: List<Range>) {
        fun map(source: Long): Long {
            ranges.forEach{
                if(source in it.fromRange) {
                    return it.to + (source - it.from)
                }
            }
            return source
        }
        fun unMap(dest: Long): Long {
            ranges.forEach{
                if(dest in it.toRange) {
                    return it.from + (dest - it.to)
                }
            }
            return dest
        }
        companion object {
            fun fromList(input: List<String>): Mapper {
                val parts = input.first().splitIgnoreEmpty(" ", "-")
                val ranges = input.drop(1).map { line -> line.splitIgnoreEmpty(" ").map(String::toLong).let { Range(it[1], it[0], it[2]) } }
                return Mapper(parts[0], parts[2], ranges);
            }
        }
    }
    fun solvePart1(): Long {
        val seeds = input.first().splitIgnoreEmpty(" ").drop(1).map { it.toLong() }
        val mappers = input.drop(2).splitBy { it.isEmpty() }.map(Mapper::fromList)

        var current = "seed"
        val results = mutableMapOf(current to seeds)
        while(current != "location") {
            val mapper = mappers.find { it.from == current }!!
            results[mapper.to] = results[current]!!.map { mapper.map(it) }
            current = mapper.to
        }
        return results["location"]!!.min()
    }
    fun solvePart2(): Long {
        val seedRanges = input.first().splitIgnoreEmpty(" ").drop(1).map { it.toLong() }
        val seeds = seedRanges.chunked(2).map { (it[0]..<it[0]+it[1]) }
        val mappers = input.drop(2).splitBy { it.isEmpty() }.map(Mapper::fromList)

        for(i in 0..1_000_000_000) {
            var current = "location"
            var result = i.toLong()
            while(current != "seed") {
                val mapper = mappers.find { it.to == current }!!
                result = mapper.unMap(result)
                current = mapper.from
            }
            if(seeds.any{it.contains(result)}) {
                return i.toLong()
            }
        }
        TODO()
    }
}