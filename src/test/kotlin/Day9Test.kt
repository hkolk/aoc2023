import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 9")
class Day9Test {

    val testInput = """
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day9.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day9(testInput).solvePart1()
            assertThat(answer).isEqualTo(114)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day9(realInput).solvePart1()
            assertThat(answer).isEqualTo(1995001648)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day9(testInput).solvePart2()
            assertThat(answer).isEqualTo(2)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day9(realInput).solvePart2()
            assertThat(answer).isEqualTo(988)
        }
    }
}