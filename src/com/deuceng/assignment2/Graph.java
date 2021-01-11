package com.deuceng.assignment2;


import java.util.HashMap;
import java.util.LinkedList;

public class Graph<T> {
    // https://www.geeksforgeeks.org/implementing-generic-graph-in-java/
    // i have used this website as a reference
    // edges are kept in a linked list and every edge has a vertex and weight property.
    private HashMap<T, LinkedList<Node<T>>> vertexMap;
    HashMap<T, T> parent;

    public Graph() {
        this.vertexMap = new HashMap<T, LinkedList<Node<T>>>();
        this.parent = new HashMap<T, T>();
    }


    public void addVertex(T vertex) {
        vertexMap.put(vertex, new LinkedList<Node<T>>());
    }

    public void addEdges(T src, T dest, int weight) {
        if (!vertexMap.containsKey(src)) {
            addVertex(src);
        }
        if (!vertexMap.containsKey(dest)) {
            addVertex(dest);
        }

        // we add only one edge from src to dest because we need a one-directional graph.
//        if (vertexMap.get(src).indexOf(dest) == -1) {
//            vertexMap.get(src).add(new Node<T>(dest, weight));
//            return;
//        }
        Node edge = new Node<T>(src, dest, weight);
        vertexMap.get(src).add(edge);
    }

    public LinkedList<Node<T>> getEdges(T vertex) {
        return vertexMap.get(vertex);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v : vertexMap.keySet()) {
            builder.append(v.toString() + ": ");
            for (Node<T> w : vertexMap.get(v)) {
                builder.append(w.getDest().toString() + " (" + w.getCap() + ") ");
            }
            builder.append("\n");
        }

        return (builder.toString());
    }

    public boolean breadthFirstSearch(T start, T destination) {

        HashMap<T, Boolean> isVisited = new HashMap<T, Boolean>();
        for (T vertex :
                vertexMap.keySet()) {
            isVisited.put(vertex, false);
        }

        LinkedList<T> path = new LinkedList<T>();

        path.add(start);
        isVisited.put(start, true);
        while (path.size() != 0) {
            T currentVertex = path.poll();

            for (Node neighbour :
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
        int maximumFlow = 0;
        while (breadthFirstSearch(start, dest)) {
            int pathFlow = Integer.MAX_VALUE;


            Node<T> bottleneck = null;
            // find the minimum node
            T currentVertex = dest;
            while (!currentVertex.equals(start)) {
                T parentVertex = parent.get(currentVertex);
                Node edgeBetween = null;
                for (Node edgeNode :
                        vertexMap.get(parentVertex)) {
                    if (edgeNode.getDest().equals(currentVertex)) {
                        edgeBetween = edgeNode;
                        if (pathFlow > edgeBetween.getCap()) {
                            bottleneck = edgeBetween;
                        }
                        pathFlow = Math.min(pathFlow, edgeBetween.getCap() - edgeBetween.getUsedCapacity());
                    }
                }

                currentVertex = parent.get(currentVertex);
            }


            // set the used capacity of all edges in the path to bottleneck capacity.
            currentVertex = dest;
            while (!currentVertex.equals(start)) {
                T parentVertex = parent.get(currentVertex);
                Node edgeBetween = null;
                for (Node edgeNode :
                        vertexMap.get(parentVertex)) {
                    if (edgeNode.getDest().equals(currentVertex)) {
                        edgeBetween = edgeNode;
                        edgeBetween.setUsedCapacity(edgeBetween.getUsedCapacity() + pathFlow);
                    }
                }

                currentVertex = parent.get(currentVertex);
            }

            System.out.println("source: " + bottleneck.getSource() + " dest: " + bottleneck.getDest());
            maximumFlow += pathFlow;
        }
        System.out.println(maximumFlow);
        return maximumFlow;
    }

    public void findBottlenecks(T start, T dest) {
        breadthFirstSearch(start, dest);
        int pathFlow = Integer.MAX_VALUE;
        Node<T> bottleneck = null;
        // find the minimum node
        T currentVertex = dest;
        while (!currentVertex.equals(start)) {
            T parentVertex = parent.get(currentVertex);
            Node edgeBetween = null;
            for (Node edgeNode :
                    vertexMap.get(parentVertex)) {
                if (edgeNode.getDest().equals(currentVertex)) {
                    edgeBetween = edgeNode;
                    pathFlow = Math.min(pathFlow, edgeBetween.getCap() - edgeBetween.getUsedCapacity());
                    if (Math.min(pathFlow, edgeBetween.getCap() - edgeBetween.getUsedCapacity()) == edgeBetween.getCap() - edgeBetween.getUsedCapacity()) {
                        bottleneck = edgeBetween;
                    }
                }
            }

            currentVertex = parent.get(currentVertex);
        }
        System.out.println("source: " + bottleneck.getSource() + " dest: " + bottleneck.getDest());
    }


}
