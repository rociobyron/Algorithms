import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;

/**
 *  Compilation:  javac SAP.java
 *  Execution:    java SAP input.txt vertexA1 vertexA2 ... vertexAn vertexB
 *  Dependencies: BreadthFirstDirectedPaths.java Digraph.java In.java
 *
 *  This program takes the name of an input file as a command-line argument, and n+1 values of that input file, vertexAi
 *  and vertexB.
 *
 *  The {@code SAP} builds a digraph based on an input file of vertices and directed edges, and returns on screen the
 *  common ancestor between the first n vertices (vertexA1 to vertexAn) and vertexB.
 *
 *  @author Rocío Byron
 *  created on 2017/11/10
 **/

public class SAP {
    private final Digraph digraph;
    private final int V;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null argument");
        digraph = new Digraph(G); // copy constructor to guarantee immutability
        V = G.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        validateVertex(v);
        validateVertex(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int[] foundSAP = findSAP(bfsV, bfsW);
        int ancestor = foundSAP[0];
        int minPath = foundSAP[1];

        if (ancestor == -1) {
            return -1;
        }

        else {
            return minPath;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        validateVertex(v);
        validateVertex(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int[] foundSAP = findSAP(bfsV, bfsW);
        int ancestor = foundSAP[0];

        if (ancestor == -1) {
            return -1;
        }

        else {
            return ancestor;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        validateVertices(v);
        validateVertices(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int[] foundSAP = findSAP(bfsV, bfsW);
        int ancestor = foundSAP[0];
        int minPath = foundSAP[1];

        if (ancestor == -1) {
            return -1;
        }

        else {
            return minPath;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        validateVertices(v);
        validateVertices(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);


        int[] foundSAP = findSAP(bfsV, bfsW);
        int ancestor = foundSAP[0];

        if (ancestor == -1) {
            return -1;
        }

        else {
            return ancestor;
        }
    }

    private int[] findSAP(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {

        int minPath = V;
        int ancestor = -1;

        for (int i = 0; i < V; i++) {
            if (!bfsV.hasPathTo(i) || !bfsW.hasPathTo(i)) {
                continue;
            }

            else {
                int lV = bfsV.distTo(i);
                int lW = bfsW.distTo(i);

                if (lV + lW < minPath) {
                    ancestor = i;
                    minPath = lV + lW;
                }
            }
        }

        int[] foundSAP = {ancestor, minPath};

        return foundSAP;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("Vertex " + v + " is not between 0 and " + (V-1));
            }
        }
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        ArrayList<Integer> A = new ArrayList<>();
        ArrayList<Integer> B = new ArrayList<>();
        int len = args.length;

        for (int i=1; i < len - 1; i++) {
            A.add(Integer.parseInt(args[i]));
        }

        B.add(Integer.parseInt(args[len - 1]));

        System.out.println(sap.ancestor(A, B));
    }
}