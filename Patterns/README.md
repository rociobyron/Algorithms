# Pattern recognition

The class `FastCollinearPoints` takes an input file name as command-line argument, finds all segments connecting 4+ points in the file, and returns a figure with the points and the segments drawn on it.

The search of collinear points takes time on the order of $O(n^{2} + log(n))$ instead of $O(n^{4})$ (all-with-all brute approach) thanks to the application of a quicksort algorithm.

For more information, visit the [official assignment description](http://coursera.cs.princeton.edu/algs4/assignments/collinear.html).

Key concepts: Sorting algorithms, compareTo() method implementation.

## How to compile and run

### Mac OS/ Linux

```
$ javac -cp "../lib/*" LineSegment.java Point.java FastCollinearPoints.java
$ java -cp "../lib/*:." FastCollinearPoints "./input/input50.txt"
```

### Windows

```
$ javac -cp "../lib/*" LineSegment.java Point.java FastCollinearPoints.java
$ java -cp "../lib/*;." FastCollinearPoints "./input/input50.txt"
```
