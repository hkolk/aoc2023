class Day15(input:List<String>) {
    val input = input.first().splitIgnoreEmpty(",")

    fun HASH(subject: String) = subject.fold(0) { acc, c ->
        (((acc + c.code) * 17) % 256)
    }
    data class Lens(val label: String, val focal: Int) {
        override fun toString(): String {
            return "[$label $focal]"
        }
    }

    fun solvePart1() = input.sumOf { HASH(it) }

    fun solvePart2(): Int {
        val boxes = (0..255).associateWith { mutableListOf<Lens>() }
        input.forEach { command ->
            val label = command.filter { it.isLetter() }
            val operation = command.drop(label.length).first()
            val focal = command.drop(label.length+1).firstOrNull()?.digitToInt() ?: 0
            //println("$label, $operation, $focal")
            val box = HASH(label)
            if(operation == '-') {
                boxes[box]!!.removeIf { it.label == label }
            } else {
                val idx = boxes[box]!!.indexOfFirst { it.label == label }
                if(idx == -1) {
                    boxes[box]!!.add(Lens(label, focal))
                } else {
                    boxes[box]!![idx] = Lens(label, focal)
                }
            }
            /*
            println("After \"$command\":")
            boxes.filter { it.value.size > 0 }.forEach {
                println("Box: ${it.key}: ${it.value.joinToString(" ")}")
            }
            println()
             */
        }

        return boxes.toList().sumOf { (boxId, lenses) ->
            lenses.mapIndexed { slot, lens ->
                (1+boxId) * (slot+1) * lens.focal
            }.sum()
        }
    }
}