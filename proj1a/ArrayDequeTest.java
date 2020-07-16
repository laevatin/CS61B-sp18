public class ArrayDequeTest {
    
    public static void main(String[] args) {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(0);
         deque.addLast(1);
         deque.addLast(2);
         deque.addLast(3);
         deque.addLast(4);
         deque.addLast(5);
         System.out.println(deque.removeFirst());
         deque.addLast(7);
         deque.addLast(8);
         deque.addLast(9);
        
    }
}
