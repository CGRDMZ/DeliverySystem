package com.deuceng.assignment2;

public class Main {

    public static void main(String[] args) {
        DeliverySystemOptimizer dso = new DeliverySystemOptimizer("./graph.txt");
        dso.run();
    }
}
