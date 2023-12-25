class Day25(val input: List<String>) {
    val nodes = mutableSetOf<String>()
    val connections = mutableSetOf<Pair<String, String>>()
    init {
        input.forEach { line ->
            val parts = line.splitIgnoreEmpty(" ", ":")
            nodes.addAll(parts)
            parts.drop(1).forEach {
                val sorted = if (parts[0] < it) parts[0] to it else it to parts[0]
                connections.add(sorted)
            }
        }
    }
    fun findClusterSize(start: String, filteredConnections: List<Pair<String, String>>): Int {
        val visited = mutableSetOf<String>()
        val queue = mutableListOf(start)
        while(queue.isNotEmpty()) {
            val next = queue.removeAt(0)
            val connected = filteredConnections.filter { it.first == next || it.second == next }
            val new = connected.flatMap{it.toList().filter { it != next }}.filter { it !in visited }
            queue.addAll(new)
            visited.addAll(new)
        }
        return visited.size
    }
    fun solvePart1(): Int {
        if(false) {
            println("graph {")
            connections.forEach { println("  ${it.first} -- ${it.second}") }
            println("}")
        }
        // cut: pzr-sss, njx-pbx, sxx-zvk
        val filteredConnections = connections.filter { it !in listOf("pzr" to "sss", "njx" to "pbx", "sxx" to "zvk") }
        println(findClusterSize("qnn", filteredConnections))
        println(findClusterSize("nrd", filteredConnections))
        return findClusterSize("qnn", filteredConnections) * findClusterSize("nrd", filteredConnections)
    }
    fun solvePart2(): Int {
        return 1;
    }
}