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

        override fun addConnection(name: String) {}

        override fun toString() = abstractToString("FlipFlop($on, ")

    }

    class Conjunction(name:String, receivers:List<String>): Module(name, receivers) {
        val connections = mutableMapOf<String, Boolean>()

        val sortOrder = listOf("mg", "hl", "qb", "cd", "ps", "xc", "kz", "mc", "xv", "tx", "rv", "tp", "jc")
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
            val integer = sorted.map { if(it.second) 1 else 0 }.joinToString("").toInt()
            return "$seperated ($integer)"
        }
    }

    fun button(): Pair<Int, Int> {
        // simulate button
        var signals = broadcaster.map { Triple("broadcaster", it, false) }
        var processedHigh = 0
        var processedLow = 1 + broadcaster.size

        repeat(10000) {
            //println("Step $it, signals: $signals")

            signals = signals.flatMap { signal ->
                val receiver = modules[signal.second]
                if(receiver != null ) {
                    modules[signal.second]!!.receive(signal.first, signal.third).map { Triple(signal.second, it.first, it.second) }
                } else {
                    listOf()
                }
            }
            processedHigh += signals.count { it.third }
            processedLow += signals.count { !it.third }

            if(signals.isEmpty()) {
                return processedHigh to processedLow
            }
        }
        TODO()
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

    fun button2(debug:Boolean=false): Pair<Int, Int> {
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
    fun solvePart2(): Int {
        val rxSender = modules.firstNotNullOf { if(it.value.receivers.contains("rx")) it.value else null }
        //println(rxSender)
        val sendersToRxSender = modules.filter { it.value.receivers.contains(rxSender.name) }
        //println(sendersToRxSender)
        var round = 0
        repeat(4096-2) {
            button2()
            round++

            println("Round: $round - ${(modules["jc"] as Conjunction).toBits()}")
        }


        println("Round $round")
        button2(true); round++
        println("Round $round")
        printDigraph()
        button2(true); round++
        printDigraph()
        println("Round $round")
        button2(true); round++
        println("Round $round")
        button2(true); round++



        TODO()
        while((modules["dg"] as Conjunction).connections.values.all { !it }) {
            button2()
        }
        println(modules["dg"])
        //modules.forEach { println(it) }
        TODO()
    }
}