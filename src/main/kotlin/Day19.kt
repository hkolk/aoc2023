class Day19(input: List<String>) {
    val split = input.splitBy { it.isEmpty() }
    val eleMap = mapOf('x' to 0, 'm' to 1, 'a' to 2, 's' to 3)

    val rules = split[0].map { line ->
        val parts = line.splitIgnoreEmpty("{", "}", ",")
        val name = parts.first()
        val rules = parts.drop(1).map { Rule.fromString(it) }
        name to rules
    }.toMap()

    data class Rule(val condition: Triple<Int, Char, Int>?, val result: String) {
        companion object {
            fun fromString(input: String): Rule {
                val eleMap = mapOf('x' to 0, 'm' to 1, 'a' to 2, 's' to 3)

                val parts = input.splitIgnoreEmpty(":")
                return if(parts.size == 1) {
                    Rule(null, parts.first())
                } else {
                    Rule(Triple(eleMap[parts[0][0]]!!, parts[0][1], parts[0].drop(2).toInt()), parts[1])
                }
            }
        }

    }

    fun validate(rules: List<Rule>, part: List<Int>): String {
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

    data class Part(val elements: List<List<Int>>) {
        fun applyRule(rule: Rule): Pair<Part, Part?> {
            if (rule.condition != null) {
                val applied = elements[rule.condition.first]!!.let {
                    when(rule.condition.second) {
                        '<' -> it.filter { it < rule.condition.third } to it.filter { it >= rule.condition.third }
                        '>' -> it.filter { it > rule.condition.third } to it.filter { it <= rule.condition.third }
                        else -> throw IllegalStateException()
                    }
                }

                val matched = elements.mapIndexed { idx, it -> if(idx == rule.condition.first) applied.first else it  }
                val unmatched = elements.mapIndexed { idx, it -> if(idx == rule.condition.first) applied.second else it  }

                return Part(matched) to Part(unmatched)
            } else {
                return this to null
            }
        }

        fun intersectCount(other: List<Part>): Long {
            var new = this.elements
            for(i in other) {
                new = new.mapIndexed { idx, it ->
                    (i.elements[idx]).intersect(it.toSet()).toList()
                }
            }
            return new.fold(1L) { acc, item -> acc * item.size}
        }
        fun surface() = this.elements.fold(1L) { acc, item -> acc * item.size}


        override fun toString(): String {
            return "Part("+elements.map { entry -> "${toRanges(entry)}" }.joinToString(",") + ")"
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
        //println("${" ".repeated(level).joinToString("")} $ruleName, $part")
        if(ruleName == "A") {
            return listOf(part)
        }
        if(ruleName == "R") {
            return listOf()
        }
        var part = part
        val ret = mutableListOf<Part>()
        rules[ruleName]!!.forEach {
            val options = part.applyRule(it)
            part = options.second ?: Part(listOf())
            ret.addAll(recurse(it.result, options.first, level + 1))
        }
        return ret
    }

    // Using the exclusion/inclusion principle
    fun totalSurface(calculate: List<Part>): Long {
        val visited = mutableListOf<Part>()
        val exclusives = calculate.map { base ->
            var baseCount = base.surface()
            val intersections = visited.filter { it.intersectCount(listOf(base)) > 0 }
            if(intersections.size > 0) {
                println("isize: ${intersections.size}")
            }
            for(i in 1..intersections.size) {
                val isectCount = intersections.combinations(i).sumOf { base.intersectCount(it) }
                if(i % 2 == 0) {
                    baseCount += isectCount
                } else {
                    baseCount -= isectCount
                }
            }
            visited.add(base)
            baseCount
        }
        return exclusives.sum()
    }


    fun solvePart1(): Int {
        val parts = split[1].map { line -> line.splitIgnoreEmpty("{", "}", ",").map{ it.splitIgnoreEmpty("=").let { it[1].toInt() }} }
        val result = parts.map { part ->
            var rule = "in"
            var depth = 0
            while(rule !in listOf("R", "A")) {
                rule = validate(rules[rule]!!, part)
                depth++
            }
            if(rule == "A") {
                part.sum()
            } else {
                0
            }
        }
        return result.sum()
    }

    fun solvePart2(): Long {

        val part = Part(listOf(
            (1..4000).toList(),
            (1..4000).toList(),
            (1..4000).toList(),
            (1..4000).toList()
            ))
        val result = recurse("in", part).filter { !it.elements.any { it.isEmpty() } }
        return result.sumOf { it.surface() }
        // It seems the awesome option is not needed here..
        return totalSurface(result)
    }
}