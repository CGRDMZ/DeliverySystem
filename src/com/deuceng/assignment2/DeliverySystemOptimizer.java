package com.deuceng.assignment2;

import java.io.File;
import java.util.Scanner;

public class DeliverySystemOptimizer {
    private Graph g;
    private String src = "A";
    private String dest = "C";

    public DeliverySystemOptimizer(Graph g) {
        this.g = g;
    }

    public DeliverySystemOptimizer(String filePath) {
        this.g = createGraphFromEdges(filePath);
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
            System.out.println(g.toString());

            String s = "B";
            String d = "A";

            System.out.println(g.findMaxFlow(s, d));
            g.printBottlenecksAndAmountOfIncrement(s, d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return g;
    }
}
