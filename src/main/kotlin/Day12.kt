class Day12(val input: List<String>) {
    val lines = input.map {
        it.splitIgnoreEmpty(" ").let { it[0] to it[1].splitIgnoreEmpty(",").map { it.toInt() } }
    }

    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()

    fun validArrangements(springs: String, checksum: List<Int>): Long {
        //println("$springs, $checksum")
        // if checksum is empty, there can't be any mandatory springs left
        if (checksum.isEmpty()) return if ("#" in springs) 0 else 1
        // springs is empty and checksum is not
        if (springs.isEmpty()) return 0

        return cache.getOrPut(springs to checksum) {

            // check the first character?
            var count = 0L
            if (springs.first() in ".?") {
                count += validArrangements(springs.drop(1), checksum)
            }
            if (springs.first() in "#?") {
                if (springs.length >= checksum.first() && '.' !in springs.take(checksum.first())) {
                    // everything we took is # or ?
                    // next char should be . or ? or end of string
                    if (springs.length == checksum.first() || springs[checksum.first()] != '#') {
                        count += validArrangements(springs.drop(checksum.first() + 1), checksum.drop(1))
                    }
                }
            }
            count
        }
    }
    fun solvePart1() = lines.sumOf {line -> validArrangements(line.first, line.second) }

    fun solvePart2(): Long {
        return lines.sumOf {line ->
            validArrangements(line.first.repeated(5).joinToString("?"), line.second.repeated(5).flatMap { it }.toList())
        }
    }
}