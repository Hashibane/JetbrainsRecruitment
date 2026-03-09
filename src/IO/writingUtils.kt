package IO

import problem.Station

fun writeSolution(stationList: List<Station>) {
    println(stationList.sortedBy { it.id }.joinToString(separator = "\n"))
}