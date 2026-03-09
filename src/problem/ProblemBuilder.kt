package problem

private const val INITIAL_BUFFER_CAPACITY = 128

fun buildProblem(body: ProblemBuilder.() -> ProblemRepresentation): ProblemRepresentation {
    val builder = ProblemBuilder()
    return builder.body()
}

class ProblemBuilder(initialCapacity: Int = INITIAL_BUFFER_CAPACITY) {
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
        if (stations[startStation] != null)
            ProblemRepresentation(startStation, stations.mapValues { it.value.spawn() })
        else
            throw IllegalArgumentException("The starting station does not exist in the given context")


}