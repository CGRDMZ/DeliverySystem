package com.deuceng.assignment2;


import java.util.HashMap;
import java.util.LinkedList;

public class Graph<T> {
    // https://www.geeksforgeeks.org/implementing-generic-graph-in-java/
    // i have used this website as a reference
    // edges are kept in a linked list and every edge has a vertex and weight property.
    private HashMap<T, LinkedList<Edge<T>>> vertexMap;
    private HashMap<T, T> parent;
    private HashMap<T, Boolean> isVisited;
    private LinkedList<Edge<T>> bottlenecks;

    public Graph() {
        this.vertexMap = new HashMap<T, LinkedList<Edge<T>>>();
        this.parent = new HashMap<T, T>();
        this.isVisited = new HashMap<T, Boolean>();
    }


    public void addVertex(T vertex) {
        vertexMap.put(vertex, new LinkedList<Edge<T>>());
    }

    public void addEdges(T src, T dest, int weight) {
        if (!vertexMap.containsKey(src)) {
            addVertex(src);
        }
        if (!vertexMap.containsKey(dest)) {
            addVertex(dest);
        }

        if (vertexMap.containsKey(src)) {
            for (Edge<T> neighbour :
                    vertexMap.get(src)) {
                if (neighbour.getSource().equals(src) && neighbour.getDest().equals(dest)) {
                    neighbour.increaseCapacity(weight);
                    System.out.println("increased capacity");
                }
            }
        }

        // we add only one edge from src to dest because we need a one-directional graph.
//        if (vertexMap.get(src).indexOf(dest) == -1) {
//            vertexMap.get(src).add(new Edge<T>(dest, weight));
//            return;
//        }
        Edge edge = new Edge<T>(src, dest, weight);
        vertexMap.get(src).add(edge);
    }

    public LinkedList<Edge<T>> getEdges(T vertex) {
        return vertexMap.get(vertex);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v : vertexMap.keySet()) {
            builder.append(v.toString() + ": ");
            for (Edge<T> w : vertexMap.get(v)) {
                builder.append(w.getDest().toString() + " (" + w.getCap() + ") ");
            }
            builder.append("\n");
        }

