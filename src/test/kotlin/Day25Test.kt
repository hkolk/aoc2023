import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 25")
class Day25Test {


    //
    val testInput = """
jqt: rhn xhk nvd
rsh: frs pzl lsr
xhk: hfx
cmg: qnr nvd lhk bvb
rhn: xhk bvb hfx
bvb: xhk hfx
pzl: lsr hfx nvd
qnr: nvd
ntq: jqt hfx bvb xhk
nvd: lhk
lsr: lhk
rzs: qnr cmg lsr rsh
frs: qnr lhk lsr
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day25.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day25(testInput).solvePart1()
            assertThat(answer).isEqualTo(54)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day25(realInput).solvePart1()
            assertThat(answer).isEqualTo(582590)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day25(testInput).solvePart2()
            assertThat(answer).isEqualTo(1)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day25(realInput).solvePart2()
            assertThat(answer).isEqualTo(1)
        }
    }
}