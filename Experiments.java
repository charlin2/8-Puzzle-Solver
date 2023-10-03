public class Experiments {
    public static void main(String[] args) throws Exception {
        EightPuzzle p = new EightPuzzle();
        // EightPuzzle.setMaxNodes(1000);

        int solveCount = 0;
        int moveSum = 0;
        long start = 0;
        long end = 0;
        long time = 0;
        for (int i = 0; i < 1000; i++) {
            p.randomize(100);
            try {
                int moves = -1;
                start = System.nanoTime();
                // moves = p.solveAStar("h2");
                moves = p.solveBeam(1);
                end = System.nanoTime();
                time += end - start;
                solveCount++;
                if (moves >= 0) {
                    moveSum += moves;
                }
            } catch (OutOfMemoryError e) {
                continue;
            } catch (Exception e) {
                continue;
            }
        }
        double avg = 0;
        if (moveSum == 0) {
            avg = -1;
        } else {
            avg = (double)moveSum/solveCount;
        }

        System.out.println("Average number of moves: " + avg);
        System.out.println("Number of solved puzzles: " + solveCount);
        System.out.println("Average time taken: " + (time/1000));
    }
}
