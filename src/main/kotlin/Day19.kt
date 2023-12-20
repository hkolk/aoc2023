class Day19(input: List<String>) {
    val split = input.splitBy { it.isEmpty() }
    val rules = split[0].map { line ->
        val parts = line.splitIgnoreEmpty("{", "}", ",")
        val name = parts.first()
        val rules = parts.drop(1).map { Rule.fromString(it) }
        name to rules
    }.toMap()
    data class Rule(val condition: Triple<Char, Char, Int>?, val result: String) {
        companion object {
            fun fromString(input: String): Rule {
                val parts = input.splitIgnoreEmpty(":")
                return if(parts.size == 1) {
                    Rule(null, parts.first())
                } else {
                    Rule(Triple(parts[0][0], parts[0][1], parts[0].drop(2).toInt()), parts[1])
                }
            }
        }

    }

    fun validate(rules: List<Rule>, part: Map<Char, Int>): String {
        rules.forEach {
            if(it.condition != null) {
                val passes = when(it.condition.second) {
                    '>' -> part[it.condition.first]!! > it.condition.third
                    '<' -> part[it.condition.first]!! < it.condition.third
                    else -> throw IllegalStateException("Unknown rule: $it")
                }
                if(passes) {
                    return it.result
                }
            } else {
                return it.result
            }
        }
        throw IllegalStateException("Ran out of conditions for $rules")
    }
    fun solvePart1(): Int {
        val parts = split[1].map { line -> line.splitIgnoreEmpty("{", "}", ",").map{ it.splitIgnoreEmpty("=").let { it[0].first() to it[1].toInt() }}.toMap() }
        val result = parts.map { part ->
            var rule = "in"
            var depth = 0
            while(rule !in listOf("R", "A")) {
                rule = validate(rules[rule]!!, part)
                depth++
            }
            if(rule == "A") {
                part.values.sum()
            } else {
                0
            }
        }
        return result.sum()
    }

    data class Part(val elements: Map<Char, List<Int>>) {
        fun applyRule(rule: Rule): Pair<Part, Part?> {
            if (rule.condition != null) {
                val applied = elements[rule.condition.first]!!.let {
                    when(rule.condition.second) {
                        '<' -> it.filter { it < rule.condition.third } to it.filter { it >= rule.condition.third }
                        '>' -> it.filter { it > rule.condition.third } to it.filter { it <= rule.condition.third }
                        else -> throw IllegalStateException()
                    }
                }

                val matched = elements.map { if(it.key == rule.condition.first) it.key to applied.first else it.key to it.value  }.toMap()
                val unmatched = elements.map { if(it.key == rule.condition.first) it.key to applied.second else it.key to it.value  }.toMap()

                return Part(matched) to Part(unmatched)
            } else {
                return this to null
            }
        }

        fun union(other: Part): Part {
            val new = this.elements.map {
                it.key to (other.elements[it.key]?:listOf()).union(it.value).toList()
            }.toMap()
            return Part(new)
        }

        fun intersectCount(other: Part): Long {
            val new = this.elements.map {
                (other.elements[it.key]?:listOf()).intersect(it.value.toSet()).toList()
            }
            return new.fold(1L) { acc, item -> acc * item.size}
        }
        fun surface() = this.elements.values.fold(1L) { acc, item -> acc * item.size}

        // remove from `this` everything from other
        fun complement(reduce: Part): Part {
            val ret = this.elements.map { item -> item.key to item.value.filter { it !in (reduce.elements[item.key]?:listOf()) } }.toMap()
            return Part(ret)
        }

        override fun toString(): String {
            return "Part("+elements.map { entry -> "${entry.key}=${toRanges(entry.value)}" }.joinToString(",") + ")"
        }
        fun toRanges(list: List<Int>): List<IntRange> {
            if(list.isEmpty()) {
                return listOf()
            }
            val sorted = list.sorted()
            val ret = mutableListOf<IntRange>()

            var prev = sorted.first()
            var start = prev
            for(i in sorted.drop(1)) {
                if(i == prev+1) {
                    // still in range
                } else {
                    ret.add(start .. prev)
                    start = i
                }
                prev = i
            }
            ret.add(start .. prev)
            return ret
        }
    }

    fun recurse(ruleName: String, part: Part, level:Int=0): List<Part> {
        println("${" ".repeated(level).joinToString("")} $ruleName, $part")
        if(ruleName == "A") {
            return listOf(part)
        }
        if(ruleName == "R") {
            return listOf()
        }
        var part = part
        val ret = mutableListOf<Part>()
        var lastRule = ""
        rules[ruleName]!!.forEach {

            val options = part.applyRule(it)
            part = options.second ?: Part(mapOf())
            lastRule = it.result
            ret.addAll(recurse(it.result, options.first, level + 1))
        }
        return ret
    }

    fun solvePart2(): Long {
        val part1 = Part(mapOf('x' to (1..7).toList(), 'm' to (1..5).toList()))
        val part2 = Part(mapOf('x' to (7..10).toList(), 'm' to (3..7).toList()))
        println("P1: $part1, P2: $part2")
        println("IntersectCount: ${part1.intersectCount(part2)}")
        println("P1 area: ${part1.surface()}")
        println("P2 area: ${part2.surface()}")
        println("Total: ${part1.surface()+part2.surface()-part1.intersectCount(part2)}")




        val part = Part(mapOf(
            'x' to (1..4000).toList(),
            'm' to (1..4000).toList(),
            'a' to (1..4000).toList(),
            's' to (1..4000).toList()
            ))
        val result = recurse("in", part).filter { !it.elements.any { it.value.isEmpty() } }



        println("======== result ========")
        var visited = mutableListOf(Part(mapOf()))
        val result5 = result.map {
            println(it)
            val ret = visited.fold(it) { acc, it -> acc.complement(it) }
            visited.add(it)
            ret.elements.values.fold(1L) { acc, item -> acc * item.size}
        }
        println(result5)

        val final = result.fold(Part(mapOf())) { acc, it -> it.union(acc) }
        println(final)
        println(final.elements.values.fold(1L) { acc, item -> acc * item.size})

        val resultMapped = result.map { it.elements.values.fold(1L) { acc, item -> acc * item.size} }
        println(result)
        println(resultMapped)
        return resultMapped.sum()
        //val subrule = rules["in"]!!.first()
        //println(part.applyRule(subrule))
        TODO()
    }
}