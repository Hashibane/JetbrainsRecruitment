package problem

class Station(val id: Int, val consumed: Int, val produced: Int, val connections: List<Int>) {
    var incomingTypes: Set<Int> = emptySet()
        private set

    var outgoingTypes: Set<Int> = setOf(produced)
        private set


    // potentially a visitor pattern(?) - with an interface
    fun receiveCargo(cargo: Set<Int>): Boolean {
        println("Checking $cargo vs $incomingTypes")
        val newSet = incomingTypes.plus(cargo)

        if (newSet.size > incomingTypes.size) {
            incomingTypes = newSet

            outgoingTypes = incomingTypes.minus(consumed).plus(produced)

            return true
        }
        return false
    }

    override fun toString(): String = "$id -> ${incomingTypes.joinToString()}"
}