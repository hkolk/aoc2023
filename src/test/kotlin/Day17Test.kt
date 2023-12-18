import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 17")
class Day17Test {

    val testInput = """
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day17.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day17(testInput).solvePart1()
            assertThat(answer).isEqualTo(102)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day17(realInput).solvePart1()
            assertThat(answer).isEqualTo(1244)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day17(testInput).solvePart2()
            assertThat(answer).isEqualTo(94)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day17(realInput).solvePart2()
            assertThat(answer).isEqualTo(1367)
        }
    }
}