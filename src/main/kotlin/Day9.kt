class Day9( input: List<String>) {

    val input = input.map { it.splitIgnoreEmpty(" ").map(String::toInt) }
    private fun findNextDiff(nums: List<Int>): Int {
        val diffs = nums.windowed(2).map { it[1] - it[0] }
        return if(diffs.all { it == 0 }) {
            0
        } else {
            diffs.last() + findNextDiff(diffs)
        }

    }

    private fun findPrevDiff(nums: List<Int>): Int {
        val diffs = nums.windowed(2).map { it[1] - it[0] }
        return if(diffs.all { it == 0 }) {
            0
        } else {
            diffs.first() - findPrevDiff(diffs)
        }

    }
    fun solvePart1() = input.sumOf { it.last()+findNextDiff(it) }
    fun solvePart2() = input.sumOf { it.first()-findPrevDiff(it) }
}