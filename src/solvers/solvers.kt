package solvers

import problem.ProblemRepresentation
import problem.Station

typealias Solver = (ProblemRepresentation) -> List<Station>

fun naiveSolver(representation: ProblemRepresentation): List<Station> {
    with (representation) {
        val nodeQueue = ArrayDeque(listOf(startStation))

        while (!nodeQueue.isEmpty()) {
            val node = nodeQueue.first()
            println("visiting $node")
            println("current incoming types: ${stations[node]!!.incomingTypes}")
            val outgoingTypes = stations[node]!!.outgoingTypes
            println("current outgoing types $outgoingTypes")
            // LOL!
            for (station in stations[node]?.connections ?: listOf()) {
                if (stations[station]!!.receiveCargo(outgoingTypes)) {
                    nodeQueue.add(station)
                    println("added $station")
                }

            }

            println("popped $node")
            nodeQueue.removeFirst()
        }

        return stations.values.toList()
    }
}