package test

import problem.ProblemRepresentation
import problem.buildProblem
import solvers.Solver
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.measureNanoTime

/*
    Notes to self
    - When a node has connection to itself, it's not redundant because the type produced may not be present
    in a node at the time
    - There could be a node that consumes T and produces T, but it should be handled
 */
typealias TestResult = String

class Fuzzer {
    var totalReferenceTimeNanos = 0L
        private set
    var totalComparedTimeNanos = 0L
        private set
    var successfulRuns = 0
        private set

    fun generateRandomTest(referenceSolver: Solver, comparedSolver: Solver, maxS: Int = 100, maxT: Int = 1000): Pair<TestResult, TestResult> {
        val runSeed = Random.nextInt()
        with (Random(runSeed)) {

            val s = nextInt(2, maxS)
            val t = nextInt(2, maxT)
            val minCargo = nextInt(s - 1)

            val problem: () -> ProblemRepresentation = {
                with (Random(runSeed)) {
                    buildProblem {
                        for (i in 1..s) {
                            val cUnload = nextInt(minCargo, s)
                            val cLoad = nextInt(minCargo, s)

                            station(i, cUnload, cLoad)
                        }

                        repeat(t) {
                            val from = nextInt(s)
                            val to = nextInt(s)
                            track(from, to)
                        }

                        spawn(nextInt(s))
                    }
                }
            }

            try {
                var referenceSolution = ""
                val refTime = measureNanoTime {
                    referenceSolution = referenceSolver(problem()).joinToString(separator = "\n")
                }

                var comparedSolution = ""
                val compTime = measureNanoTime {
                    comparedSolution = comparedSolver(problem()).joinToString(separator = "\n")
                }

                totalReferenceTimeNanos += refTime
                totalComparedTimeNanos += compTime
                successfulRuns++

                return Pair(referenceSolution, comparedSolution)
            } catch (_: IllegalArgumentException) {
                return Pair("illegal spawn", "illegal spawn")
            }
        }


    }

    fun printPerformanceReport() {
        println("valid runs: $successfulRuns")

        // Convert nanoseconds to milliseconds for human readability
        val avgRefMs = (totalReferenceTimeNanos / successfulRuns.toDouble()) / 1_000_000.0
        val avgCompMs = (totalComparedTimeNanos / successfulRuns.toDouble()) / 1_000_000.0

        println("\n--- Fuzzer Performance Report ---")
        println("Valid Graphs Tested: $successfulRuns")
        println(String.format("Reference Solver Avg: %.4f ms", avgRefMs))
        println(String.format("Compared Solver Avg:  %.4f ms", avgCompMs))
        println("---------------------------------")
    }
}

