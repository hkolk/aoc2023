import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 20")
class Day20Test {

    val testInput = """
broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day20.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day20(testInput).solvePart1()
            assertThat(answer).isEqualTo(11687500)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day20(realInput).solvePart1()
            assertThat(answer).isEqualTo(866435264)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day20(testInput).solvePart2()
            assertThat(answer).isEqualTo(1)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day20(realInput).solvePart2()
            assertThat(answer).isEqualTo(229215609826339L)
        }
    }
}