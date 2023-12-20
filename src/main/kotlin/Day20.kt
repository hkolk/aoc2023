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
    fun solvePart1(): Int {
        println(broadcaster)
        modules.forEach { println(it) }

        var high =  0
        var low  = 0
        repeat(1_000) {
            //println("======= Button $it")
            button().let { high += it.first; low += it.second }
            //println(button())
        }
        return high * low
    }

    fun button(debug:Boolean=false, round:Int=0): Pair<Int, Int> {
        // simulate button
        var signals = broadcaster.map { Triple("broadcaster", it, false) }
        var processedHigh = 0
        var processedLow = 1 + broadcaster.size

        repeat(10000) {step ->
            //println("Step $it, signals: $signals")

            signals = signals.flatMap { signal ->
                val receiver = modules[signal.second]
                if(receiver != null ) {
                    modules[signal.second]!!.receive(signal.first, signal.third).map { Triple(signal.second, it.first, it.second) }
                } else {
                    listOf()
                }
            }
            if(signals.contains(Triple("jc", "lk", false))) {
                println("JC message to LK at round $round")
            }
            if(signals.contains(Triple("dv", "xt", false))) {
                println("DV message to XT at round $round")
            }
            if(signals.contains(Triple("xq", "sp", false))) {
                println("XQ message to SP at round $round")
            }
            if(signals.contains(Triple("vv", "zv", false))) {
                println("VV message to ZV at round $round")
            }
            if(debug) {
                println("  Step $step")
                signals.filter { it.first in listOf("mg", "hl", "qb", "cd", "ps", "xc", "kz", "mc", "xv", "tx", "rv", "tp", "jc") }.forEach { println("    $it") }
            }
            processedHigh += signals.count { it.third }
            processedLow += signals.count { !it.third }

            if(signals.isEmpty()) {
                return processedHigh to processedLow
            }
        }
        TODO()
    }
    fun printDigraph() {
        println("digraph stupid {")
        println("rx [color=blue]")
        modules.values.filter { it is Conjunction }.forEach { println("${it.name} [shape=box color=red label=\"${it.name} - ${(it as Conjunction).connections.values.map { if(it) 1 else 0 }.joinToString(",")}\"]") }
        modules.values.filter { it is FlipFlop }.forEach { println("${it.name} [label=\"${it.name} - ${(it as FlipFlop).on}\"]") }
        broadcaster.forEach { println("broadcast -> $it") }
        modules.forEach { mod -> mod.value.receivers.forEach { println("${mod.key} -> ${it}") } }
        println("}")
    }
    fun solvePart2(): Long {
        val rxSender = modules.firstNotNullOf { if(it.value.receivers.contains("rx")) it.value else null }
        //println(rxSender)
        val sendersToRxSender = modules.filter { it.value.receivers.contains(rxSender.name) }
        //println(sendersToRxSender)
        var round = 0
        repeat((3823*3)+2) {
            round++
            button(round=round)

            if (modules["jc"]!!.value() > 1019) {
                println("Round: $round - ${(modules["jc"] as Conjunction).toBits()}")
            }
        }
        return listOf(3767L, 3823L, 3929L, 4051L).fold(1L) { acc, it -> acc.lcm(it)}


        println("Round $round")
        button(true); round++
        println("Round $round")
        button(true); round++

        println("Round $round")
        printDigraph()
        button(true); round++
        printDigraph()

        println("Round $round")
        button(true); round++



        TODO()
        while((modules["dg"] as Conjunction).connections.values.all { !it }) {
            button()
        }
        println(modules["dg"])
        //modules.forEach { println(it) }
        TODO()
    }
}