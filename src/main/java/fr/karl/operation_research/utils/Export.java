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

    public static void printMaxFlow(DirectedGraph graph, float maxFlow){
        Map<Integer, List<DirectedEdge>> adjacencyList = graph.getAdjacencyList();
        System.out.printf("--------------------------------%n");
        System.out.printf(" Max Flow Algorithm Result      %n");
        System.out.printf("--------------------------------%n");
        System.out.printf("| %-9s | %-8s | %-6s |%n", "StartNode", "EndNode", "Flow");
        System.out.printf("--------------------------------%n");
        for(int v : adjacencyList.keySet()){
            for (DirectedEdge e : adjacencyList.get(v)){
                if(e.getFlow() != 0)
                    System.out.printf("| %-9d | %-8d | %6.2f |%n", e.getStartNode(), e.getEndNode(), e.getFlow());
            }
        }
        System.out.printf("--------------------------------%n");
        System.out.printf("-- The max flow is: %7f --%n", maxFlow);
        System.out.printf("--------------------------------%n");
        System.out.printf(" Note: The negative flow represent %n");
        System.out.printf(" the arc created in the residual graph %n");
        System.out.printf("--------------------------------%n");
        System.out.println("\n");
    }

    public static void printMinCut(List<DirectedEdge> edges){
        System.out.printf("------------------------%n");
        System.out.printf("  Edges in the min cut  %n");
        System.out.printf("------------------------%n");
        System.out.printf("| %-9s | %-8s |%n", "StartNode", "EndNode");
        System.out.printf("------------------------%n");
        for(DirectedEdge e : edges){
            System.out.printf("| %-9d | %-8d |%n", e.getStartNode(), e.getEndNode());
        }
        System.out.printf("------------------------%n");
        System.out.println("\n");
    }

    public static void printMinCostMaxFlow(DirectedGraph graph, float maxFlow, float minCost){
        Map<Integer, List<DirectedEdge>> adjacencyList = graph.getAdjacencyList();
        System.out.printf("------------------------------------%n");
        System.out.printf(" Min Cost Max Flow Algorithm Result %n");
        System.out.printf("------------------------------------%n");
        System.out.printf("| %-9s | %-8s | %-6s |%n", "StartNode", "EndNode", "Flow");
        System.out.printf("------------------------------------%n");
        for(int v : adjacencyList.keySet()){
            for (DirectedEdge e : adjacencyList.get(v)){
                if(e.getFlow() != 0)
                    System.out.printf("| %-9d | %-8d | %6.2f |%n", e.getStartNode(), e.getEndNode(), e.getFlow());

//                    if(e.getFlow() < 0)
//                        System.out.println("\t" + e.getStartNode() + "\t\t\t" + e.getEndNode() + "\t\t " + e.getFlow() + "\t\t Arc created in the residual graph");
//                    else
//                        System.out.println("\t" + e.getStartNode() + "\t\t\t" + e.getEndNode() + "\t\t " + e.getFlow() + "\t\t\t" + e.getMaxCapacity());
            }
        }
        System.out.printf("------------------------------------%n");
        System.out.printf("---- The max flow is: %7.2f ----%n", maxFlow);
        System.out.printf("---- The min cost is: %7.2f ----%n", minCost);
        System.out.printf("------------------------------------%n");
        System.out.printf(" Note: The negative flow represent %n");
        System.out.printf(" the arc created in the residual graph %n");
        System.out.printf("------------------------------------%n");
        System.out.println("\n");

//        Export.showGraph(graph, "MinCostMaxFlow", "/home/karl/Projects/operation_research/project/resources/graphV.png");
    }

    public static void showGraph(DirectedGraph residualGraph, String graphName, String filePath){
        Map<Integer, List<DirectedEdge>> adjacencyList = residualGraph.getAdjacencyList();
        Graph graph = graph(graphName).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class");
        for(int v : adjacencyList.keySet()){
            for (DirectedEdge e : adjacencyList.get(v)){
                if(e.getFlow() != 0)
                {
                    if(e.getFlow() < 0)
                        graph = graph.with(
                                node(Integer.toString(e.getStartNode())).link(
                                        to(node(Integer.toString(e.getEndNode())))
//                                                .with(attr("weight", e.getCost()))
//                                                .with(attr("flow", e.getFlow()))
//                                                .with(Color.RED)
                                )
                        );
                    else
                        graph = graph.with(
                                node(Integer.toString(e.getStartNode())).link(
                                        to(node(Integer.toString(e.getEndNode()))
                                                .with(attr("weight", e.getCost()))
                                                .with(attr("flow", e.getFlow()))
                                        )
                                )
                        );
                }

            }
        }
        try{
            Graphviz.fromGraph(graph).height(400).width(650).render(Format.PNG).toFile(new File(filePath));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
