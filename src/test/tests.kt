package test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import problem.ProblemBuilder
import problem.ProblemRepresentation
import problem.buildProblem
import solvers.Solver
import solvers.*




class ProblemTester {
    val solver: Solver = ::exploringSolver

    @Test
    fun `nontrivial with endpoint`() {
        val problem = buildProblem {
            station(1, 1, 1)
            station(2, 5, 2)
            station(3, 3, 1)
            station(4, 2, 1)
            station(5, 1, 4)
            station(6, 5, 6)
            station(7, 6, 2)
            station(8, 2, 5)
            station(9, 3, 1)

            track(1, 2)
            track(2, 3)
            track(3, 4)
            track(3, 2)
            track(4, 5)
            track(3, 5)
            track(5, 3)
            track(5, 6)
            track(3, 6)
            track(6, 7)
            track(6, 8)
            track(7, 9)
            track(8, 9)

            spawn(1)
        }

        val result = solver(problem)

        assertEquals(result.joinToString(separator = "\n"), """
            1 -> 
            2 -> 1, 2, 4
            3 -> 1, 2, 4
            4 -> 1, 2, 4
            5 -> 1, 2, 4
            6 -> 1, 2, 4
            7 -> 1, 2, 4, 6
            8 -> 1, 2, 4, 6
            9 -> 1, 2, 4, 5, 6
        """.trimIndent())
    }

    @Test
    fun `unconnected nodes`() {
        val problem = buildProblem {
            station(1, 1, 1)
            station(2, 5, 2)
            station(3, 3, 1)

            spawn(1)
        }

        val result = solver(problem)

        assertEquals(result.joinToString(separator = "\n"), """
            1 -> 
            2 -> 
            3 -> 
        """.trimIndent())
    }

    @Test
    fun `non-consuming cycle`() {
        val problem = buildProblem {
            station(1, 1, 2)
            station(2, 3, 2)
            station(3, 1, 4)

            track(1, 2)
            track(2, 1)
            track(2, 3)
            track(3, 2)
            track(1, 3)
            track(3, 1)

            spawn(1)
        }

        val result = solver(problem)

        assertEquals(result.joinToString(separator = "\n"), """
            1 -> 2, 4
            2 -> 2, 4
            3 -> 2, 4
        """.trimIndent())
    }

    @Test
    fun `unreachable cycle`() {
        val problem = buildProblem {
            station(1, 1, 2)
            station(2, 3, 2)
            station(3, 1, 4)

            track(2, 3)
            track(3, 2)

            spawn(1)
        }

        val result = solver(problem)

        assertEquals(result.joinToString(separator = "\n"), """
            1 -> 
            2 -> 
            3 -> 
        """.trimIndent())
    }

    @Test
    fun `self loop and multitrack`() {
        val problem = buildProblem {
            station(1, 1, 2)
            track(1, 1)
            track(1, 1)

            spawn(1)
        }

        val result = solver(problem)

        assertEquals(result.joinToString(separator = "\n"), """
            1 -> 2
        """.trimIndent())
    }

    @Test
    fun `fuzzing tests`() {
        val epochs = 100000
        val referenceSolver = ::naiveSolver
        val fuzzer = Fuzzer()

        repeat(epochs) {
            val result = fuzzer.generateRandomTest(referenceSolver, solver)
            assertEquals(result.first.trimIndent(), result.second.trimIndent())
        }

        fuzzer.printPerformanceReport()
    }
}