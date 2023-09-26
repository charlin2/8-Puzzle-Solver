import org.junit.Assert;
import org.junit.Test;

public class EightPuzzleTester {
    /**
     * Test basic functions of 8-puzzle by
     * comparing expected output to actual output
     */
    @Test
    public void testEightPuzzleOperations() {
        // 8-puzzle is in solved state by default
        EightPuzzle p = new EightPuzzle();
        Assert.assertEquals("012345678", EightPuzzle.gridToString(p));

        // Test up, down, left, right

        // Invalid moves return false and do not change state
        Assert.assertFalse(p.up());
        Assert.assertFalse(p.left());
        Assert.assertEquals("012345678", EightPuzzle.gridToString(p));

        // Valid moves change state
        Assert.assertTrue(p.right());
        Assert.assertTrue(p.down());
        Assert.assertEquals("142305678", EightPuzzle.gridToString(p));
        
        // Test setState
        Assert.assertFalse(p.setState(new String[] {"112", "456", "780"}));
        Assert.assertTrue(p.setState(new String[] {"123", "456", "780"}));

        // Invalid moves return false and do not change state
        Assert.assertFalse(p.right());
        Assert.assertFalse(p.down());

        // Valid moves change state
        Assert.assertTrue(p.up());
        Assert.assertTrue(p.left());
        Assert.assertEquals("123405786", EightPuzzle.gridToString(p));
    }
}