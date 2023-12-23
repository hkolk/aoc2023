import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 22")
class Day22Test {

    val testInput = """
1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day22.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day22(testInput).solvePart1()
            assertThat(answer).isEqualTo(5)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day22(realInput).solvePart1()
            assertThat(answer).isEqualTo(505)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day22(testInput).solvePart2()
            assertThat(answer).isEqualTo(7)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day22(realInput).solvePart2()
            assertThat(answer).isEqualTo(71002)
        }
    }
}