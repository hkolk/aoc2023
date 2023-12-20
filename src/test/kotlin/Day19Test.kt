import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Day 19")
class Day19Test {

    val testInput = """
px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
    """.trimIndent().lines()

    val realInput = Resources.resourceAsList("day19.txt")

    @Nested
    @DisplayName("Part 1")
    inner class Part1 {
        @Test
        fun `Matches Example`() {
            val answer = Day19(testInput).solvePart1()
            assertThat(answer).isEqualTo(19114)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day19(realInput).solvePart1()
            assertThat(answer).isEqualTo(323625)
        }
    }
    @Nested
    @DisplayName("Part 2")
    inner class Part2 {
        @Test
        fun `Matches Example`() {
            val answer = Day19(testInput).solvePart2()
            assertThat(answer).isEqualTo(167409079868000L)
        }

        @Test
        fun `Actual Answer`() {
            val answer = Day19(realInput).solvePart2()
            assertThat(answer).isEqualTo(127447746739409L)
        }
    }
}