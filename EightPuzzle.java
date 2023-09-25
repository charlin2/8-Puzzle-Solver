import java.util.*;

public class EightPuzzle implements Comparable<EightPuzzle> {
    // Using matrix to represent puzzle
    private int[][] grid;

    // Row of the space
    private int r;

    // Column of the space
    private int c;

    // Reference to parent state 
    private EightPuzzle parent;

    // Previous move
    private String prevMove;

    // Value of state 
    private int value;

    // Max number of nodes to consider during search
    private static int maxNodes = Integer.MAX_VALUE;

    /**
     * Constructs a new 8-Puzzle in the solved state
     */
    public EightPuzzle() {
        grid = new int[][] {{0,1,2}, {3,4,5}, {6,7,8}};
        r = 0;
        c = 0;
        parent = null;
        prevMove = null;
        value = 0;
    }

    /**
     * Set the max number of nodes to be considered during a search
     * 
     * @param n Number of nodes to consider
     */
    public static void setMaxNodes(int n) {
        maxNodes = n;
    }

    /**
     * Sets the state of the 8-Puzzle
     * Note that this does not check if the state is reachable from the goal state
     * 
     * @param rows String array in the format {"012", "345", "678"}
     * @return True if state is successfully set
     */
    public boolean setState(String[] rows) {
        if (!checkFormat(rows)) {
            return false;
        }
        // Save state of original grid in case operation is invalid
        int[][] oldGrid = new int[3][3];
        int i = 0;
        for (int[] row : grid) {
            int j = 0;
            for (int tile : row) {
                oldGrid[i][j++] = tile;
            }
            i++;
        }
        Set<Integer> valueSet = new HashSet<>();
        i = 0;
        for (String row : rows) {
            char[] tiles = row.toCharArray();
            int j = 0;
            for (char tile : tiles) {
                grid[i][j] = tile - '0';
                if (tile - '0' == 0) {
                    r = i;
                    c = j;
                }
                // Check for duplicate values or invalid digit
                if (!valueSet.add(tile - '0') || tile - '0' == 9) {
                    // Reset grid
                    grid = oldGrid;
                    return false;
                }
                j++;
            }
            i++;
        }
        return true;
    }

    /**
     * Helper method to check the formatting of setState input
     * 
     * @param rows setState input
     * @return True if formatting is correct
     */
    private boolean checkFormat(String[] rows) {
        if (rows.length != 3)
            return false;
        for (String row : rows) {
            if (row.length() != 3)
                return false;
        }
        return true;
    }

    /**
     * Moves blank tile up
     * 
     * @return True if blank tile can move up
     */
    public boolean up() {
        if (r == 0) {
            return false;
        }
        grid[r][c] = grid[--r][c];
        grid[r][c] = 0;
        prevMove = "up";
        return true;
    }

    /**
     * Moves blank tile down
     * 
     * @return True if blank tile can move down
     */
    public boolean down() {
        if (r == 2) {
            return false;
        }
        grid[r][c] = grid[++r][c];
        grid[r][c] = 0;
        prevMove = "down";
        return true;
    }

    /**
     * Moves blank tile left
     * 
     * @return True if blank tile can move left
     */
    public boolean left() {
        if (c == 0) {
            return false;
        }
        grid[r][c] = grid[r][--c];
        grid[r][c] = 0;
        prevMove = "left";
        return true;
    }

    /**
     * Moves blank tile right
     * 
     * @return True if blank tile can move right
     */
    public boolean right() {
        if (c == 2) {
            return false;
        }
        grid[r][c] = grid[r][++c];
        grid[r][c] = 0;
        prevMove = "right";
        return true;
    }

    /**
     * Randomly performs n moves from the goal state
     * 
     * @param n Number of random moves to perform
     */
    public void randomize(int n) {
        // Reset grid
        grid = new int[][] {{0,1,2}, {3,4,5}, {6,7,8}};
        r = 0;
        c = 0;
        for (int i = 0; i < n; i++) {
            List<String> moves = getValidMoves();
            String move = moves.get((int)(Math.random()*moves.size()));
            if (move.equals("up")) {
                up();
            } else if (move.equals("down")) {
                down();
            }  else if (move.equals("left")) {
                left();
            } else {
                right();
            }
        }
    }

    /**
     * Returns a list of valid moves for given board state
     * 
     * @return List of valid moves
     */
    public List<String> getValidMoves() {
        List<String> moves = new LinkedList<>();
        // can move up
        if (r != 0)
            moves.add("up");
        // can move down
        if (r != 2)
            moves.add("down");
        // can move left
        if (c != 0)
            moves.add("left");
        // can move right
        if (c != 2)
            moves.add("right");
        return moves;
    }

