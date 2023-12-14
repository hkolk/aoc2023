import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 14")
class Day14Test {

    val testInput = """
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day14.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day14(testInput).solvePart1()
            assertThat(answer).isEqualTo(136)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day14(realInput).solvePart1()
            assertThat(answer).isEqualTo(109345)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day14(testInput).solvePart2()
            assertThat(answer).isEqualTo(64)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day14(realInput).solvePart2()
            assertThat(answer).isEqualTo(34230)
        }
    }
}