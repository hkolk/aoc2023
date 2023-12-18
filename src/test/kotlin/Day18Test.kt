import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 18")
class Day18Test {

    val testInput = """
R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day18.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day18(testInput).solvePart1()
            assertThat(answer).isEqualTo(62)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day18(realInput).solvePart1()
            assertThat(answer).isEqualTo(47139)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day18(testInput).solvePart2()
            assertThat(answer).isEqualTo(952408144115L)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day18(realInput).solvePart2()
            assertThat(answer).isEqualTo(1367)
        }
    }
}