# Seam carving

The program `SeamCarver` takes the input file name as command-line argument and allows to carve seams (lines of pixels)
with minimum energy (i.e. minimum loss of content in the picture). The carving process is controlled via terminal.
For more information, visit the [official assignment description](http://coursera.cs.princeton.edu/algs4/assignments/seam.html).

Key concepts: Edge-weighted graphs, directed acyclic graphs, shortest path algorithms.

## How to compile and run

### Mac OS/ Linux

```
$ javac -cp "../lib/*" SeamCarver.java
$ java -cp "../lib/*:." SeamCarver "./input/beach.jpg"
```

### Windows

```
$ javac -cp "../lib/*" SeamCarver.java
$ java -cp "../lib/*;." SeamCarver "./input/beach.jpg"
