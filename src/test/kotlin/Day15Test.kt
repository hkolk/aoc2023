import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 15")
class Day15Test {

    val testInput = """
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day15.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day15(testInput).solvePart1()
            assertThat(answer).isEqualTo(1320)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day15(realInput).solvePart1()
            assertThat(answer).isEqualTo(495972)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day15(testInput).solvePart2()
            assertThat(answer).isEqualTo(145)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day15(realInput).solvePart2()
            assertThat(answer).isEqualTo(245223)
        }
    }
}