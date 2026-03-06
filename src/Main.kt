import IO.parseProblem
import IO.writeSolution
import solvers.Solver
import solvers.naiveSolver
import java.util.Scanner


fun main() {
    val problem = parseProblem(Scanner(System.`in`))
    println(problem)
    val solution = naiveSolver(problem)
    writeSolution(solution)
}