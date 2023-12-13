import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 13")
class Day13Test {

    val testInput = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
    """.trimIndent().lines()
    val testInput3 = """
.##
#.#
#.#
    """.trimIndent().lines()
    val testInput2 = """
....#..#.
####.#.#.
...###.##
..#..##.#
##...#...
##.##.#..
##.....#.
##...##.#
...####.#
.#...####
####...#.
###..###.
..#####.#
..#..##.#
..#..##.#
    """.trimIndent().lines()
    val testInput4 = """
        ##..####.###.
        ###.##..##...
        ...####.#..##
        ...####.#..##
        ######..##...
        ##..####.###.
        #.##...##..#.
        .#..##..##..#
        .#..##..##..#
        #.##...##..#.
        ##..####.###.
        ######..##...
        ...####.#..##
        ...####.#..##
        ###.##..##...
        ##..####.###.
        .##.#.....#.#
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day13.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day13(testInput4).solvePart1()
            assertThat(answer).isEqualTo(405)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day13(realInput).solvePart1()
            assertThat(answer).isEqualTo(8075)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day13(testInput).solvePart2()
            assertThat(answer).isEqualTo(525152)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day13(realInput).solvePart2()
            assertThat(answer).isEqualTo(4232520187524L)
        }
    }
}