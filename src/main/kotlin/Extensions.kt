import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun CharSequence.splitIgnoreEmpty(vararg delimiters: String): List<String> {
    return this.split(*delimiters).filter {
        it.isNotEmpty()
    }
}

fun List<String>.splitBy(func: (String) -> Boolean): List<List<String>> {
    val ret = mutableListOf<List<String>>()
    var collect = mutableListOf<String>()
    for(line in this) {
        if(func(line)) {
            if(collect.isNotEmpty()) ret.add(collect)
            collect = mutableListOf()
        } else {
            collect.add(line)
        }
    }
    if(collect.isNotEmpty()) ret.add(collect)
    return ret
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun <T, R : Comparable<R>> Iterable<T>.minAndMaxOf(selector: (T) -> R): Pair<R, R> {
    val iterator = iterator()
    if (!iterator.hasNext()) throw NoSuchElementException()
    var maxValue = selector(iterator.next())
    var minValue = maxValue
    while (iterator.hasNext()) {
        val v = selector(iterator.next())
        if (maxValue < v) {
            maxValue = v
        }
        if(minValue > v) {
            minValue = v
        }
    }
    return minValue to maxValue
}

fun List<Number>.multiply(): Long = map { it.toLong() }.fold(1L) { acc, it -> acc * it }

fun List<String>.rotate(): List<String> = (0 until first().length).map { x -> (size-1 downTo 0).map { this[it][x] }.joinToString("") }

fun <T:Number, E:Pair<String, T>, K> List<E>.sumValue(keySelector: (E) -> K) = groupingBy(keySelector).aggregate{ _, accu: Long?, element, _ -> element.second.toLong().plus(accu?:0L)}

fun Boolean.toLong() = if(this) { 1L } else { 0L }

fun <T> List<T>.zipAll(other: List<T>): List<Pair<T?, T?>> {
    return if(this.size == other.size) {
        this.zip(other)
    } else if (this.size < other.size) {
        (this + List(other.size - this.size){null}).zip(other)
    } else {
        this.zip(other + List(this.size - other.size){null})
    }
}

fun Pair<Int, Int>.toRange(): IntRange = this.first..this.second


fun <T> List<T>.circularIterator(): CircularIterator<T> {
    return CircularIterator(this)
}

class CircularIterator<T>(val list: List<T>): Iterator<T> {
    private var i = -1
    override fun hasNext() = true

    override fun next(): T {
        i = (i + 1) % list.size
        return list[i]
    }

    fun getIndex() = i
}


fun Long.gcd(other:Long): Long {
    var b = other
    var a = this
    while(b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}
fun List<Long>.gcd(): Long {
    return this.drop(1).fold(this.first()) { acc, i -> acc.gcd(i)}
}

fun Long.lcm(other:Long): Long {
    return this * (other / this.gcd(other))
}
fun List<Long>.lcm(): Long {
    return this.drop(1).fold(this.first()) { acc, i -> acc.lcm(i)}
}

infix fun <T> Set<T>.xor(that: Set<T>): Set<T> = (this - that) + (that - this)


fun <A, B>List<A>.pmap(f: suspend (A) -> B): List<B> = runBlocking {
    map { async(Dispatchers.Default) { f(it) } }.awaitAll()
}
fun <A, B>List<A>.pmapIndexed(f: suspend (index: Int, A) -> B): List<B> = runBlocking {
    mapIndexed { idx, it -> async(Dispatchers.Default) { f(idx, it) } }.awaitAll()
}