public class LinkedListDeque<T> {

    private class Node {
        public Node prev;
        public Node next;
        public T value;

        public Node() {

        }

        public Node(Node p, Node n, T v) {
            prev = p;
            next = n;
            value = v;
        }

        public T get(int index) {
            if (index == 0) {
                return value;
            }
            return next.get(index - 1);
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node();
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        sentinel.value = null;
    }

    public void addFirst(T item) {
        size += 1;
        sentinel.next.prev = new Node(sentinel, sentinel.next, item);
        sentinel.next = sentinel.next.prev;
    }

    public void addLast(T item) {
        size += 1;
        sentinel.prev.next = new Node(sentinel.prev, sentinel, item);
        sentinel.prev = sentinel.prev.next;
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
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.value);
            if (p.next != sentinel) {
                System.out.print(" ");
            }
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node temp = sentinel.next;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        return temp.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node temp = sentinel.prev;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return temp.value;
    }

    public T get(int index) {
        Node p = sentinel.next;
        int i = 0;
        while (p != sentinel) {
            if (i == index) {
                return p.value;
            }
            p = p.next;
            i += 1;
        }
        return null;
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return sentinel.next.get(index);
    }
}
