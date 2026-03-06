import java.util.Queue

class Station(val consumed: Int, val produced: Int) {

    var incomingTypes: Set<Int> = emptySet()
        private set

    // can be cached by lazy(?)
    val outgoingTypes: Set<Int>
        get() = incomingTypes.minus(consumed).plus(produced)

    // potentially a visitor pattern(?) - with an interface
    fun receiveCargo(cargo: Set<Int>): Boolean {
        val newSet = incomingTypes.plus(cargo)

        if (newSet.size > incomingTypes.size) {
            incomingTypes = newSet
            return true
        }
        return false
    }
}

// Station repository
fun resolveGraph(start: Int, stations: Map<Int, Station>, connections: Map<Int, List<Int>>): List<Station> {
    val nodeQueue = ArrayDeque(listOf(start))

    while (!nodeQueue.isEmpty()) {
        val node = nodeQueue.first()
        println("visiting $node")
        val outgoingTypes = stations[node]!!.outgoingTypes
        // LOL!
        for (station in connections[node] ?: listOf()) {
            if (stations[station]!!.receiveCargo(outgoingTypes))
                nodeQueue.add(station)
        }

        nodeQueue.removeFirst()
    }

    return stations.values.toList()
}

fun main() {
    val stations = mutableMapOf<Int, Station>()
    val connections = mutableMapOf<Int, MutableList<Int>>()

    var lineBuffer = readln()
    // error handling!!
    val (s, t) = lineBuffer.split(',', ' ').map { it.toInt() }

    for (i in 0..<s) {
        lineBuffer = readln()
        val (s, c_unload, c_load) = lineBuffer.split(',', ' ').map { it.toInt() }
        stations[s] = Station(c_unload, c_load)
    }

    for (i in 0..<t) {
        lineBuffer = readln()
        val (s_from, s_to) = lineBuffer.split(',', ' ').map { it.toInt() }

        // assuming there was s_from station
        connections[s_from]?.add(s_to) ?: connections.set(s_from, mutableListOf(s_to))
    }

    lineBuffer = readln()
    val s_0 = lineBuffer.toInt()

    val cargoMap = mapOf(
        1 to "V",
        2 to "T",
        3 to "U",
        4 to "E",
        5 to "A",
        6 to "B"
    )
    println(resolveGraph(s_0 + 1, stations, connections).map { it.incomingTypes.map{ cargoMap[it] } })

}