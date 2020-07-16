package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        
        for (int i = 0; i < 10; i += 1) {
            arb.enqueue(i);
        }
        arb.dequeue();
        arb.enqueue(10);
        for (int i = 1; i < 11; i += 1) {
            assertEquals(arb.dequeue(), (Integer) i);
        }

        ArrayRingBuffer<Integer> arb1 = new ArrayRingBuffer<>(10);
        for (int i = 0; i < 10; i += 1) {
            arb1.enqueue(i);
        }

        for (int i: arb1) {
            System.out.println(i);
        }

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
