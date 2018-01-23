# Percolation

The program `PercolationStats` takes two command-line arguments, n and T, and performs T number of experiments on a nxn grid. It prints out the mean, standard deviation and 95% confidence interval of the percolation threshold. For more information, visit the [official assignment description](http://coursera.cs.princeton.edu/algs4/assignments/percolation.html).

## How to compile and run

### Mac OS/ Linux

```
$ javac -cp ../lib/* Percolation.java PercolationStats.java
$ java -cp "../lib/*:." PercolationStats 20 500
```

### Windows

```
$ javac -cp ../lib/* Percolation.java PercolationStats.java
$ java -cp "../lib/*;." PercolationStats 20 500
```
