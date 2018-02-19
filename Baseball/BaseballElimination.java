import java.util.ArrayList;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 *  Compilation:  javac BaseballElimination.java
 *  Execution:    java BaseballElimination input.txt
 *  Dependencies: FlowEdge.java FlowNetwork.java FordFulkerson.java In.java StdOut.java
 *
 *  This program takes the name of a file as a command-line argument.
 *
 *  The {@code BaseballElimination} class represents a flow network of games (next to source) and teams (next to sink).
 *
 *  @author Rocío Byron
 *  created on 2018/02/01
 **/
public class BaseballElimination {

    private final ArrayList<String> teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] matches;

    private FlowNetwork network;
    private int nMatches = 0;
    private final int n;
    private ArrayList<String> certificateOfElimination;
    private FordFulkerson FF;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();

        teams = new ArrayList<>();
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        matches = new int[n][n];

        for (int i = 0; i < n; i++) {
            teams.add(in.readString());
            wins[i] = in.readInt();
            losses[i] = in.readInt();

            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                matches[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        int id = teams.indexOf(team);

        verify(id);

        return wins[id];
    }

    // number of losses for given team
    public int losses(String team) {
        int id = teams.indexOf(team);
        verify(id);

        return losses[id];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        int id = teams.indexOf(team);
        verify(id);

        return remaining[id];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int i = teams.indexOf(team1);
        int j = teams.indexOf(team2);
        verify(i);
        verify(j);

        return matches[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        int id = teams.indexOf(team);
        verify(id);

        int total = wins[id] + remaining[id];

        // Every time isEliminated is called, COE is cleaned.
        certificateOfElimination = new ArrayList<>();

        // check if it is a direct elimination
        for (int i = 0; i < n; i++) {
            if (wins[i] > total) {
                certificateOfElimination.add(teams.get(i));
                return true;
            }
        }

        // Else, load all teams into FlowNetwork
        LoadFN(id);

        // Build Ford Fulkerson class to find maxflow
        FF = new FordFulkerson(network, 0, network.V() - 1);

        // if some edges not in full flow, team eliminated
        for (FlowEdge e: network.adj(0)) {
            if (e.flow() < e.capacity()) {
                return true;
            }
        }

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        if (this.isEliminated(team)) {
            if (certificateOfElimination.isEmpty()) {
                for (int i = 0; i < n; i++) {
                    if (FF.inCut(i + nMatches + 1)) {
                        certificateOfElimination.add(teams.get(i));
                    }
                }
            }

            return certificateOfElimination;
        }
        else {
            return null;
        }

    }

    private void LoadFN(int id) {

        // no of vertices = matches (n choose 2) + teams + 2
        nMatches = (n - 1) * n / 2;
        int V =  nMatches + n + 2;

        // create empty network
        network = new FlowNetwork(V);

        /* The vertices are tracked by j, with values as follows:
        - source = 0
        - first match = 1
        - last match = nMatches
        - first team = nMatches + 1
        - last team = nMatches + n
        - sink = V - 1
        */

        int j = 1;
        for (int p = 0; p < n; p++) {
            if (p != id) {
                network.addEdge(new FlowEdge(nMatches + p + 1, V - 1,
                        wins[id] + remaining[id] - wins[p]));

                for (int q = p + 1; q < n; q++) {
                    if (q != id) {
                        network.addEdge(new FlowEdge(0, j, matches[p][q]));
                        network.addEdge(new FlowEdge(j, nMatches + p + 1, Double.POSITIVE_INFINITY));
                        network.addEdge(new FlowEdge(j, nMatches + q + 1, Double.POSITIVE_INFINITY));
                        j++;
                    }
                }
            }
        }
    }

    private void verify(int id) {
        if (id == -1) {
            throw new IllegalArgumentException("Unknown team name");
        }
    }

    // client
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
