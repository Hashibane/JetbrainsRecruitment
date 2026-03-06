package problem

private const val INITIAL_BUFFER_CAPACITY = 64

class StationBuilder(private val id: Int, private val consumes: Int, private val produces: Int) {
    private val connections = ArrayList<Int>(INITIAL_BUFFER_CAPACITY)

    fun connect(other: Int) {
        connections.add(other)
    }

    fun spawn(): Station = Station(id,consumes, produces, connections)
}