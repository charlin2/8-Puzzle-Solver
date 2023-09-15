import java.util.*;

public class EightPuzzle {
    // Using matrix to represent puzzle
    private int[][] grid;

    // row of the space
    private int r;

    // column of the space
    private int c;

    /**
     * Constructs a new 8-Puzzle in the solved state
     */
    public EightPuzzle() {
        grid = new int[][] {{0,1,2}, {3,4,5}, {6,7,8}};
        r = 0;
        c = 0;
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
        } else {
            grid[r][c] = grid[--r][c];
            grid[r][c] = 0;
        }
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
        } else {
            grid[r][c] = grid[++r][c];
            grid[r][c] = 0;
        }
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
        } else {
            grid[r][c] = grid[r][--c];
            grid[r][c] = 0;
        }
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
        } else {
            grid[r][c] = grid[r][++c];
            grid[r][c] = 0;
        }
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
            List<Integer> moves = getValidMoves();
            int move = moves.get((int)(Math.random()*moves.size()));
            if (move == 1) {
                up();
            } else if (move == 2) {
                down();
            }  else if (move == 3) {
                left();
            } else {
                right();
            }
        }
    }

    /**
     * Helper method for randomize
     * 
     * @return List of valid moves
     */
    private List<Integer> getValidMoves() {
        List<Integer> moves = new LinkedList<>();
        // can move up
        if (r != 0)
            moves.add(1);
        // can move down
        if (r != 2)
            moves.add(2);
        // can move left
        if (c != 0)
            moves.add(3);
        // can move right
        if (c != 2)
            moves.add(4);
        return moves;
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

    public static void main(String[] args) {
        EightPuzzle puzzle = new EightPuzzle();
        String[] rows = {"234", "156", "078"};
        System.out.println(puzzle.setState(rows));
        System.out.println(puzzle.getValidMoves());
        System.out.println(puzzle.toString());
        puzzle.randomize(30);
        puzzle.toString();
    }
}