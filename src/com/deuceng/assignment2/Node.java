package com.deuceng.assignment2;

public class Node<T> {
    private T source;
    private T dest;
    private int cap;
    private int usedCapacity;
    private boolean visited;

    public Node(T source, T dest, int cap) {
        this.source = source;
        this.dest = dest;
        this.cap = cap;
        this.usedCapacity = 0;
        this.visited = false;
    }

    public void increaseCapacity(int amount) {
        this.cap += amount;
    }

    public void resetUsedCapacity() {
        this.usedCapacity = 0;
    }

    public boolean hasSpace() {
        if (cap - usedCapacity == 0) {
            return false;
        }
        return true;
    }

    public T getDest() {
        return dest;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public void setDest(T dest) {
        this.dest = dest;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public int getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(int usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
