
public class OffByN implements CharacterComparator {
    
    int byN;
    public OffByN(int n) {
        byN = n;
    }
    
    @Override
    public boolean equalChars(char x, char y) { 
        int diff = Math.abs(x - y);
        if (diff == byN) {
            return true;
        }
        return false;
    }
    
}