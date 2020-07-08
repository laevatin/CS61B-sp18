public class ArrayDeque<T> {

    private T a[];
    private int size;
    private int capacity;
    private int first;
    private int last;

    public ArrayDeque() {
        /** The data in array a is from first to last - 1 */
        capacity = 8;
        size = 0;
        first = 4;
        last = 4;
        a = (T []) new Object[8];
    }

    public ArrayDeque(ArrayDeque<T> other) {
        capacity = other.capacity;
        size = other.size;
        first = other.first;
        last = other.last;
        a = (T []) new Object[capacity];
        System.arraycopy(other.a, 0, a, 0, capacity);
    }

    public void addFirst(T item) {
        if (first == 0) {
            resize();
        }
        first -= 1;
        a[first] = item;
        size++;
    }

    public void addLast(T item) {
        if (last == capacity) {
            resize();
        }
        a[last] = item;
        last += 1;
        size++;
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

    public int capacity() {
        return capacity;
    }

    public void printDeque() {
        for (int i = first; i < last - 1; i++) {
            System.out.print(a[i]);
            System.out.print(" ");
        }
        System.out.println(a[last - 1]);
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T temp = a[first];
        a[first] = null;
        first += 1;
        size -= 1;
        if (size < capacity / 2) {
            resize();
        }
        return temp;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T temp = a[last];
        a[last] = null;
        last -= 1;
        size -= 1;
        if (size < capacity / 2) {
            resize();
        }
        return temp;
    }

    public T get(int index) {
        return a[first + index];
    }

    public void resize() {
        if (size < capacity / 2) {
            if (capacity == 8) {
                return;
            }
            capacity /= 2;
            T[] src = a;
            a = (T []) new Object[capacity];
            System.arraycopy(src, first, a, 0, size);
            first = 0;
            last = first + size;
        } else {
            capacity *= 2;
            T[] src = a;
            a = (T []) new Object[capacity];
            if (first == 0) {
                System.arraycopy(src, 0, a, capacity / 4, size); 
            } else {
                System.arraycopy(src, first, a, capacity / 4, size);
            }
            first = capacity / 4;
            last = first + size;
        }
    }
}