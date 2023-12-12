class Day12(val input: List<String>) {
    val lines = input.map {
        it.splitIgnoreEmpty(" ").let { it[0] to it[1].splitIgnoreEmpty(",").map { it.toInt() } }
    }

    fun validArrangements(springs: String, checksum: List<Int>): Int {
        println("$springs, $checksum")
        if(springs.isEmpty() || springs.all{ it == '.'}) {
            // springs is empty, so validates if checksum is also empty
            return if(checksum.isEmpty()) 1 else 0
        }
        if(checksum.isEmpty() ) {
            // springs is not empty, so this cannot validate anymore
            return 0
        }
        // check the first character?
        var count = 0
        if(springs[0] in ".?") {
            count = validArrangements(springs.drop(1), checksum)
        }
        if(springs[0] in "#?") {
            if(springs.length >= checksum.first() && '.' !in springs.take(checksum.first())) {
                // everything we took is # or ?
                // next char should be . or ? or end of string
                if(springs.length == checksum.first() || springs[checksum.first()] != '#') {
                    count += validArrangements(springs.drop(checksum.first()), checksum.drop(1))
                }
            }
        }
        return count
    }

    fun permutations(springs: String): List<String> {
        if(springs.isEmpty()) {
            return listOf("")
        }
        val perms = permutations(springs.drop(1))
        if(springs[0] == '?') {
            return perms.map { ".$it" } + perms.map { "#$it" }
        } else {
            return perms.map { springs[0] + it}
        }
    }

    fun validate(springs: String, checksum: List<Int>) = springs.splitIgnoreEmpty(".").map { it.length } == checksum

    fun solvePart1(): Int {

        return lines.sumOf {line ->
            val perms = permutations(line.first).filter { validate(it, line.second) }
            perms.size
        }

    }
    fun solvePart2(): Int {
        return lines.mapIndexed {idx, line ->
            val perms = validArrangements(line.first, line.second)
            val perms2 = permutations(line.first).filter { validate(it, line.second) }.size
            if(perms != perms2) {
                println("$idx, $perms, $perms2, $line")
                println(permutations(line.first).filter { validate(it, line.second) })
                TODO()
            }
            perms
        }.sum()

        TODO()
    }
}