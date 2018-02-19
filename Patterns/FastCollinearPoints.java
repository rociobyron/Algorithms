import java.util.Arrays;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

/******************************************************************************
 *  Compilation:  javac LineSegment.java Point.java FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints input.txt
 *  Dependencies: Point.java LineSegment.java In.java
 *
 *  The {@code FastCollinearPoints} finds all line segments containing 4+ points
 *
 *  @author Rocío Byron
 *  created on 2017/08/28
 *
 ******************************************************************************/
public class FastCollinearPoints {

    private int numSegs;
    private final ArrayList<LineSegment> lineSegmentList = new ArrayList<>();
    private ArrayList<Point[]> edgeNodes = new ArrayList<>();


    /**
     * Constructor: Finds all line segments containing 4+ points
     *
     * @param points all points to be evaluated
     */
    public FastCollinearPoints(Point[] points) {

        numSegs = 0;
        checkPoints(points);

        for (int i = 0; i < points.length - 3; i++) {
            // Note that we need at least 3 points ahead of the i to get a 4+ line
            Arrays.sort(points, i, points.length, points[i].slopeOrder());
            // After sorting, p slope is -inf; p index is in position i inside points[]

            int firstIndex = i + 1;
            double slope = Double.NEGATIVE_INFINITY;
            double newSlope;

            for (int j = i + 1; j < points.length; j++) {
                newSlope = points[i].slopeTo(points[j]);
                int lastIndex = 0;

                if (newSlope == slope && j == points.length - 1 && j >= firstIndex + 2) {
                    lastIndex = points.length;
                }

                else if (newSlope != slope && j >= firstIndex + 3) {
                    lastIndex = j;
                }

                if (lastIndex != 0) {
                    Point[] edge = findEdge(i, firstIndex, lastIndex, points);

                    if (!isDuplicate(edge, edgeNodes)) {
                        lineSegmentList.add(new LineSegment(edge[0], edge[1]));
                        edgeNodes.add(edge);
                        numSegs++;
                    }
                }

                if (newSlope != slope) {
                    firstIndex = j;
                    slope = newSlope;
                }
            }
        }
    }

    /**
     * Finds edges of segment in physical 2D space. Brute force implementation
     *
     * @param firstIndex inclusive
     * @param lastIndex exclusive
     * @param points
     * @return edges of segment (defined from top to bottom)
     */
    private Point[] findEdge(int iteration, int firstIndex, int lastIndex, Point[] points) {
        Point [] edge = new Point[2];
        Point [] set = new Point[lastIndex - firstIndex + 1];

        set[0] = points[iteration];

        for (int i = 1; i < set.length; i++) {
            set[i] = points[firstIndex + i - 1];
        }

        Arrays.sort(set);

        edge[0] = set[0];
        edge[1] = set[set.length - 1];

        return edge;
    }

    private boolean isDuplicate(Point[] edge, ArrayList<Point[]> edgeNodes) {

        for (int i = 0; i < edgeNodes.size(); i++) {
            if (edge[0].compareTo(edgeNodes.get(i)[0]) == 0 || edge[1].compareTo(edgeNodes.get(i)[1]) == 0) {
                double slope = edge[0].slopeTo(edge[1]);
                double historySlope = edgeNodes.get(i)[0].slopeTo(edgeNodes.get(i)[1]);

                if (slope == historySlope) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Throws illegal arguments if input points not valid
     *
     * @param points input points
     */
    private void checkPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }

        int n = points.length;

        boolean validPoints = true;

        for (int i = 0; i < n; i++) {
            if (points[i] == null) {
                validPoints = false;
                break;
            }
        }

        if (!validPoints || repeated(points)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean repeated(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This is the number of line segments containing 4 points
     *
     * @return number of line segments
     */
    public int numberOfSegments() {
        return numSegs;
    }

    /**
     * These are the line segments
     *
     * @return LineSegment object defined between first and last points (p, q)
     */
    public LineSegment[] segments() {
        return lineSegmentList.toArray(new LineSegment[lineSegmentList.size()]);
    }

    /**
     * Unit tests the FCP class.
     */
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        // load and draw points

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(255, 0, 0);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();

            points[i] = new Point(x, y);
            points[i].draw();
        }

        // print and draw the line segments
        StdDraw.setPenRadius(0.001);
        StdDraw.setPenColor(0, 0, 255);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        System.out.println("No. segments: " + collinear.numberOfSegments());
        StdDraw.save(args[0].substring(0, args[0].length() - 4) + "_segments.png");
        System.exit(0);
    }
}