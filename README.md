# CSDS 391: P1 (8-Puzzle)
An 8-puzzle solver written in Java.  This project is capable of solving 8-puzzles using A* search and beam search.

### How to use
The entry-point of this program is through the *EightPuzzle.java* file.  This program takes **one** .txt file as an argument.

The .txt file should have one command and its respective arguments per line. A list of commands is specified below:

- **setState \<state\>** - *state* should be in the format "012 345 678". Note that setting the state this way may result in an unsolvable board. This method will fail if there are any invalid or duplicate tiles.
- **printState** - Prints the current state of the board.
- **move \<direction\>** - *direction* is either "up", "down", "left", or "right". Moves the blank tile in the specified direction.
- **randomizeState \<n\>** - Performs *n* random moves from the solved state.
- **solve A-star \<heuristic\>** - *heuristic* is either "h1" or "h2".  Solves the puzzle using A* and prints the solution.
- **solve beam \<k\>** - *k* is the number of states for beam search to store at each iteration.
- **maxNodes \<n\>** - *n* is the max number of nodes to be considered during the duration of a search.
