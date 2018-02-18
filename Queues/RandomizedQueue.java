import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

/**
 *  Compilation:  javac RandomizedQueue.java
 *  Execution:    java RandomizedQueue filename
 *  Dependencies: StdRandom.java
 *
 *  The {@code RandomizedDeque} class represents a data structure similar to a queue, except that the item removed is
 *  chosen uniformly at random from items in the data structure.
 *
 *  This implementation uses a resizing array, which double the underlying array
 *  when it is full and halves the underlying array when it is one-quarter full.
 *
 *  The client of this class takes a filename as a command-line argument. It returns on screen a random permutation of
 *  the strings contained in the file.
 *
 *  @author Rocío Byron
 *  created on 2017/08/10
 */

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;       // array of items
    private int n;          // number of elements on queue
    private int head;       // position of the first element
    private int tail;       // position of the last element

    public RandomizedQueue() {
        // construct an empty randomized queue
        a = (Item[]) new Object[2];
        n = 0;
        head = 0;
        tail = 0;
    }

    public boolean isEmpty() {
        // is the queue empty?
        return (n == 0);
    }

    public int size() {
        // return the number of items on the queue
        return n;
    }

    private void resize(int capacity) {
        // resize the underlying array holding the elements
        Item[] temp = (Item[]) new Object[capacity];
        int t = 0;

        for (int i = head; i < tail + 1; i++) {
            if (a[i] != null) {
                temp[t] = a[i];
                t++;
            }
        }

        head = 0;
        tail = n - 1;

        a = temp;
    }

    public void enqueue(Item item) {
        // add the item
        if (item == null) throw new IllegalArgumentException();

        if (n > 0 && tail + 1 == a.length) resize(2 * a.length);  // double size of array if necessary
        if (a[tail] != null) tail++;
        a[tail] = item;                                       // add item
        n++;
    }

    public Item dequeue() {
        // remove and return a random item

        if (isEmpty()) throw new NoSuchElementException("Stack underflow");

        int index = StdRandom.uniform(head, tail + 1);
        Item item = a[index];

        while (item == null) {
            if (index == 0) index = tail;
            else index--;
            item = a[index];
        }

        a[index] = null;                              // to avoid loitering

        if (head == tail) {
            // This was the last element of the array
            head = 0;
            tail = 0;
        }
        else if (index == head) head++;
        else if (index == tail) tail--;
        n--;

        // shrink size of array if necessary
        if (n > 0 && tail + 1 == a.length / 4) resize(a.length / 2);
        else if (n > 0 && head == 3*a.length / 4) resize(a.length/2);
        return item;
    }

    public Item sample() {
        // return (but do not remove) a random item

        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int index = StdRandom.uniform(head, tail + 1);
        Item item = a[index];

        while (item == null) {
            if (index == 0) index = tail;
            else index--;
            item = a[index];
        }

        return item;

    }

    public Iterator<Item> iterator() {
        // return an iterator over random items
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Item[] temp = (Item[]) new Object[a.length];
        private int tempHead;
        private int tempTail;
        private int tempN;


        public ListIterator() {

            int t = 0;

            for (int i = 0; i < a.length; i++) {
                temp[t] = a[i];
                t++;
            }

            tempHead = head;
            tempTail = tail;
            tempN = n;
        }

        public boolean hasNext() {
            return (tempN != 0);
        }

        public Item next() {

            if (!hasNext()) throw new NoSuchElementException();

            int index = StdRandom.uniform(tempHead, tempTail + 1);
            Item item = temp[index];

            while (item == null) {
                if (index == 0) index = tempTail;
                else index--;
                item = temp[index];
            }

            temp[index] = null;                              // to avoid loitering

            if (tempHead == tempTail) {
                // This was the last element of the array
                tempHead = 0;
                tempTail = 0;
            }
            else if (index == tempHead) tempHead++;
            else if (index == tempTail) tempTail--;
            tempN--;

            return item;
        }
    }

    // unit testing
    public static void main(String[] args) {

        RandomizedQueue<String> rqueue = new RandomizedQueue<>();
        In in = new In(args[0]);

        // enqueue word by word
        while (!in.isEmpty()) {
            rqueue.enqueue(in.readString());
        }

        // sample an random element of the queue
        StdOut.print("Random sample of the queue: ");
        StdOut.println(rqueue.sample());

        int k = rqueue.size();

        // dequeue in random order
        StdOut.println("All elements in random order:");
        for (int i = 0; i < k; i++) {
            StdOut.println(rqueue.dequeue());
        }
    }
}


