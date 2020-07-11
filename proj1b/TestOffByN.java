import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByN(2);

    @Test
    public void testOffByN() {
        assertTrue(offByOne.equalChars('a', 'c'));
        assertTrue(offByOne.equalChars('b', 'd'));
        assertTrue(offByOne.equalChars('e', 'c'));
        assertFalse(offByOne.equalChars('a', 'b'));
    }
    
}
