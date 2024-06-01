package fr.karl.operation_research.utils;

import fr.karl.operation_research.model.DirectedGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Import {

    public static DirectedGraph load(String filePath, String delimiter) throws Exception {
        DirectedGraph graph = new DirectedGraph();
        Path path = Paths.get(filePath);
        try(BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            int i = 0;
            while((line = reader.readLine()) != null){
                String[] values = line.split(delimiter);
                if (i == 0){
                    graph.setSourceNode(Integer.parseInt(values[2]));
                    graph.setSinkNode(Integer.parseInt(values[3]));
                    i++;
                    continue;
                }
                int startNode = Integer.parseInt(values[0]);
                int endNode = Integer.parseInt(values[1]);
                float maxCapacity = Float.parseFloat(values[2]);
                float cost = Float.parseFloat(values[3]);
                graph.addDirectedVertex(startNode, endNode, cost, maxCapacity);
                i++;
            }
        }catch (IOException e){
            throw  e;
        }catch (ArrayIndexOutOfBoundsException e){
            throw new Exception("The delimiter provided for the file is not the good one");
        }
    return graph;
    }

}
