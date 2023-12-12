class Day12(val input: List<String>) {

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
        val lines = input.map {
            it.splitIgnoreEmpty(" ").let { it[0] to it[1].splitIgnoreEmpty(",").map { it.toInt() } }
        }
        return lines.sumOf {line ->
            val perms = permutations(line.first).filter { validate(it, line.second) }
            perms.size
        }

    }
    fun solvePart2(): Int {
        TODO()
    }
}