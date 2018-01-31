/**
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation n
 *  Dependencies: WeightedQuickUnionUF.java
 *
 *  This program takes an integer (the size of the percolation grid) as a command-line argument.
 *
 *  The {@code Percolation} class represents a data type for representing a grid of blocked or open sites.
 *
 *  @author Rocío Byron
 *  created on 2017/06/18
 **/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int nGrid;
    private WeightedQuickUnionUF grid;
    private boolean[][] gridOpened;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("size n <= 0");
        }
        else {
            nGrid = n; // size side of grid; needs to include 2 add. virtual nodes
            int nSquare = (int) Math.pow(n, 2.0);

            grid = new WeightedQuickUnionUF(nSquare+2);

            // connects virtual nodes to first and last rows respectively
            for (int i = 1; i <= nGrid; i++) {
                grid.union(0, i);
                grid.union(nSquare+1, nSquare - i + 1);
            }

            gridOpened = new boolean[nGrid+1][nGrid+1];

            for (int i = 0; i < nGrid; i++) {
                for (int j = 0; j < nGrid; j++) {
                    gridOpened[i][j] = false; // array to mark open sites
                }
            }
        }
    }

    // checks indexes within grid boundaries
    private void indexCheck(int row, int col) {

        if (row <= 0 || col <= 0 || row > nGrid || col > nGrid) {
            throw new IndexOutOfBoundsException("Invalid row or column");
        }
    }

    // converts coordinates to array id
    private int xyTo1D(int row, int col) {
        int id = (row-1)*nGrid + col;
        return id;
    }

    private int[][] neighbour(int row, int col) {

        int id = xyTo1D(row, col);
        int [][] neighbour = new int[4][4];

        neighbour[0][0] = row - 1;
        neighbour[0][1] = col;
        neighbour[1][0] = row + 1;
        neighbour[1][1] = col;
        neighbour[2][0] = row;
        neighbour[2][1] = col - 1;
        neighbour[3][0] = row;
        neighbour[3][1] = col + 1;

        return neighbour;
    }

    public void open(int row, int col) {
        indexCheck(row, col);

        int id = xyTo1D(row, col);

        if (isOpen(row, col)) {
            return;
        }

        gridOpened[row][col] = true;

        int[][] neighbour = neighbour(row, col);

        for (int i = 0; i < neighbour.length; i++) {

            int idNeighbour = xyTo1D(neighbour[i][0], neighbour[i][1]);

            try {
                if (isOpen(neighbour[i][0], neighbour[i][1])) {
                    grid.union(id, idNeighbour);
                }
            }
            catch (IndexOutOfBoundsException e) {
                ;
            }
        }
    }

    public boolean isOpen(int row, int col) {
        indexCheck(row, col);

        if (gridOpened[row][col]){
            return true;
        }

        return false;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        indexCheck(row, col);
        int id = xyTo1D(row, col);

        if (grid.connected(id, 0) && gridOpened[row][col]) {
            return true;
        }

        return false;
    }

    public int numberOfOpenSites() {
        int openSites = 0;
        for (int i = 0; i < gridOpened.length; i++) {
            for (int j = 0; j < gridOpened.length; j++) {
                if (gridOpened[i][j]){
                    openSites ++;
                }
            }
        }
        return openSites;
    }

    public boolean percolates() {

        int nSquare = (int) Math.pow(nGrid, 2.0);

        if (grid.connected(0, nSquare+1)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);

        Percolation grid = new Percolation(n);
        System.out.println(grid.percolates());
        System.out.println(grid.isOpen(1,1));
    }
}
