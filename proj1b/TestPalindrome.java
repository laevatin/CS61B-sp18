import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testisPalindrome() {
        boolean ans1 = palindrome.isPalindrome("persiflage");
        assertFalse(ans1);
        boolean ans2 = palindrome.isPalindrome("abccba");
        assertTrue(ans2);
        boolean ans3 = palindrome.isPalindrome("acdca");
        assertTrue(ans3);
        boolean ans4 = palindrome.isPalindrome("adddddddddddda");
        assertTrue(ans4);
        boolean ans5 = palindrome.isPalindrome("0");
        assertTrue(ans5);
        boolean ans6 = palindrome.isPalindrome("");
        assertTrue(ans6);
        boolean ans7 = palindrome.isPalindrome("YYy");
        assertFalse(ans7);
    }
    
    @Test
    public void testIsPalindromeCc() {
        OffByOne obo = new OffByOne();
        assertTrue(palindrome.isPalindrome("", obo));
        assertTrue(palindrome.isPalindrome("a", obo));
        assertTrue(palindrome.isPalindrome("flake", obo));
        assertTrue(palindrome.isPalindrome("zyzy", obo));
        assertTrue(palindrome.isPalindrome("yyxz", obo));
        assertTrue(palindrome.isPalindrome("yyyxz", obo));
        assertFalse(palindrome.isPalindrome("aa", obo));
        assertFalse(palindrome.isPalindrome("xyz", obo));
        assertFalse(palindrome.isPalindrome("aa", obo));
        assertFalse(palindrome.isPalindrome("zxzx", obo));
    }
}
