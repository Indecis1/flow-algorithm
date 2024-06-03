package fr.karl.operation_research;

import com.beust.jcommander.JCommander;
import fr.karl.operation_research.cli.Args;
import fr.karl.operation_research.model.DirectedEdge;
import fr.karl.operation_research.model.DirectedGraph;
import fr.karl.operation_research.utils.Export;
import fr.karl.operation_research.utils.Import;
import fr.karl.operation_research.utils.Tuple;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Args cliArgs = new Args();
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
            DirectedGraph graph = Import.load(cliArgs.filePath, cliArgs.delimiter);

            if(cliArgs.algorithm.compareToIgnoreCase("all") == 0){

                Tuple<Float, DirectedGraph> maxFlowResult = graph.maximumFlow();
                Export.printMaxFlow(maxFlowResult.getY(), maxFlowResult.getX(), cliArgs.originalFlowOnly);
                List<DirectedEdge> edges = graph.minCut(maxFlowResult.getY());
                Export.printMinCut(edges);
                Tuple<Tuple<Float, Float>, DirectedGraph> result = graph.minCostMaxFlow();
                Export.printMinCostMaxFlow(result.getY(), result.getX().getX(), result.getX().getY(), cliArgs.originalFlowOnly);

            } else if (cliArgs.algorithm.compareToIgnoreCase("max_flow") == 0) {

                Tuple<Float, DirectedGraph> maxFlowResult = graph.maximumFlow();
                Export.printMaxFlow(maxFlowResult.getY(), maxFlowResult.getX(), cliArgs.originalFlowOnly);

            } else if (cliArgs.algorithm.compareToIgnoreCase("min_cut") == 0) {

                List<DirectedEdge> edges = graph.minCut();
                Export.printMinCut(edges);

            } else if (cliArgs.algorithm.compareToIgnoreCase("max_flow_min_cost") == 0) {

                Tuple<Tuple<Float, Float>, DirectedGraph> result = graph.minCostMaxFlow();
                Export.printMinCostMaxFlow(result.getY(), result.getX().getX(), result.getX().getY(), cliArgs.originalFlowOnly);

            }

        }catch (IOException e){
            System.out.println("File Not found: " + cliArgs.filePath);
        } catch (Exception e) {
            e.printStackTrace();
            jc.usage();
        }






    }
}
