/**
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats n T
 *  Dependencies: Percolation.java StdStats.java StdRandom.java
 *
 *  This program takes the name of a file as a command-line argument.
 *
 *  The {@code Percolation} class represents a data type for representing a grid of blocked or open sites.
 *
 *  @author Rocío Byron
 *  created on 2017/06/18
 **/

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

    private double[] p;
    private int tIn;
    private double mean = 0;
    private double stddev = 0;

    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        else {
            p = new double[trials];
            tIn = trials;

            for (int t = 0; t < trials; t++) {
                Percolation grid = new Percolation(n);
                int nSquare = (int) Math.pow(n, 2);

                while (!grid.percolates()) {
                    int i = StdRandom.uniform(1, n+1);
                    int j = StdRandom.uniform(1, n+1);
                    grid.open(i, j);
                }

                p[t] = grid.numberOfOpenSites()/Math.pow(n, 2.0);
            }
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        mean = StdStats.mean(p);

        return mean;
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        stddev = StdStats.stddev(p);
        return stddev;
    }

    public double confidenceLo() {
        // low endpoint of 95% confidence interval
        double low = mean - 1.96*stddev/Math.sqrt(tIn);
        return low;
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        double high = mean + 1.96*stddev/Math.sqrt(tIn);
        return high;
    }

    public static void main(String[] args) {
        // test client
        int n = Integer.parseInt(args[0]); // size of grid
        int tIn = Integer.parseInt(args[1]); // number of experiments
        PercolationStats stats = new PercolationStats(n, tIn);
        System.out.format("mean = %.5f\n", stats.mean());
        System.out.format("stddev = %.5f\n", stats.stddev());
        System.out.format("95%% confidence interval = [%.5f, %.5f]\n",
                stats.confidenceLo(), stats.confidenceHi());
    }

}
