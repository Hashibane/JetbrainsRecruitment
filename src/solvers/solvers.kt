package solvers

import problem.ProblemRepresentation
import problem.Station

typealias Solver = (ProblemRepresentation) -> List<Station>

fun naiveSolver(representation: ProblemRepresentation): List<Station> {
    with (representation) {
        val nodeQueue = ArrayDeque(listOf(startStation))

        while (!nodeQueue.isEmpty()) {
            val node = nodeQueue.first()
            val outgoingTypes = stations[node]!!.outgoingTypes
            for (station in stations[node]?.connections ?: continue) {
                if (stations[station]!!.receiveCargo(outgoingTypes)) {
                    nodeQueue.add(station)
                }

            }

            nodeQueue.removeFirst()
        }

        return stations.values.toList()
    }
}

fun exploringSolver(representation: ProblemRepresentation): List<Station> {
    with (representation) {
        val nodeQueue = ArrayDeque(listOf(startStation))
        val priorityQueue = ArrayDeque<Int>()
        var priorityUsed: Boolean

        while (nodeQueue.isNotEmpty() || priorityQueue.isNotEmpty()) {
            val node = if (priorityQueue.isEmpty()) {
                priorityUsed = false
                nodeQueue.first()
            } else {
                priorityUsed = true
                priorityQueue.first()
            }

            val currentStation = stations[node]!!
            currentStation.visited = true

            val outgoingTypes = currentStation.outgoingTypes

            for (station in stations[node]?.connections ?: continue) {
                val nextStation = stations[station]!! // not null, because of reader checking for it
                if (nextStation.receiveCargo(outgoingTypes)) {
                    if (!nextStation.visited)
                        priorityQueue.add(nextStation.id)
                    else
                        nodeQueue.add(nextStation.id)
                }

            }
            if (priorityUsed) {
                priorityQueue.removeFirst()
            } else {
                nodeQueue.removeFirst()
            }


        }

        return stations.values.toList()
    }
}