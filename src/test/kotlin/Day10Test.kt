import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 10")
class Day10Test {

    val testInput = """
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day10.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day10(testInput).solvePart1()
            assertThat(answer).isEqualTo(8)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day10(realInput).solvePart1()
            assertThat(answer).isEqualTo(1995001648)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day10(testInput).solvePart2()
            assertThat(answer).isEqualTo(2)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day10(realInput).solvePart2()
            assertThat(answer).isEqualTo(988)
        }
    }
}