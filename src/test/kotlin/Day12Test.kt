import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 12")
class Day12Test {

    val testInput = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day12.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day12(testInput).solvePart1()
            assertThat(answer).isEqualTo(21)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day12(realInput).solvePart1()
            assertThat(answer).isEqualTo(8075)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day12(testInput).solvePart2()
            assertThat(answer).isEqualTo(525152)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day12(realInput).solvePart2()
            assertThat(answer).isEqualTo(406725732046L)
        }
    }
}