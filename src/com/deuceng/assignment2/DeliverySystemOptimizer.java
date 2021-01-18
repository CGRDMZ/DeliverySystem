package com.deuceng.assignment2;

import java.io.File;
import java.util.Scanner;

public class DeliverySystemOptimizer {
    private Graph g;

    public DeliverySystemOptimizer(Graph g) {
        this.g = g;
    }

    public DeliverySystemOptimizer(String filePath) {
        this.g = createGraphFromEdges(filePath);
    }


    public void run() {
        if (g == null) throw new NullPointerException("graph should not be null");

        System.out.println(g);

        Scanner scn = new Scanner(System.in);
        while (true) {
            // i choose to not transform letter case, because vertex "A" and vertex "a" is different.
            System.out.println("Enter the first vertex: (case sensitive)(-1 to quit.)");
            String firstVertex = scn.nextLine();
            if (firstVertex.equals("-1")) break;

            System.out.println("Enter the second vertex: (case sensitive)(-1 to quit.)");
            String secondVertex = scn.nextLine();
            if (secondVertex.equals("-1")) break;

            System.out.println("Maximum flow is: " + g.findMaxFlow(firstVertex, secondVertex));
            g.printBottlenecksAndAmountOfIncrement(firstVertex, secondVertex);

            System.out.println("------------------------------------------------------");

        }
    }

    private static Graph<String> createGraphFromEdges(String path) {
        Graph<String> g = new Graph<String>();
        try {
            Scanner sc = new Scanner(new File(path));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parsedLine = line.trim().split("\\s+");// regex for capturing the whitespaces
                g.addEdges(parsedLine[0], parsedLine[1], Integer.parseInt(parsedLine[2]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return g;
    }

}