    /**
     * Solves 8-puzzle using A* search and prints the solution
     * Specify heuristic as "h1" (number of misplaced tiles) or "h2" (manhattan distance)
     * 
     * @param heuristic Either "h1" or "h2"
     */
    public void solveAStar(String heuristic) throws IllegalArgumentException {
        if (heuristic.equals("h1")) {
            System.out.println("Number of moves: " + solveH1());
        } else if (heuristic.equals("h2")) {
            System.out.println("Number of moves: " + solveH2());
        } else {
            throw new IllegalArgumentException("Invalid heuristic");
        }
    }

    /**
     * A* search using h1
     * 
     * @return Number of moves
     * @throws OutOfMemoryError Max node limit exceeded
     */
    private int solveH1() throws OutOfMemoryError {
        // min cost heap
        PriorityQueue<EightPuzzle> pq = new PriorityQueue<>();

        // keep track of visited states
        Map<String, EightPuzzle> visited = new HashMap<>();

        // count of generated nodes
        int nodes = 0;

        // add initial state to pq
        pq.add(this);

        while (!visited.containsKey("012345678") && !pq.isEmpty() && nodes <= maxNodes) {
            EightPuzzle currState = pq.poll();
            String gridString = gridToString(currState);
            if (!visited.containsKey(gridString)) {
                visited.put(gridString, currState);

                List<String> validMoves = currState.getValidMoves();
                for (String move : validMoves) {
                    // generate and add child state if not already visited
                    EightPuzzle child = currState.duplicate();
                    if (move.equals("up")) {
                        child.up();
                    } else if (move.equals("down")) {
                        child.down();
                    } else if (move.equals("left")) {
                        child.left();
                    } else {
                        child.right();
                    }
                    if (!visited.containsKey(gridToString(child))) {
                        int depth = currState.value - h1(currState) + 1;
                        child.parent = currState;
                        child.value = depth + h1(child);
                        pq.add(child);
                        nodes++;
                    }
                }
            }
        }
        
        if (nodes > maxNodes) {
            throw new OutOfMemoryError("Max node limit exceeded.");
        }

        // Extract path
        EightPuzzle trav = null;
        if (visited.containsKey("012345678")) {
            trav = visited.get("012345678");
            int moveCount = 0;
            List<String> path = new LinkedList<>();
            while (trav != null) {
                path.add(trav.prevMove);
                moveCount++;
                trav = trav.parent;
            }
            path.remove(path.size()-1);
            Collections.reverse(path);
            System.out.println(path.toString());
            System.out.println("Nodes considered: " + nodes);
            return moveCount-1;
        }
        System.out.println("No path found.");
        return 0;
    }

    /**
     * Create deep copy of current puzzle state
     * 
     * @return Deep copy of puzzle
     */
    private EightPuzzle duplicate() {
        EightPuzzle copy = new EightPuzzle();
        int[][] copyGrid = new int[3][3];
        int i = 0;
        for (int[] row : grid) {
            int j = 0;
            for (int tile : row) {
                copyGrid[i][j++] = tile;
            }
            i++;
        }
        copy.grid = copyGrid;
        copy.c = c;
        copy.r = r;
        return copy;
    }

    /**
     * Heuristic function based on number of misplaced tiles
     * 
     * @param state State of the board
     * @return Function value
     */
    private static int h1(EightPuzzle state) {
        int misplaced = 0;
        // didn't want to think about how to write loop
        if (state.grid[0][0] != 0) {
            misplaced++;
        }
        if (state.grid[0][1] != 1) {
            misplaced++;
        }
        if (state.grid[0][2] != 2) {
            misplaced++;
        }
        if (state.grid[1][0] != 3) {
            misplaced++;
        }
        if (state.grid[1][1] != 4) {
            misplaced++;
        }
        if (state.grid[1][2] != 5) {
            misplaced++;
        }
        if (state.grid[2][0] != 6) {
            misplaced++;
        }
        if (state.grid[2][1] != 7) {
            misplaced++;
        }
        if (state.grid[2][2] != 8) {
            misplaced++;
        }
        return misplaced;
    }

    /**
     * A* search using h2
     * 
     * @return Number of moves
     * @throws OutOfMemoryError Max node limit exceeded
     */
    private int solveH2() throws OutOfMemoryError {
        // min cost heap
        PriorityQueue<EightPuzzle> pq = new PriorityQueue<>();

        // keep track of visited states
        Map<String, EightPuzzle> visited = new HashMap<>();

        // count of generated nodes
        int nodes = 0;

        // add initial state to pq
        pq.add(this);

        while (!pq.isEmpty() && !visited.containsKey("012345678") && nodes <= maxNodes) {
            EightPuzzle currState = pq.poll();
            String gridString = gridToString(currState);
            if (!visited.containsKey(gridString)) {
                visited.put(gridString, currState);
                List<String> validMoves = currState.getValidMoves();
                for (String move : validMoves) {
                    // generate and add child state if not already visited
                    EightPuzzle child = currState.duplicate();
                    if (move.equals("up")) {
                        child.up();
                    } else if (move.equals("down")) {
                        child.down();
                    } else if (move.equals("left")) {
                        child.left();
                    } else {
                        child.right();
                    }
                    if (!visited.containsKey(gridToString(child))) {
                        int depth = currState.value - h2(currState) + 1;
                        child.parent = currState;
                        child.value = depth + h2(child);
                        pq.add(child);
                        nodes++;
                    }
                }
            }
        }
        
        if (nodes > maxNodes) {
            throw new OutOfMemoryError("Max node limit exceeded.");
        }

        // Extract path
        EightPuzzle trav = null;
        if (visited.containsKey("012345678")) {
            trav = visited.get("012345678");
            int moveCount = 0;
            List<String> path = new LinkedList<>();
            while (trav != null) {
                path.add(trav.prevMove);
                moveCount++;
                trav = trav.parent;
            }
            path.remove(path.size()-1);
            Collections.reverse(path);
            System.out.println(path.toString());
            System.out.println("Nodes considered: " + nodes);
            return moveCount-1;
        }
        System.out.println("No path found.");
        return 0;
    }

