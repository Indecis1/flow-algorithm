package fr.karl.operation_research.model;

import fr.karl.operation_research.utils.Tuple;
import fr.karl.operation_research.utils.Utils;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

public class DirectedGraph {

    private Map<Integer, List<DirectedEdge>> adjacencyList;
    private int sourceNode;
    private int sinkNode;

    public DirectedGraph() {
        adjacencyList = new HashMap<>();
    }

    public void addDirectedVertex(int startNode, int endNode, float cost, float maxCapacity){
        adjacencyList.putIfAbsent(startNode, new ArrayList<>());
        adjacencyList.putIfAbsent(endNode, new ArrayList<>());
        adjacencyList.get(startNode).add(
                - (getEdgePosition(startNode, endNode) + 1),
                new DirectedEdge(
                        startNode, endNode, cost, maxCapacity
                )
        );
    }

    public int getEdgePosition (int startNode, int endNode){
        List<DirectedEdge> edgeList = adjacencyList.get(startNode);
        DirectedEdge e = new DirectedEdge();
        e.setEndNode(endNode);
        return Collections.binarySearch(edgeList, e, new Comparator<DirectedEdge>() {
            @Override
            public int compare(DirectedEdge o1, DirectedEdge o2) {
                return Integer.compare(o1.getEndNode(), o2.getEndNode());
            }
        });
    }

    public Tuple<Float, DirectedGraph> maximumFlow(){
        DirectedGraph residualGraph = copy();
        Tuple<Boolean, Map<Integer, Integer>> bfsResult;
        float maxFlow = 0;
        while((bfsResult = Utils.bfs(sourceNode, sinkNode, residualGraph)).getX()){
            float pathFlow = Float.MAX_VALUE;
            Map<Integer, Integer> parent = bfsResult.getY();
            List<DirectedEdge> edges = new ArrayList<>();
            for(int v = sinkNode; v != sourceNode; v = parent.get(v)){
                int u = parent.get(v);
                DirectedEdge e = residualGraph.getAdjacencyList().get(u).get(residualGraph.getEdgePosition(u, v));
                pathFlow = Math.min(
                        pathFlow,
                        e.getMaxCapacity() - Math.abs(e.getFlow())
                );
                edges.add(e);
            }
            Map<Integer, List<DirectedEdge>> resAdjList = residualGraph.getAdjacencyList();
            for (DirectedEdge e : edges){
                e.setFlow(e.getFlow() + pathFlow);
                residualGraph.getAdjacencyList().computeIfAbsent(e.getEndNode(), k -> new ArrayList<>());
                int insertPosition = residualGraph.getEdgePosition(e.getEndNode(), e.getStartNode());
                if (insertPosition >= 0){
                    DirectedEdge invertedEdge = resAdjList.get(e.getEndNode()).get(insertPosition);
                    invertedEdge.setMaxCapacity(invertedEdge.getMaxCapacity() + pathFlow);
                    invertedEdge.setFlow(invertedEdge.getFlow() - pathFlow);
                } else{
                    DirectedEdge invertedEdge = new DirectedEdge(e.getEndNode(), e.getStartNode(), e.getCost(), pathFlow);
                    invertedEdge.setFlow(-pathFlow);
                    resAdjList.get(e.getEndNode()).add(
                            - (insertPosition + 1),
                            invertedEdge
                    );
                }
            }
            maxFlow += pathFlow;
        }
        return new Tuple<>(maxFlow, residualGraph);
    }

    public List<DirectedEdge> minCut(){
        Tuple<Float, DirectedGraph> maxFlowResult = this.maximumFlow();
        return minCut(maxFlowResult.getY());
    }

    public List<DirectedEdge> minCut(DirectedGraph residualGraph){
        int source = sourceNode;
        List<DirectedEdge> minCutEdges = new ArrayList<>();
        Map<Integer, Boolean> isVisited = Utils.reachableVerticesFromSource(residualGraph, source);
        Map<Integer, List<DirectedEdge>> adjacencyList = this.adjacencyList;
        for(int u : adjacencyList.keySet()){
            for (DirectedEdge e : adjacencyList.get(u)){
                if(isVisited.getOrDefault(u, false) && !isVisited.getOrDefault(e.getEndNode(), false)){
                    minCutEdges.add(e);
                }
            }
        }
        return minCutEdges;
    }

    public Tuple<Tuple<Float, Float>, DirectedGraph> minCostMaxFlow(){
        DirectedGraph residualGraph = copy();
        float maxFlow = 0, minCost = 0;
        Tuple<Boolean, Map<Integer, Integer>> bellmanResult;

        Map<Integer, List<DirectedEdge>> resAdjList = residualGraph.getAdjacencyList();
        while ((bellmanResult = Utils.bellmanFord(sourceNode, sinkNode, residualGraph)).getX()) {
            float pathFlow = Float.MAX_VALUE;
            Map<Integer, Integer> parent = bellmanResult.getY();
            List<DirectedEdge> edges = new ArrayList<>();
            for (int v = sinkNode; v != sourceNode; v = parent.get(v)) {
                int u = parent.get(v);
                DirectedEdge e = residualGraph.getAdjacencyList().get(u).get(
                        residualGraph.getEdgePosition(u, v)
                );
                pathFlow = Math.min(pathFlow, e.getMaxCapacity() - Math.abs(e.getFlow()));
                edges.add(e);
            }

            for (DirectedEdge e : edges){
                e.setFlow(e.getFlow() + pathFlow);
                int insertPosition = residualGraph.getEdgePosition(e.getEndNode(), e.getStartNode());
                if (insertPosition >= 0){
                    DirectedEdge invertedEdge = resAdjList.get(e.getEndNode()).get(insertPosition);
                    invertedEdge.setMaxCapacity(invertedEdge.getMaxCapacity() + pathFlow);
                    invertedEdge.setFlow(invertedEdge.getFlow() - pathFlow);
                } else{
                    DirectedEdge invertedEdge = new DirectedEdge(e.getEndNode(), e.getStartNode(), e.getCost(), pathFlow);
                    invertedEdge.setFlow(-pathFlow);
                    resAdjList.get(e.getEndNode()).add(
                            - (insertPosition + 1),
                            invertedEdge
                    );
                }
                minCost += pathFlow * e.getCost();
            }
            maxFlow += pathFlow;
        }
        return new Tuple<>(new Tuple<>(maxFlow, minCost), residualGraph);
    }


    protected DirectedGraph copy(){
        DirectedGraph graph = new DirectedGraph();
        graph.setSinkNode(this.sinkNode);
        graph.setSourceNode(this.sourceNode);
//        graph.getAdjacencyList().putAll(this.adjacencyList);
        Map<Integer, List<DirectedEdge>> newAdjacencylist = SerializationUtils.clone(new HashMap<>(adjacencyList));
        graph.setAdjacencyList(newAdjacencylist);
        return graph;
    }

    public Map<Integer, List<DirectedEdge>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(Map<Integer, List<DirectedEdge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(int sourceNode) {
        this.sourceNode = sourceNode;
    }

    public int getSinkNode() {
        return sinkNode;
    }

    public void setSinkNode(int sinkNode) {
        this.sinkNode = sinkNode;
    }
}
