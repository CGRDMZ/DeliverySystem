package com.deuceng.assignment2;// Java Program to implement
// the above approach
import java.util.*;

public class test {

    // Stores the found edges
    boolean found[];

    // Stores the number of nodes
    int N;

    // Stores the capacity
    // of each edge
    int cap[][];

    int flow[][];

    // Stores the cost per
    // unit flow of each edge
    int cost[][];

    // Stores the distance from each node
    // and picked edges for each node
    int dad[], dist[], pi[];

    static final int INF
            = Integer.MAX_VALUE / 2 - 1;

    // Function to check if it is possible to
    // have a flow from the src to sink
    boolean search(int src, int sink)
    {

        // Initialise found[] to false
        Arrays.fill(found, false);

        // Initialise the dist[] to INF
        Arrays.fill(dist, INF);

        // Distance from the source node
        dist[src] = 0;

        // Iterate untill src reaches N
        while (src != N) {

            int best = N;
            found[src] = true;

            for (int k = 0; k < N; k++) {

                // If already found
                if (found[k])
                    continue;

                // Evaluate while flow
                // is still in supply
                if (flow[k][src] != 0) {

                    // Obtain the total value
                    int val
                            = dist[src] + pi[src]
                            - pi[k] - cost[k][src];

                    // If dist[k] is > minimum value
                    if (dist[k] > val) {

                        // Update
                        dist[k] = val;
                        dad[k] = src;
                    }
                }

                if (flow[src][k] < cap[src][k]) {

                    int val = dist[src] + pi[src]
                            - pi[k] + cost[src][k];

                    // If dist[k] is > minimum value
                    if (dist[k] > val) {

                        // Update
                        dist[k] = val;
                        dad[k] = src;
                    }
                }

                if (dist[k] < dist[best])
                    best = k;
            }

            // Update src to best for
            // next iteration
            src = best;
        }

        for (int k = 0; k < N; k++)
            pi[k]
                    = Math.min(pi[k] + dist[k],
                    INF);

        // Return the value obtained at sink
        return found[sink];
    }

    // Function to obtain the maximum Flow
    int[] getMaxFlow(int cap[][], int cost[][],
                     int src, int sink)
    {

        this.cap = cap;
        this.cost = cost;

        N = cap.length;
        found = new boolean[N];
        flow = new int[N][N];
        dist = new int[N + 1];
        dad = new int[N];
        pi = new int[N];

        int totflow = 0, totcost = 0;

        // If a path exist from src to sink
        while (search(src, sink)) {

            // Set the default amount
            int amt = INF;
            for (int x = sink; x != src; x = dad[x])

                amt = Math.min(amt,
                        flow[x][dad[x]] != 0
                                ? flow[x][dad[x]]
                                : cap[dad[x]][x]
                                - flow[dad[x]][x]);

            for (int x = sink; x != src; x = dad[x]) {

                if (flow[x][dad[x]] != 0) {
                    flow[x][dad[x]] -= amt;
                    totcost -= amt * cost[x][dad[x]];
                }
                else {
                    flow[dad[x]][x] += amt;
                    totcost += amt * cost[dad[x]][x];
                }
            }
            totflow += amt;
        }

        // Return pair total cost and sink
        return new int[] { totflow, totcost };
    }

    // Driver Code
    public static void main(String args[])
    {

        // Creating an object flow
        test flow = new test();

        int s = 0, t = 4;

        int cap[][] = { { 0, 3, 1, 0, 3 },
                { 0, 0, 2, 0, 0 },
                { 0, 0, 0, 1, 6 },
                { 0, 0, 0, 0, 2 },
                { 0, 0, 0, 0, 0 } };

        int cost[][] = { { 0, 1, 0, 0, 2 },
                { 0, 0, 0, 3, 0 },
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0 } };

        int ret[] = flow.getMaxFlow(cap, cost, s, t);

        System.out.println(ret[0] + " " + ret[1]);
    }
}
