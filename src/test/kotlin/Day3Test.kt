import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 3")
class Day3Test {

    val testInput = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...${'$'}.*....
.664.598..
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day3.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day3(testInput).solvePart1()
            assertThat(answer).isEqualTo(4361)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day3(realInput).solvePart1()
            assertThat(answer).isEqualTo(539433)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day3(testInput).solvePart2()
            assertThat(answer).isEqualTo(2286)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day3(realInput).solvePart2()
            assertThat(answer).isEqualTo(83435)
        }
    }
}