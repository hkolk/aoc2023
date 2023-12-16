import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 16")
class Day16Test {

    val testInput = """
.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day16.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day16(testInput).solvePart1()
            assertThat(answer).isEqualTo(46)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day16(realInput).solvePart1()
            assertThat(answer).isEqualTo(7242)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day16(testInput).solvePart2()
            assertThat(answer).isEqualTo(51)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day16(realInput).solvePart2()
            assertThat(answer).isEqualTo(245223)
        }
    }
}