    /**
     * Heuristic function based on Manhattan distance of tiles to correct spot
     * 
     * @param state State of the board
     * @return Function value
     */
    private static int h2(EightPuzzle state) {
        int[][] spots = {{0,0},{0,1},{0,2},{1,0},{1,1},{1,2},{2,0},{2,1},{2,2}};
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] spot = spots[state.grid[i][j]];
                sum += Math.abs(spot[0] - i) + Math.abs(spot[1] - j);
            }
        }
        return sum;
    }

    /**
     * Solves 8-Puzzle using beam search and prints the solution
     * This version of beam search uses h2
     * 
     * @param k Number of states to be considered at each iteration
     */
    public void solveBeam(int k) {
        // min cost heap storing the best k nodes
        PriorityQueue<EightPuzzle> best = new PriorityQueue<>();

        // list of open nodes
        List<EightPuzzle> open = new ArrayList<>();

        // keep track of visited states
        Map<String, EightPuzzle> visited = new HashMap<>();

        // count of generated nodes
        int nodes = 0;

        // flag for goal state
        boolean solved = false;

        if (gridToString(this).equals("012345678")) {
            System.out.println("Number of moves: 0");
            return;
        }

        open.add(this);

        while (!open.isEmpty() && !solved && nodes <= maxNodes) {
            for (EightPuzzle currState : open) {
                String gridString = gridToString(currState);
                if (!visited.containsKey(gridString)) {
                    visited.put(gridString, currState);
                    List<String> validMoves = getValidMoves();
                    for (String move : validMoves) {
                        EightPuzzle child = currState.duplicate();
                        if (move.equals("up")) {
                            child.up();
                        } else if (move.equals("down")) {
                            child.down();
                        } else if (move.equals("left")) {
                            child.left();
                        } else {
                            child.right();
                        }
                        // using h2 for beam search
                        if (!visited.containsKey(gridToString(child))) {
                            int depth = currState.value - h2(currState) + 1;
                            child.parent = currState;
                            child.value = depth + h2(child);
                            best.add(child);
                            nodes++;
                        }
                    }
                }
            }
            open.clear();

            // add k best children into consideration
            for (int i = 0; !best.isEmpty() && i < k; i++) {
                open.add(best.poll());
                if (gridToString(open.get(i)).equals("012345678")) {
                    solved = true;
                    visited.put("012345678", open.get(i));
                }
            }
        }

        if (nodes > maxNodes) {
            throw new OutOfMemoryError("Max node limit exceeded.");
        }

        EightPuzzle trav = visited.get("012345678");
        if (trav != null) {
            List<String> path = new LinkedList<>();
            while (trav != null) {
                path.add(trav.prevMove);
                trav = trav.parent;
            }
            Collections.reverse(path);
            path.remove(0);
            System.out.println(path.toString());
            System.out.println("Nodes considered: " + nodes);
            System.out.println("Number of moves: " + path.size());
            return;
        }

        System.out.println("No path found.");
    }

    /**
     * Convert 2D array board to string
     * 
     * @param p puzzle to convert
     * @return String of board state
     */
    private static String gridToString(EightPuzzle p) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : p.grid) {
            for (int x : row) {
                sb.append(x);
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder printOut = new StringBuilder();
        for (int[] row : grid) {
            for (int tile : row) {
                printOut.append(tile + " ");
            }
            printOut.append("\n");
        }
        System.out.println(printOut.toString());
        return printOut.toString();
    }

    @Override
    public int compareTo(EightPuzzle o) {
        return this.value - o.value;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EightPuzzle) {
            EightPuzzle p = (EightPuzzle) o;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] != p.grid[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        EightPuzzle puzzle = new EightPuzzle();
        String[] rows = {"012", "345", "678"};
        puzzle.setState(rows);
        puzzle.randomize(50);
        puzzle.toString();
        // EightPuzzle.setMaxNodes(500);
        puzzle.solveAStar("h2");
        // puzzle.solveAStar("h1");
        puzzle.solveBeam(Integer.MAX_VALUE);
    }
}