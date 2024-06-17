package fr.karl.operation_research.utils;

import fr.karl.operation_research.model.DirectedEdge;
import fr.karl.operation_research.model.DirectedGraph;

import java.util.*;

public class Utils {

    public static Tuple<Boolean, Map<Integer, Integer>> bfs(int source, int sink, DirectedGraph residualGraph) {
        Map<Integer, Integer> parent = new HashMap<>();
        Map<Integer, Boolean> visited = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited.put(source, true);
        parent.put(source, -1);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (DirectedEdge e : residualGraph.getAdjacencyList().get(u)){
                int v = e.getEndNode();
                if (!visited.getOrDefault(v, false) && (e.getMaxCapacity() - Math.abs(e.getFlow())) > 0){
                    queue.add(v);
                    parent.put(v, u);
                    visited.put(v, true);
                    if (v == sink) {
                        return new Tuple<>(true, parent);
                    }
                }
            }
        }
        return new Tuple<>(false, parent);
    }

    public static Map<Integer, Boolean> reachableVerticesFromSource(DirectedGraph graph, int source) {
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Boolean> isVisited = new HashMap<>();
        queue.add(source);
        isVisited.put(source, true);
        Map<Integer, List<DirectedEdge>> adjacencyList = graph.getAdjacencyList();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for(DirectedEdge e : adjacencyList.get(u)){
                if ((e.getMaxCapacity() - Math.abs(e.getFlow())) > 0 && !isVisited.getOrDefault(e.getEndNode(), false)) {
                    queue.add(e.getEndNode());
                    isVisited.put(e.getEndNode(), true);
                }
            }
        }
        return isVisited;
    }

    public static Tuple<Boolean, Map<Integer, Integer>> bellmanFord(int source, int sink, DirectedGraph graph){
        Map<Integer, Float> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        Map<Integer, Boolean> inQueue = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        dist.put(source, 0.0f);
        parent.put(source, -1);
        queue.add(source);
        inQueue.put(source, true);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue.put(u, false);
            for (DirectedEdge e : graph.getAdjacencyList().get(u)){
                if ((e.getMaxCapacity() - Math.abs(e.getFlow())) > 0 && dist.getOrDefault(e.getEndNode(), Float.MAX_VALUE) > dist.getOrDefault(u, Float.MAX_VALUE) + e.getCost()) {
                    dist.put(e.getEndNode(), dist.getOrDefault(u, Float.MAX_VALUE) + e.getCost());
                    parent.put(e.getEndNode(), u);
                    if (!inQueue.getOrDefault(e.getEndNode(), false)) {
                        queue.add(e.getEndNode());
                        inQueue.put(e.getEndNode(), true);
                    }
                }
            }
        }
        return new Tuple<>(dist.getOrDefault(sink, Float.MAX_VALUE) != Float.MAX_VALUE, parent);
    }
}
