import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  Compilation:  javac Deque.java
 *  Execution:    java Deque filename
 *  Dependencies: -
 *
 *  The {@code Deque} class represents a data structure that can function either as a stack or a queue.
 *
 *  This implementation uses a linked list.
 *
 *  The client of this class takes a filename as a command-line argument. It returns on screen both the ordered and
 *  reversed order of the input strings.
 *
 *  @author Rocío Byron
 *  created on 2017/08/10
 */

public class Deque<Item> implements Iterable<Item> {
    private int n;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    public Deque() {
        // construct an empty deque
        first  = null;
        last = null;
        n = 0;
        assert check();
    }

    public boolean isEmpty() {
        // is the deque empty?
        return (first == null);
    }

    public int size() {
        // return the number of items on the deque
        return n;
    }

    public void addFirst(Item item) {
        // add the item to the front

        if (item == null) throw new IllegalArgumentException();

        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        n++;

        if (n == 1) {
            last = first;
        }
        else {
            oldfirst.prev = first;
        }
    }

    public void addLast(Item item) {
        // add the item to the end

        if (item == null) throw new IllegalArgumentException();

        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        last.next = null;
        n++;

        if (n == 1) {
            first = last;
        }
        else {
            oldlast.next = last;
        }
    }

    public Item removeFirst() {
        // remove and return the item from the front (loitering?)

        if (isEmpty()) throw new NoSuchElementException();

        Item item = first.item;

        first = first.next;

        if (n != 1) first.prev = null;
        n--;

        if (n == 0) last = null;

        assert check();
        return item;
    }

    public Item removeLast() {
        // remove and return the item from the end

        if (isEmpty()) throw new NoSuchElementException();

        Item item = last.item;

        last = last.prev;
        if (n != 1) last.next = null;
        n--;

        if (n == 0) first = null;

        assert check();
        return item;
    }

    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return (current != null);
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // check internal invariants
    private boolean check() {
        // check a few properties of instance variable 'first'
        if (n < 0) {
            return false;
        }
        if (n == 0) {
            if (first != null) return false;
        }
        else if (n == 1) {
            if (first == null)      return false;
            if (first.next != null) return false;
        }
        else {
            if (first == null)      return false;
            if (first.next == null) return false;
        }

        // check internal consistency of instance variable n
        int numberOfNodes = 0;
        for (Node x = first; x != null && numberOfNodes <= n; x = x.next) {
            numberOfNodes++;
        }
        return (numberOfNodes == n);
    }

    // unit testing
    public static void main(String[] args) {

        Deque<String> deck = new Deque<>();
        In in = new In(args[0]);


        // enqueue word by word
        while (!in.isEmpty()) {
            deck.addFirst(in.readString());
        }

        // pop (deque as a stack)
        StdOut.println("Pop words (remove from the front): ");
        int k = deck.size();

        for (int i = 0; i < k; i++) {
            StdOut.println(deck.removeFirst());
        }

        // Same process, as a queue
        in = new In(args[0]);

        // enqueue word by word
        while (!in.isEmpty()) {
            deck.addFirst(in.readString());
        }

        // dequeue (deque as a queue)
        StdOut.println("Dequeue words (remove from the end): ");
        k = deck.size();

        for (int i = 0; i < k; i++) {
            StdOut.println(deck.removeLast());
        }
    }
}