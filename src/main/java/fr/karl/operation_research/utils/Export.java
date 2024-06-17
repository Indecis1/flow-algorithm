package fr.karl.operation_research.utils;

import fr.karl.operation_research.model.DirectedEdge;
import fr.karl.operation_research.model.DirectedGraph;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

import java.io.File;
import java.util.List;
import java.util.Map;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class Export {

    public static void printMaxFlow(DirectedGraph graph, float maxFlow, boolean originalFlowOnly) {
        Map<Integer, List<DirectedEdge>> adjacencyList = graph.getAdjacencyList();
        System.out.printf("--------------------------------%n");
        System.out.printf(" Max Flow Algorithm Result      %n");
        System.out.printf("--------------------------------%n");
        System.out.printf("| %-9s | %-8s | %-6s |%n", "StartNode", "EndNode", "Flow");
        System.out.printf("--------------------------------%n");
        for (int v : adjacencyList.keySet()) {
            for (DirectedEdge e : adjacencyList.get(v)) {
                boolean flowCondition = e.getFlow() != 0;
                if (originalFlowOnly) {
                    flowCondition = e.getFlow() > 0;
                }
                if (flowCondition)
                    System.out.printf("| %-9d | %-8d | %6.2f |%n", e.getStartNode(), e.getEndNode(), e.getFlow());
            }
        }
        System.out.printf("--------------------------------%n");
        System.out.printf("-- The max flow is: %7f --%n", maxFlow);
        System.out.printf("--------------------------------%n");
        if (!originalFlowOnly) {
            System.out.printf(" Note: The negative flow represent %n");
            System.out.printf(" the arc created in the residual graph %n");
            System.out.printf("--------------------------------%n");
        }
        System.out.printf("--- Other edge have a flow  ----%n");
        System.out.printf("---       value of 0        ----%n");
        System.out.printf("--------------------------------%n");
        System.out.println("\n");
    }

    public static void printMinCut(List<DirectedEdge> edges) {
        System.out.printf("------------------------%n");
        System.out.printf("  Edges in the min cut  %n");
        System.out.printf("------------------------%n");
        System.out.printf("| %-9s | %-8s |%n", "StartNode", "EndNode");
        System.out.printf("------------------------%n");
        for (DirectedEdge e : edges) {
            System.out.printf("| %-9d | %-8d |%n", e.getStartNode(), e.getEndNode());
        }
        System.out.printf("------------------------%n");
        System.out.println("\n");
    }

    public static void printMinCostMaxFlow(DirectedGraph graph, float maxFlow, float minCost, boolean originalFlowOnly) {
        Map<Integer, List<DirectedEdge>> adjacencyList = graph.getAdjacencyList();
        System.out.printf("------------------------------------%n");
        System.out.printf(" Min Cost Max Flow Algorithm Result %n");
        System.out.printf("------------------------------------%n");
        System.out.printf("| %-9s | %-8s | %-6s |%n", "StartNode", "EndNode", "Flow");
        System.out.printf("------------------------------------%n");
        for (int v : adjacencyList.keySet()) {
            for (DirectedEdge e : adjacencyList.get(v)) {
                boolean flowCondition = e.getFlow() != 0;
                if (originalFlowOnly) {
                    flowCondition = e.getFlow() > 0;
                }
                if (flowCondition)
                    System.out.printf("| %-9d | %-8d | %6.2f |%n", e.getStartNode(), e.getEndNode(), e.getFlow());
            }
        }
        System.out.printf("------------------------------------%n");
        System.out.printf("---- The max flow is: %7.2f ----%n", maxFlow);
        System.out.printf("---- The min cost is: %7.2f ----%n", minCost);
        System.out.printf("------------------------------------%n");
        if (!originalFlowOnly) {
            System.out.printf(" Note: The negative flow represent %n");
            System.out.printf(" the arc created in the residual graph %n");
            System.out.printf("------------------------------------%n");
        }
        System.out.printf("--- Other edge have a flow  ----%n");
        System.out.printf("---       value of 0        ----%n");
        System.out.printf("--------------------------------%n");
        System.out.println("\n");
    }

    public static void exportOriginalGraph(DirectedGraph originalGraph, String graphName, String filePath) {
        Map<Integer, List<DirectedEdge>> adjacencyList = originalGraph.getAdjacencyList();
        Graph graph = graph(graphName).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("Times-Roman"))
                .linkAttr().with("class", "link-class")
                .graphAttr().with(attr("label", "Original Graph, the first number on each edge is the cost and the second is the maximum capacity"));
        for (int v : adjacencyList.keySet()) {
            for (DirectedEdge e : adjacencyList.get(v)) {
                String start = e.getStartNode() == originalGraph.getSourceNode() ? "s" : e.getStartNode() == originalGraph.getSinkNode() ? "t" : Integer.toString(e.getStartNode());
                String end = e.getEndNode() == originalGraph.getSourceNode() ? "s" : e.getEndNode() == originalGraph.getSinkNode() ? "t" : Integer.toString(e.getEndNode());
                graph = graph.with(
                        node(start).link(
                                to(node(end))
                                        .with(attr("label", e.getCost() + ", " + e.getMaxCapacity()))

                        )
                );

            }
        }
        try {
            Graphviz.fromGraph(graph).height(450).width(850).render(Format.PNG).toFile(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportGraphFlow(DirectedGraph residualGraph, String graphName, String filePath) {
        Export.exportGraphFlow(residualGraph, graphName, filePath, false);
    }


    public static void exportGraphFlow(DirectedGraph residualGraph, String graphName, String filePath, boolean exportCost) {
        Map<Integer, List<DirectedEdge>> adjacencyList = residualGraph.getAdjacencyList();
        Graph graph = graph(graphName).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("Times-Roman"))
                .linkAttr().with("class", "link-class")
                .graphAttr().with(attr("label", "Edge with flow greater than 0 in the graph"));
        for (int v : adjacencyList.keySet()) {
            for (DirectedEdge e : adjacencyList.get(v)) {
                String start = e.getStartNode() == residualGraph.getSourceNode() ? "s" : e.getStartNode() == residualGraph.getSinkNode() ? "t" : Integer.toString(e.getStartNode());
                String end = e.getEndNode() == residualGraph.getSourceNode() ? "s" : e.getEndNode() == residualGraph.getSinkNode() ? "t" : Integer.toString(e.getEndNode());
                String label = exportCost ? e.getCost() + ", " + e.getFlow() : Float.toString(e.getFlow());
                if (e.getFlow() > 0)
                    graph = graph.with(
                            node(start).link(
                                    to(node(end))
                                            .with(attr("label", label))

                            )
                    );

            }
        }
        try {
            Graphviz.fromGraph(graph).height(450).width(750).render(Format.PNG).toFile(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportCutGraph(DirectedGraph originalGraph, List<DirectedEdge> cut, String graphName, String filePath) {
        Map<Integer, List<DirectedEdge>> adjacencyList = originalGraph.getAdjacencyList();
        Graph graph = graph(graphName).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("Times-Roman"))
                .linkAttr().with("class", "link-class")
                .graphAttr().with(attr("label", "Min cut of the graph. The edges in red are in the min cut"));
        for (int v : adjacencyList.keySet()) {
            for (DirectedEdge e : adjacencyList.get(v)) {
                String start = e.getStartNode() == originalGraph.getSourceNode() ? "s" : e.getStartNode() == originalGraph.getSinkNode() ? "t" : Integer.toString(e.getStartNode());
                String end = e.getEndNode() == originalGraph.getSourceNode() ? "s" : e.getEndNode() == originalGraph.getSinkNode() ? "t" : Integer.toString(e.getEndNode());
                boolean isInCut = false;
                for (DirectedEdge edge : cut) {
                    if (edge.getStartNode() == e.getStartNode() && edge.getEndNode() == e.getEndNode()) {
                        isInCut = true;
                        break;
                    }
                }
                if (isInCut) {
                    graph = graph.with(
                            node(start).link(
                                    to(node(end))
                                            .with(Color.RED)
                            )
                    );
                } else {
                    graph = graph.with(
                            node(start).link(
                                    to(node(end))
                            )
                    );
                }

            }
        }
        try {
            Graphviz.fromGraph(graph).height(500).width(800).render(Format.PNG).toFile(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGraph(DirectedGraph residualGraph, String graphName, String filePath) {
        Map<Integer, List<DirectedEdge>> adjacencyList = residualGraph.getAdjacencyList();
        Graph graph = graph(graphName).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .graphAttr().with(attr("label", "Residual Graph in black the remaining capacity"));
        ;
        for (int v : adjacencyList.keySet()) {
            for (DirectedEdge e : adjacencyList.get(v)) {
                if (e.getFlow() != 0) {
                    if (e.getFlow() < 0)
                        graph = graph.with(
                                node(Integer.toString(e.getStartNode())).link(
                                        to(node(Integer.toString(e.getEndNode())))
                                                .with(attr("label", e.getFlow()))
                                                .with(Color.RED)
                                )
                        );
                    else if (e.getMaxCapacity() - e.getFlow() > 0)
                        graph = graph.with(
                                node(Integer.toString(e.getStartNode())).link(
                                        to(node(Integer.toString(e.getEndNode())))
                                                .with(attr("label", e.getMaxCapacity() - e.getFlow()))
                                )
                        );
                }

            }
        }
        try {
            Graphviz.fromGraph(graph).height(400).width(650).render(Format.PNG).toFile(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
