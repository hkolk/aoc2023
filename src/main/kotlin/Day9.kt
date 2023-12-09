class Day9( input: List<String>) {

    val input = input.map { it.splitIgnoreEmpty(" ").map(String::toInt) }
    
    private fun findDiff(nums: List<Int>, funct:(List<Int>, Int ) -> Int): Int {
        val diffs = nums.windowed(2).map { it[1] - it[0] }
        return if(diffs.all { it == 0 }) {
            0
        } else {
            funct(diffs, findDiff(diffs, funct))
        }
    }
    fun solvePart1() = input.sumOf { it.last() + findDiff(it) { nums, diff -> nums.last() + diff } }
    fun solvePart2() = input.sumOf { it.first() - findDiff(it) { nums, diff -> nums.first() - diff } }

}