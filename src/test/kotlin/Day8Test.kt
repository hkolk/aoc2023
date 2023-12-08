import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 8")
class Day8Test {

    val testInput = """
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
    """.trimIndent().lines()
    val testInput2 = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day8.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day8(testInput).solvePart1()
            assertThat(answer).isEqualTo(6)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day8(realInput).solvePart1()
            assertThat(answer).isEqualTo(16531)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day8(testInput2).solvePart2()
            assertThat(answer).isEqualTo(6)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day8(realInput).solvePart2()
            assertThat(answer).isEqualTo(24035773251517L)
        }
    }
}