package problem

class Station(val id: Int, val consumes: Int, val produces: Int, val connections: List<Int>, var visited: Boolean = false, var cachedIndex: Int = 0) {
    var incomingTypes: Set<Int> = emptySet()
        private set

    var outgoingTypes: Set<Int> = setOf(produces)
        private set


    // potentially a visitor pattern(?) - with an interface
    fun receiveCargo(cargo: Set<Int>): Boolean {
        val newSet = incomingTypes.plus(cargo)

        if (newSet.size > incomingTypes.size) {
            incomingTypes = newSet

            outgoingTypes = incomingTypes.minus(consumes).plus(produces)

            return true
        }
        return false
    }

    override fun toString(): String = "$id -> ${incomingTypes.sorted().joinToString()}"
}