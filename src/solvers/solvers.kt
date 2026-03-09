package solvers

import problem.ProblemRepresentation
import problem.Station

typealias Solver = (ProblemRepresentation) -> List<Station>

private const val INITIAL_BUFFER_CAPACITY = 128

fun naiveSolver(representation: ProblemRepresentation): List<Station> {
    with (representation) {
        val nodeQueue = ArrayDeque<Int>(INITIAL_BUFFER_CAPACITY)
        nodeQueue.add(startStation)

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
        val nodeQueue = ArrayDeque<Int>(INITIAL_BUFFER_CAPACITY)
        nodeQueue.add(startStation)

        val priorityQueue = ArrayDeque<Int>(INITIAL_BUFFER_CAPACITY)
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

// This is the one that the final solution is using
fun rpoSolver(representation: ProblemRepresentation): List<Station> {
    fun relaxGraph(stations: Map<Int, Station>, ordering: ArrayDeque<Int>) {
        var isRelaxed: Boolean

        do {
            isRelaxed = true
            for (i in ordering) {
                val currentStation = stations[i]!!
                val outgoingTypes = currentStation.outgoingTypes

                for (station in currentStation.connections) {
                    val neighbour = stations[station]!!
                    if (neighbour.receiveCargo(outgoingTypes)) {
                        isRelaxed = false
                    }
                }
            }
        } while (!isRelaxed)
    }

    with (representation) {
        val nodeQueue = ArrayDeque<Int>(INITIAL_BUFFER_CAPACITY)
        nodeQueue.add(startStation)

        val rpoOrdering = ArrayDeque<Int>(representation.stations.size)

        while (!nodeQueue.isEmpty()) {
            val node = nodeQueue.first()
            val currentStation = stations[node]!!
            currentStation.visited = true

            var hasUnvisitedChild = false
            for (i in currentStation.cachedIndex..<currentStation.connections.size) {
                val station = currentStation.connections[i]
                val neighbor = stations[station]!!
                if (!neighbor.visited) {
                    currentStation.cachedIndex = i + 1
                    neighbor.visited = true
                    hasUnvisitedChild = true
                    nodeQueue.addFirst(station)
                    break
                }
            }

            if (!hasUnvisitedChild) {
                nodeQueue.removeFirst()
                rpoOrdering.addFirst(node)
            }
        }

        relaxGraph(stations, rpoOrdering)

        return stations.values.toList()
    }
}

fun exploringWorklistSolver(representation: ProblemRepresentation): List<Station> {
    with (representation) {
        val nodeQueue = ArrayDeque<Int>(INITIAL_BUFFER_CAPACITY)
        nodeQueue.add(startStation)

        val priorityQueue = ArrayDeque<Int>(INITIAL_BUFFER_CAPACITY)

        while (nodeQueue.isNotEmpty() || priorityQueue.isNotEmpty()) {
            val node = if (priorityQueue.isEmpty()) {
                nodeQueue.removeFirst()
            } else {
                priorityQueue.removeFirst()
            }

            val currentStation = stations[node]!!
            currentStation.visited = true

            val outgoingTypes = currentStation.outgoingTypes
            for (station in currentStation.connections) {
                val nextStation = stations[station]!!
                if (nextStation.receiveCargo(outgoingTypes)) {
                    if (!nextStation.visited)
                        priorityQueue.addFirst(nextStation.id)
                    else
                        nodeQueue.addFirst(nextStation.id)
                }

            }
        }

        return stations.values.toList()
    }
}