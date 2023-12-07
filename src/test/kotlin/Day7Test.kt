import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 7")
class Day7Test {

    val testInput = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
    """.trimIndent().lines()
    val testInput2 = """
        AAAAA 2
        22222 3
        AAAAK 5
        22223 7
        AAAKK 11
        22233 13
        AAAKQ 17
        22234 19
        AAKKQ 23
        22334 29
        AAKQJ 31
        22345 37
        AKQJT 41
        23456 43
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day7.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day7(testInput).solvePart1()
            assertThat(answer).isEqualTo(6440)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day7(realInput).solvePart1()
            assertThat(answer).isEqualTo(246_163_188)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day7(testInput).solvePart2()
            assertThat(answer).isEqualTo(5905)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day7(realInput).solvePart2()
            assertThat(answer).isEqualTo(245794069)
        }
    }
}