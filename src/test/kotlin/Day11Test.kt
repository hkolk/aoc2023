import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 11")
class Day11Test {

    val testInput = """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day11.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day11(testInput).solvePart1()
            assertThat(answer).isEqualTo(374)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day11(realInput).solvePart1()
            assertThat(answer).isEqualTo(9545480)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day11(testInput).solvePart2(100)
            assertThat(answer).isEqualTo(8410)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day11(realInput).solvePart2(1_000_000)
            assertThat(answer).isEqualTo(406725732046L)
        }
    }
}