class Day20(val input: List<String>) {
    val broadcaster = input.first { it.startsWith("broadcaster") }.splitIgnoreEmpty(",", " ").drop(2)
    val modules = input.mapNotNull { line ->
        val type = line.first()
        val name = line.drop(1).takeWhile { it.isLetterOrDigit() }
        val receivers = line.splitIgnoreEmpty(", ", " ").drop(2)
        when (type) {
            '%' -> FlipFlop(name, receivers)
            '&' -> Conjunction(name, receivers)
            else -> null
        }
    }.associateBy { it.name }
    init {
        modules.forEach{ src ->
            src.value.receivers.forEach { recv -> modules[recv]?.addConnection(src.key) }
        }
    }

    abstract class Module(name: String, receivers: List<String>) {
        val name = name
        val receivers = receivers

        fun abstractToString(classname: String) = "$classname$name, $receivers)"
        abstract fun receive(source: String, high:Boolean): List<Pair<String, Boolean>>
        abstract fun addConnection(name: String)
        abstract fun value(): Int
    }

    class FlipFlop(name:String, receivers:List<String>): Module(name, receivers) {
        var on = false

        override fun receive(source: String, high:Boolean): List<Pair<String, Boolean>> {
            return if(!high) {
                on = !on
                receivers.map { it to on }
            } else {
                listOf()
            }
        }

        override fun value() = if(on) 1 else 0

        override fun addConnection(name: String) {}

        override fun toString() = abstractToString("FlipFlop($on, ")

    }

    class Conjunction(name:String, receivers:List<String>): Module(name, receivers) {
        val connections = mutableMapOf<String, Boolean>()

        val sortOrder = listOf(
            "mg", "hl", "qb", "cd", "ps", "xc", "kz", "mc", "xv", "tx", "rv", "tp", "jc")
        override fun receive(source: String, high: Boolean): List<Pair<String, Boolean>> {
            connections[source] = high
            if(name == "dg") {
                //println("Signal to $name from $source. New state: $connections")
            }
            val signal = !connections.values.all { it }
            return receivers.map { it to signal }
        }
        override fun addConnection(name: String) {
            connections[name] = false
        }
        override fun toString() = abstractToString("Conjunction($connections, ")

        fun toBits(): String {
            val sorted = connections.map { sortOrder.indexOf(it.key) to it.value }.sortedBy { it.first }.reversed()
            val seperated = sorted.map { if(it.second) 1 else 0 }.joinToString(",")
            val integer = sorted.map { if(it.second) 1 else 0 }.joinToString("").toInt(2)
            return "$seperated ($integer)"
        }
        override fun value(): Int {
            val sorted = connections.map { sortOrder.indexOf(it.key) to it.value }.sortedBy { it.first }.reversed()
            return sorted.map { if(it.second) 1 else 0 }.joinToString("").toInt(2)
        }
    }

    private fun button(triggers:List<String> = listOf()): Triple<Int, Int, Set<String>> {

        val triggerMessages = mutableSetOf<String>()

        // simulate button
        var signals = broadcaster.map { Triple("broadcaster", it, false) }
        var processedHigh = 0
        var processedLow = 1 + broadcaster.size

        repeat(10_000) {_ ->
            signals = signals.flatMap { signal ->
                val receiver = modules[signal.second]
                if(receiver != null ) {
                    modules[signal.second]!!.receive(signal.first, signal.third).map { Triple(signal.second, it.first, it.second) }
                } else {
                    listOf()
                }
            }
            signals.filter { it.first in triggers && !it.third }.forEach {
                triggerMessages.add(it.first)
            }
            processedHigh += signals.count { it.third }
            processedLow += signals.count { !it.third }

            if(signals.isEmpty()) {
                return Triple(processedHigh, processedLow, triggerMessages)
            }
        }
        TODO()
    }

    // Prints a nice DOT file
    fun printDigraph() {
        println("digraph stupid {")
        println("rx [color=blue]")
        modules.values.filterIsInstance<Conjunction>().forEach { println("${it.name} [shape=box color=red label=\"${it.name} - ${it.toBits()}\"]") }
        modules.values.filterIsInstance<FlipFlop>().forEach { println("${it.name} [label=\"${it.name} - ${it.on}\"]") }
        broadcaster.forEach { println("broadcast -> $it") }
        modules.forEach { mod -> mod.value.receivers.forEach { println("${mod.key} -> ${it}") } }
        println("}")
    }

    fun solvePart1(): Int {
        return (1..1000).fold(0 to 0) { acc, it ->
            button().let {
                acc.first + it.first to  acc.second + it.second
            }
        }.let { it.first * it.second }
    }
    fun solvePart2(): Long {
        if(!modules.contains("jc")) {
            return 1 // we don't support this
        }
        val found = mutableMapOf<String, Int>()
        repeat(10_000) {round ->
            val ret = button(listOf("jc", "dv", "xq", "vv"))
            ret.third.forEach{
                if(!found.contains(it)) {
                    found[it] = round+1
                }
            }
            if(found.size == 4) {
                // return first moment all rounds trigger together
                return found.values.map { it.toLong() }.lcm()
            }
        }
        TODO()
    }
}