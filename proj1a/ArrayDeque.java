public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int capacity;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        /** The data in array a is from first to last - 1 */
        capacity = 8;
        size = 0;
        nextFirst = 7;
        nextLast = 0;
        items = (T[]) new Object[8];
    }

    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst = Math.floorMod(nextFirst - 1, capacity);
        size++;
        if (size == capacity) {
            expand();
        }
    }

    public void addLast(T item) {
        items[nextLast] = item;
        nextLast = Math.floorMod(nextLast + 1, capacity);
        size++;
        if (size == capacity) {
            expand();
        }
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int start = Math.floorMod(nextFirst + 1, capacity * 2);
        int end = Math.floorMod(nextLast - 1, capacity * 2);
        if (start > end) {
            for (int i = start; i < capacity; i++) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            for (int i = 0; i <= end; i++) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
        } else {
            for (int i = start; i <= end; i++) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size--;
        nextFirst = Math.floorMod(nextFirst + 1, capacity);
        T temp = items[nextFirst];
        items[nextFirst] = null;
        if (size < capacity / 2) {
            shrink();
        }
        return temp;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size--;
        nextLast = Math.floorMod(nextLast - 1, capacity);
        T temp = items[nextLast];
        items[nextLast] = null;
        if (size < capacity / 2) {
            shrink();
        }
        return temp;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[Math.floorMod(nextFirst + 1 + index, capacity)];
    }

    private void expand() {
        capacity *= 2;
        T[] src = items;
        items = (T[]) new Object[capacity];
        int start = Math.floorMod(nextFirst + 1, capacity / 2);
        int end = Math.floorMod(nextLast - 1, capacity / 2);
        if (start > end) {
            System.arraycopy(src, start, items, capacity / 2 + start, capacity / 2 - start);
            nextFirst += capacity / 2;
            System.arraycopy(src, 0, items, 0, end + 1);
        } else {
            System.arraycopy(src, 0, items, 0, size);
            nextFirst = capacity - 1;
            nextLast = size;
        }
    }

    private void shrink() {
        if (capacity == 8) {
            return;
        }
        capacity /= 2;
        T[] src = items;
        items = (T[]) new Object[capacity];
        int start = Math.floorMod(nextFirst + 1, capacity * 2);
        int end = Math.floorMod(nextLast - 1, capacity * 2);
        if (start > end) {
            System.arraycopy(src, start, items, start - capacity, capacity * 2 - start);
            nextFirst -= capacity;
            System.arraycopy(src, 0, items, 0, end + 1);
        } else {
            System.arraycopy(src, start, items, 0, size);
            nextLast = end - start + 1;
            nextFirst = capacity - 1;
        }
    }
}
