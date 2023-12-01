import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 1")
class Day1Test {

    val testInput = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
    """.trimIndent().lines()
    val realInput = Resources.resourceAsList("day1.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day1(testInput).solvePart1()
            assertThat(answer).isEqualTo(330)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day1(realInput).solvePart1()
            assertThat(answer).isEqualTo(664)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day1(testInput).solvePart2()
            assertThat(answer).isEqualTo(286)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day1(realInput).solvePart2()
            assertThat(answer).isEqualTo(640)
        }
    }
}