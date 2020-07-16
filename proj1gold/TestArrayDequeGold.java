import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testArrayDeque() {
        double af = 0.5;
        double rf = 0.5;
        double increaserate = 0.4;
        int n = 20;

        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        String message = "";

        for (int i = 0; i < n; i++) {
            double random = StdRandom.uniform();

            if (random < af) {
                sad1.addFirst(i);
                message += ("addFirst(" + i + ")"); 
                ads1.addFirst(i);
            } else {
                ads1.addLast(i);
                message += ("addLast(" + i + ")"); 
                sad1.addLast(i);
            }

            double random1 = StdRandom.uniform();

            if (random1 > increaserate) {
                if (random1 < rf && !sad1.isEmpty()) {
                    message += ("removeFirst()"); 
                    assertEquals(message, ads1.removeFirst(), sad1.removeFirst());
                } else {
                    message += ("removeLast()"); 
                    assertEquals(message, ads1.removeLast(), sad1.removeLast());
                }
            }
        }
    }
}
