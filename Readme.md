# JetBrains Compiler Optimizations in Kotlin/Native Internship Task

This repository contains the solution to the internship task, complete with input 
handling, separation of concerns, domain builders, tests, and a benchmarks. 
The final submission is a merged single-file version of this project.

## The Solvers
I implemented and benchmarked:
- Naive approach (BFS)
- Exploring naive (BFS + prioritization of unvisited nodes)
- Exploring worklist (BFS + stack + prioritization of unvisited nodes)
- Iterative Reverse Post-Order (RPO)

with the with differential fuzzer and my hand-made examples. 
Below are the performance results for 10 000 randomized graphs with 
`0 < S < 100` and `0 < T < 1000` are as follows:

| Algorithm | Average Time (ms) |
| :--- | :--- |
| Naive | 4.0 |
| Exploring Naive | 3.8 |
| Exploring Worklist | 1.9 |
| Iterative RPO | 1.1 |

Based on these benchmarks, the final submitted solution utilizes iterative RPO.

## Packages
- `io` - Parses the problem from a `Scanner` and handles `stdout` formatting
- `problem` - Domain builders and entity representations
- `solvers` - Implementations of solver algorithms
- `test` - Unit tests, differential fuzzer

## Technical Notes and Trade-offs
* **RPO optimizations:** The RPO was designed to be fully iterative rather than 
recursive to prevent a stack overflow. The DFS uses a `cachedIndex` on the `Station`
to track visited neighbors. This prevents redundant rescanning of previously checked 
nodes when backtracking.


* **Null Safety:** The parser filters out invalid or non-existent stations and tracks 
during the build phase. Therefore, the use of the `!!` operator in the solvers is 
safe and used deliberately to avoid the performance overhead.


* **Representing cargo as `Set<Int>`** I considered using `BitSet` or primitive-type 
arrays instead of `Set<Int>` to avoid reallocating memory. 
The problem specification did not guarantee that cargo IDs would be contiguous 
(or even valid non-negative integers), utilizing `BitSet` requires
implementing some sort of mapping. I opted for standard sets to maintain 
cleaner code that safely handles _any_ integers out of the box.


* **Design Patterns:** While analyzing the Kotlin compiler, I noticed the heavy 
utilization of the Visitor and Builder patterns. I implemented a DSL builder for safe 
graph construction, but opted against the Visitor pattern for stations 
to avoid bloating the code for a strictly algorithmic assessment.


* **IO formatting:** The task did not specify an output format or 
a programming language. I chose Kotlin and formatted the output  
using `Station.toString()`, with sorted cargo IDs for predictability of results.
I chose to format outputs in the solution as a new line for every station:
`station_id cargo1 cargo2 ... cargoN`
In this repo I there is a `->` between `station_id` and `cargoX` for readability and
`,` between cargo types.