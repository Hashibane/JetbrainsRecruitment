package problem

private const val DEFAULT_INITIAL_CAPACITY = 10

class ProblemBuilder(initialCapacity: Int = DEFAULT_INITIAL_CAPACITY) {
    private val stations = LinkedHashMap<Int, StationBuilder>(initialCapacity)

    fun station(id: Int, consumes: Int, produces: Int) {
        stations[id] = StationBuilder(id, consumes, produces)
    }

    // will skip track creation if begin or end does not exist
    fun track(from: Int, to: Int) {
        val beginStation = stations[from] ?: return
        if (stations[to] != null) {
            beginStation.connect(to)
        }
    }

    fun spawn(startStation: Int): ProblemRepresentation =
        ProblemRepresentation(startStation, stations.mapValues { it.value.spawn() })
}