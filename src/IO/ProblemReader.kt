package IO

import problem.ProblemBuilder
import problem.ProblemRepresentation
import java.util.Scanner

private fun Scanner.checkedNextInt(): Int =
    if (hasNextInt()) {
        nextInt()
    } else {
        throw IllegalArgumentException("The parameter could not be parsed as an Int (could the input be empty?)")
    }

fun parseProblem(scanner: Scanner): ProblemRepresentation {
    with (scanner) {
        val s = checkedNextInt()
        val t = checkedNextInt()

        val builder = ProblemBuilder(s)

        repeat(s) {
            val id = checkedNextInt()
            val cUnload = checkedNextInt()
            val cLoad = checkedNextInt()

            builder.station(id, cUnload, cLoad)
        }

        repeat(t) {
            val from = checkedNextInt()
            val to = checkedNextInt()
            builder.track(from, to)
        }

        return builder.spawn(checkedNextInt())
    }
}