# Deques and randomized queues

The `RandomizedQueue` class implements a queue with a resizing array, that allows for random sampling and iteration. The cliento of the class takes the input file name as command-line argument and prints out the strings in the input file in random order. 

The `Deque` class implements a double-ended queue with a linked list, that allows to add and remove elements on both ends (either as a stack or as a queue). The client takes the input file name as command-line argument and prints out the strings in the input file, first in order (popping the strings) and then in reversed order (dequeuing the strings). 

For more information, visit the [official assignment description](http://coursera.cs.princeton.edu/algs4/assignments/queues.html).

## How to compile and run

### Mac OS/ Linux

__Randomized queue__
```
$ javac -cp "../lib/*" RandomizedQueue.java
$ java -cp "../lib/*:." RandomizedQueue "./input/tinytale.txt"
```

__Deque__
```
$ javac -cp "../lib/*" Deque.java
$ java -cp "../lib/*:." Deque "./input/tinytale.txt"
```

### Windows

__Randomized queue__
```
$ javac -cp "../lib/*" RandomizedQueue.java
$ java -cp "../lib/*;." RandomizedQueue "./input/tinytale.txt"
```

__Deque__
```
$ javac -cp "../lib/*" Deque.java
$ java -cp "../lib/*;." Deque "./input/tinytale.txt"
```
