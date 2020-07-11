/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {
    
    public static void main(String[] args) {
        int minLength = 4;
        //In in = new In("data/words.txt");
        Palindrome palindrome = new Palindrome();
        int max = 0;
        for (int i = 0; i < 26; i++) {
            int count = 0;
            CharacterComparator cc = new OffByN(i);
            In in = new In("data/words.txt");
            while (!in.isEmpty()) {
                String word = in.readString();
                if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
                    count++;
                    //System.out.println(word);
                }
            }
            if (count > max) {
                max = count;
            }
            System.out.println(count);
        }
        
    }
}
