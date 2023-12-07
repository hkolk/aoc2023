import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 6")
class Day6Test {

    val testInput = """
Time:      7  15   30
Distance:  9  40  200
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day6.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day6(testInput).solvePart1()
            assertThat(answer).isEqualTo(288)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day6(realInput).solvePart1()
            assertThat(answer).isEqualTo(1624896)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day6(testInput).solvePart2()
            assertThat(answer).isEqualTo(71503)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day6(realInput).solvePart2()
            assertThat(answer).isEqualTo(32583852)
        }
    }
}