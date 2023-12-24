import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 24")
class Day24Test {


    //
    val testInput = """
19, 13, 30 @ -2,  1, -2
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
12, 31, 28 @ -1, -2, -1
20, 19, 15 @  1, -5, -3
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day24.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day24(testInput).solvePart1(7.0..27.0)
            assertThat(answer).isEqualTo(2)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day24(realInput).solvePart1(200_000_000_000_000.0 .. 400_000_000_000_000.0)
            assertThat(answer).isEqualTo(26611)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day24(testInput).solvePart2()
            assertThat(answer).isEqualTo(154)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day24(realInput).solvePart2()
            assertThat(answer).isEqualTo(6622)
        }
    }
}