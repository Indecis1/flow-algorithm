package fr.karl.operation_research.model;

import java.io.Serializable;

public class DirectedEdge implements Comparable<DirectedEdge>, Serializable {

    private float cost;
    private float maxCapacity;
    private int startNode;
    private int endNode;

    private float flow;

    public DirectedEdge() {
    }

    public DirectedEdge(int startNode, int endNode, float cost) {
        this.cost = cost;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public DirectedEdge(int startNode, int endNode, float cost, float maxCapacity) {
        this.cost = cost;
        this.maxCapacity = maxCapacity;
        this.startNode = startNode;
        this.endNode = endNode;
        this.flow = 0.0f;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getFlow() {
        return flow;
    }

    public void setFlow(float flow) {
        this.flow = flow;
    }

    public float getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(float maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public int getEndNode() {
        return endNode;
    }

    public void setEndNode(int endNode) {
        this.endNode = endNode;
    }

    @Override
    public int compareTo(DirectedEdge o) {
        return Integer.compare(endNode, o.endNode);
    }
}