        return (builder.toString());
    }

    public boolean breadthFirstSearch(T start, T destination) {

        if (start.equals(destination)) return true;

        parent = new HashMap<T, T>();
        isVisited = new HashMap<T, Boolean>();

        for (T vertex :
                vertexMap.keySet()) {
            isVisited.put(vertex, false);
        }

        LinkedList<T> path = new LinkedList<T>();

        path.add(start);
        isVisited.put(start, true);
        while (path.size() != 0) {
            T currentVertex = path.poll();

            for (Edge neighbour :
                    vertexMap.get(currentVertex)) {
                if (!isVisited.get(neighbour.getDest()) && neighbour.hasSpace()) {
                    path.add((T) neighbour.getDest());
                    isVisited.put((T) neighbour.getDest(), true);
                    parent.put((T) neighbour.getDest(), currentVertex);
                }

            }
        }
        return isVisited.get(destination);
    }

    // the question is a maximum flow problem. So, i have searched for the different algorithms
    // to find a solution for the problem, and decided Ford Fulkerson algorithm is a better choice.
    // i have used the below resources to learn it.
    // * https://www.programiz.com/dsa/ford-fulkerson-algorithm
    // * https://en.wikipedia.org/wiki/Ford%E2%80%93Fulkerson_algorithm
    // * https://www.hackerearth.com/practice/algorithms/graphs/maximum-flow/tutorial/
    public int findMaxFlow(T start, T dest) {
        if (!vertexMap.containsKey(start) || !vertexMap.containsKey(dest)) return -1;
        if (start.equals(dest)) return -1;
        bottlenecks = new LinkedList<Edge<T>>();
        int maximumFlow = 0;
        resetEdges();
        while (breadthFirstSearch(start, dest)) {
            int pathFlow = Integer.MAX_VALUE;

            Edge<T> bottleneck = null;
            // find the minimum node
            T currentVertex = dest;
            while (!currentVertex.equals(start)) {
                T parentVertex = parent.get(currentVertex);
                Edge edgeBetween = null;
                for (Edge edge :
                        vertexMap.get(parentVertex)) {
                    if (edge.getDest().equals(currentVertex)) {
                        edgeBetween = edge;
                        if (bottleneck == null || bottleneck.getCap() > edgeBetween.getCap()) {
                            bottleneck = edgeBetween;
                        }
                        pathFlow = Math.min(pathFlow, edgeBetween.getCap() - edgeBetween.getUsedCapacity());
                    }
                }

                currentVertex = parent.get(currentVertex);
            }


            bottlenecks.add(bottleneck);


            // set the used capacity of all edges in the path to bottleneck capacity.
            currentVertex = dest;
            while (!currentVertex.equals(start)) {
                T parentVertex = parent.get(currentVertex);
                Edge edgeBetween = null;
                for (Edge edge :
                        vertexMap.get(parentVertex)) {
                    if (edge.getDest().equals(currentVertex)) {
                        edgeBetween = edge;
                        edgeBetween.setUsedCapacity(edgeBetween.getUsedCapacity() + pathFlow);
                    }
                }

                currentVertex = parent.get(currentVertex);
            }
//
            maximumFlow += pathFlow;
        }

        return maximumFlow;
    }

    public void printBottlenecksAndAmountOfIncrement(T start, T dest) {
        resetEdges();
        findMaxFlow(start, dest);
        LinkedList<Edge<T>> edgeList = new LinkedList<Edge<T>>();
        if (start.equals(dest)) return;
        for (LinkedList<Edge<T>> edges :
                vertexMap.values()) {
            edgeList.addAll(edges);
        }
//        for (Edge edge :
//                bottlenecks) {
//            edgeList.add(edge);
//        }
        for (Edge edge :
                edgeList) {
            boolean startToBottleneck = breadthFirstSearch(start, (T) edge.getSource());
            Edge bottleneck1 = findBottleneckEdge(parent, start, (T) edge.getSource());

            boolean bottleneckToEnd = breadthFirstSearch((T) edge.getDest(), dest);
            Edge bottleneck2 = findBottleneckEdge(parent, (T) edge.getDest(), dest);

            if (!edge.hasSpace() && bottleneckToEnd && startToBottleneck) {
                int neededIncrement = -1;
                if (bottleneck1 != null && bottleneck2 == null) {
                    neededIncrement = bottleneck1.getResidualCap();
                } if (bottleneck2 != null && bottleneck1 == null) {
                    neededIncrement = bottleneck2.getResidualCap();
                } else if (bottleneck1 != null && bottleneck2 != null) {
                    neededIncrement = Math.min(bottleneck1.getResidualCap(), bottleneck2.getResidualCap());
                }
                System.out.println("source: " + edge.getSource() + " dest: " + edge.getDest() + " increment: " + neededIncrement);
            }
        }
    }

    public void getAvailableIncrement(T start, T dest) {

        LinkedList<Edge<T>> bottleneckList = new LinkedList<Edge<T>>();


    }
    //        for (Edge<T> bottleneck :
//                bottlenecks) {
//            bottleneckList.add(bottleneck);
//        }
//
//        for (Edge bottleneck :
//                bottleneckList) {
//            int previousFlow = findMaxFlow(start, dest);
//            bottleneck.increaseCapacity(10);
//            int increasedFlow = findMaxFlow(start, dest);
//            int incrementCounter = 10;
//            while (previousFlow < increasedFlow) {
//                previousFlow = findMaxFlow(start, dest);
//                bottleneck.increaseCapacity(10);
//                increasedFlow = findMaxFlow(start, dest);
//                incrementCounter+=10;
//            }
//            bottleneck.increaseCapacity(-incrementCounter);
//            System.out.println(incrementCounter + " for: " + "source: " + bottleneck.getSource() + " dest: " + bottleneck.getDest());


    private void resetEdges() {
        for (LinkedList<Edge<T>> edges :
                vertexMap.values()) {
            for (Edge<T> edge :
                    edges) {
                edge.resetUsedCapacity();
            }
        }
    }

    private Edge findBottleneckEdge(HashMap vertices, T start, T end) {
//        if (start.equals(end)) return null;
        T currentVertex = end;
        Edge bottleneck = null;
        while (!currentVertex.equals(start)) {
            T parentVertex = (T) vertices.get(currentVertex);
            if (parentVertex == null) return null;
            Edge edgeBetween = null;
            for (Edge edge :
                    vertexMap.get(parentVertex)) {
                if (edge.getDest().equals(currentVertex)) {
                    edgeBetween = edge;
                    if (bottleneck == null || bottleneck.getResidualCap() > edgeBetween.getResidualCap()) {
                        bottleneck = edgeBetween;
                    }
                }
            }

            currentVertex = parent.get(currentVertex);
        }
        return bottleneck;
    }

    private int[] getAdjMatrix(HashMap adjList) {
        return null;
    }


}
