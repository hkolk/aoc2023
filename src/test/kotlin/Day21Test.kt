import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 21")
class Day21Test {

    val testInput = """
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
    """.trimIndent().lines()

    val testInput2 = """
...........
...........
...........
...........
...........
.....S.....
...........
...........
...........
...........
...........
    """.trimIndent().lines()
    val realInput = Resources.resourceAsList("day21.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day21(testInput).solvePart1(6)
            assertThat(answer).isEqualTo(16)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day21(realInput).solvePart1()
            assertThat(answer).isEqualTo(866435264)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day21(testInput2).solvePart2()
            assertThat(answer).isEqualTo(1)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day21(realInput).solvePart2()
            assertThat(answer).isEqualTo(229215609826339L)
        }
    }
}