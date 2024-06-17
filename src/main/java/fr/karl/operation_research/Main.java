package fr.karl.operation_research;

import com.beust.jcommander.JCommander;
import fr.karl.operation_research.cli.Args;
import fr.karl.operation_research.model.DirectedEdge;
import fr.karl.operation_research.model.DirectedGraph;
import fr.karl.operation_research.utils.Export;
import fr.karl.operation_research.utils.Import;
import fr.karl.operation_research.utils.Tuple;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Args cliArgs = new Args();
        boolean export = false;
        JCommander jc = JCommander.newBuilder()
                .addObject(cliArgs)
                .build();
        try{
            jc.parse(args);
            if (cliArgs.help) {
                jc.usage();
                System.exit(0);
            }
            if(cliArgs.filePath.isBlank()){
                System.out.println("You should provide the filePath (-p parameter)");
                System.exit(0);
            }
            if (cliArgs.algorithm.isBlank()){
                System.out.println("You should provide the computation to do on the graph (-a parameter)");
                System.exit(0);
            }
            if(!cliArgs.directory.isBlank()){
                Path path = Paths.get(cliArgs.directory);
                if (!Files.isDirectory(path)){
                    System.out.println("You should provide a valid directory (--dir-path parameter). The path is not a directory ");
                    System.exit(0);
                }
                export = true;
            }
            DirectedGraph graph = Import.load(cliArgs.filePath, cliArgs.delimiter);

            if(cliArgs.algorithm.compareToIgnoreCase("all") == 0){
                Tuple<Float, DirectedGraph> maxFlowResult = graph.maximumFlow();
                Export.printMaxFlow(maxFlowResult.getY(), maxFlowResult.getX(), cliArgs.originalFlowOnly);
                List<DirectedEdge> edges = graph.minCut(maxFlowResult.getY());
                Export.printMinCut(edges);
                Tuple<Tuple<Float, Float>, DirectedGraph> result = graph.minCostMaxFlow();
                Export.printMinCostMaxFlow(result.getY(), result.getX().getX(), result.getX().getY(), cliArgs.originalFlowOnly);
                if (export){
                    Path path = Paths.get(cliArgs.directory, "original_graph.png");
                    Export.exportOriginalGraph(graph, "Original Graph", path.toString());
                    path = Paths.get(cliArgs.directory, "max_flow_graph.png");
                    Export.exportGraphFlow(maxFlowResult.getY(), "Max Flow", path.toString());
                    path = Paths.get(cliArgs.directory, "cut_graph.png");
                    Export.exportCutGraph(graph, edges, "Cut Graph", path.toString());
                    path = Paths.get(cliArgs.directory, "max_flow_min_cost_graph.png");
                    Export.exportGraphFlow(result.getY(), "Max Flow Min Cost", path.toString(), true);
                }
            } else if (cliArgs.algorithm.compareToIgnoreCase("max_flow") == 0) {
                Tuple<Float, DirectedGraph> maxFlowResult = graph.maximumFlow();
                Export.printMaxFlow(maxFlowResult.getY(), maxFlowResult.getX(), cliArgs.originalFlowOnly);
                if (export) {
                    Path path = Paths.get(cliArgs.directory, "original_graph.png");
                    Export.exportOriginalGraph(graph, "Original Graph", path.toString());
                    path = Paths.get(cliArgs.directory, "max_flow_graph.png");
                    Export.exportGraphFlow(maxFlowResult.getY(), "Max Flow", path.toString());
                }
            } else if (cliArgs.algorithm.compareToIgnoreCase("min_cut") == 0) {

                List<DirectedEdge> edges = graph.minCut();
                Export.printMinCut(edges);
                if (export) {
                    Path path = Paths.get(cliArgs.directory, "original_graph.png");
                    Export.exportOriginalGraph(graph, "Original Graph", path.toString());
                    path = Paths.get(cliArgs.directory, "cut_graph.png");
                    Export.exportCutGraph(graph, edges, "Cut Graph", path.toString());
                }

            } else if (cliArgs.algorithm.compareToIgnoreCase("max_flow_min_cost") == 0) {

                Tuple<Tuple<Float, Float>, DirectedGraph> result = graph.minCostMaxFlow();
                Export.printMinCostMaxFlow(result.getY(), result.getX().getX(), result.getX().getY(), cliArgs.originalFlowOnly);
                if (export) {
                    Path path = Paths.get(cliArgs.directory, "original_graph.png");
                    Export.exportOriginalGraph(graph, "Original Graph", path.toString());
                    path = Paths.get(cliArgs.directory, "max_flow_min_cost_graph.png");
                    Export.exportGraphFlow(result.getY(), "Max Flow Min Cost", path.toString(), true);
                }
            }

        }catch (IOException e){
            System.out.println("File Not found: " + cliArgs.filePath);
        } catch (Exception e) {
            e.printStackTrace();
            jc.usage();
        }






    }
}
