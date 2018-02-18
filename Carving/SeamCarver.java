import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *  Compilation:  javac SeamCarver.java
 *  Execution:    java SeamCarver input.jpg
 *  Dependencies: Picture.java StdIn.java StdOut.java
 *
 *  This program takes a picture (extensions .jpg, .gif or .png) as a command-line argument.
 *
 *  The class {@code SeamCarver} creates an object that allows to find and remove the line of pixels with minimum energy
 *  (minimum accumulated variation in colour between contiguous pixels).
 *
 *  @author Rocío Byron
 *  created on 2017/11/28
 **/

public class SeamCarver {

    private Color[][] SCColor;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Specified picture is invalid");
        }

        Picture SCPicture =  new Picture(picture);
        width = SCPicture.width();
        height = SCPicture.height();

        SCColor = new Color[width][height];

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                SCColor[c][r] = SCPicture.get(c, r);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                picture.set(c, r, SCColor[c][r]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public  double energy(int x, int y) {

        if (x > width - 1 || y > height - 1 || x < 0 || y < 0) {
            throw new IllegalArgumentException("Specified (x, y) out of bounds");
        }
        else if (x == 0 || y == 0 || x >= (width - 1) || y >= (height - 1)) {
            // pixel on the borders
            return 1000;
        }
        else {
            Color colorEast = SCColor[x + 1][y];
            Color colorSouth = SCColor[x][y + 1];
            Color colorWest = SCColor[x - 1][y];
            Color colorNorth = SCColor[x][y - 1];

            double deltaX2 = delta2(colorEast, colorWest);
            double deltaY2 = delta2(colorNorth, colorSouth);

            return Math.sqrt(deltaX2 + deltaY2);
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        Picture pictureT = new Picture(height, width);

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                pictureT.set(r, c, SCColor[c][r]);
            }
        }

        SeamCarver scT = new SeamCarver(pictureT);
        int[] HS = scT.findVerticalSeam();

        return HS;
    }

    // sequence of indices for vertical seam
    public   int[] findVerticalSeam() {

        double[][] E = new double[width][height];

        // create 2D energy matrix
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                E[col][row] = energy(col, row);
            }
        }

        int[] path = ShortestPath(E);

        return path;
        }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] seam) {

        Picture pictureT = new Picture(height, width);

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                pictureT.set(r, c, SCColor[c][r]);
            }
        }

        SeamCarver scT = new SeamCarver(pictureT);
        scT.removeVerticalSeam(seam);

        height -= 1;

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                SCColor[c][r] = scT.SCColor[r][c];
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        IllegalArgumentException SeamError = new IllegalArgumentException("Invalid seam");

        if (seam == null) {
            throw SeamError;
        }

        if (seam.length != height) {
            throw SeamError;
        }

        for (int r = 0; r < seam.length; r++) {
            int c = seam[r];

            if (c > width - 1) {
                throw SeamError;
            }

            if (r > 1) {

                if (Math.abs(seam[r - 1] - seam[r]) > 1) {
                    throw SeamError;
                }
            }

            for (int col = c; col < width - 1; col++) {
                SCColor[col][r] = SCColor[col + 1][r];
            }
        }

        width -= 1;
    }

    private double delta2(Color color1, Color color2) {

        double red = Math.pow(color2.getRed() - color1.getRed(), 2.0);
        double green = Math.pow(color2.getGreen() - color1.getGreen(), 2.0);
        double blue = Math.pow(color2.getBlue() - color1.getBlue(), 2.0);

        return red + green + blue;
    }

    // finds AcyclicSP (based on algs4 AcyclicSP algorithm
    private int[] ShortestPath(double[][] energy) {

        double[][] distTo = new double[width][height]; // distTo[v] = distance of shortest s->v path
        int[][] edgeTo = new int[width][height]; // edgeTo[v] = last edge on shortest s->v path

        for (int c = 0; c < width; c++) {
            distTo[c][0] = 0;
            for (int r = 1; r < height; r++) {
                distTo[c][r] = Double.POSITIVE_INFINITY;
                edgeTo[c][r] = -1;
            }
        }

        // visit vertices in topological order = top to bottom and left to right

        for (int r = 0; r < height - 1; r++) {
            for (int c = 0; c < width; c++) {
                for (int w : adjacent(c)) {
                    relax(energy, distTo, edgeTo, r, c, w);
                }
            }
        }

        // find minimum of dist[all][height() - 1]
        double minDist = Double.POSITIVE_INFINITY;
        int minEnd = -1;

        for (int c = 0; c < width; c++) {
            if (distTo[c][height - 1] < minDist) {
                minDist = distTo[c][height - 1];
                minEnd = c;
            }
        }

        double pathEnergy = minDist;
        int[] path = new int[height];

        int e = minEnd;

        for (int r = height - 1; r >= 0; r--) {
            path[r] = e;
            e = edgeTo[e][r];
        }

        return path;
    }

    // relax edge v -> w
    private void relax(double[][] energy, double[][] distTo, int[][] edgeTo, int row, int v, int w) {
        double weight = energy[w][row + 1];
        if (distTo[w][row + 1] > distTo[v][row] + weight) {
            distTo[w][row + 1] = distTo[v][row] + weight;
            edgeTo[w][row + 1] = v;
        }
    }

    private List<Integer> adjacent(int v) {
        if (v == 0) {
            if (width == 1) {
                Integer[] adj = {v};
                return Arrays.asList(adj);
            }
            else {
                Integer[] adj = {v, v + 1};
                return Arrays.asList(adj);
            }
        }

        else if (v == width - 1) {
            Integer[] adj = {v - 1, v};
            return Arrays.asList(adj);
        }
        else {
            Integer[] adj = {v - 1, v, v + 1};
            return Arrays.asList(adj);
        }
    }

    // unit testing
    public static void main(String[] args) {

        // Load picture
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);

        StdOut.println("Seam removal started. Type h for horizontal seam or v for vertical seam.");
        StdOut.println("Type 'quit' for exit.");
        String read = StdIn.readString();


        while (!read.contains("quit")) {
            if (read.equals("h")) {
                StdOut.println("Horizontal seam removed!");
                int[] seam = sc.findHorizontalSeam();
                sc.removeHorizontalSeam(seam);
            }

            else if (read.equals("v")) {
                StdOut.println("Vertical seam removed!");
                int[] seam = sc.findVerticalSeam();
                sc.removeVerticalSeam(seam);
            }

            else {
                StdOut.println("Type h for horizontal seam or v for vertical seam.");
            }

            StdOut.println("Image is " + sc.width() + "x" + sc.height() + " pixels");
            read = StdIn.readString();
        }

        // find name of original picture in command-line argument
        Pattern pattern = Pattern.compile("([A-Za-z]+)(\\.jpg|\\.gif|\\.png)");
        Matcher matcher = pattern.matcher(args[0]);

        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            // Saves carved version of original picture
            sc.picture().save("carved_" + args[0].substring(start, end));
        }

        else {
            StdOut.println("Error: the resulting picture could not be saved.");
        }

        System.exit(0);
    }

}

