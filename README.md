# Flow Algorithms

This project enable the computation of 3 algorithms:
- Max Flow Algorithm
- Min Cut Algorithm
- Max Flow Min Cut Algorithm

This project is done in the scope of Operation Research course

To Generate the jar file: `mvn package`
To get help use the argument -h of --help.

For -a parameter representing the computation to do on the graph 
there is 4 possibles values:
- `max_flow` compute the max flow of the graph. The result is the
max flow and the flow in each edge of the graph
- `min_cut` compute the min cut on the graph. The result is the 
list of vertices in the min cut  
- `max_flow_min_cost` compute the max fow min cost of the graph. The
result is the max flow, the mi, cost and the flow in each edge of the graph 
- `all` to compute all the previous algorithm

**Note:** In the result of `max_flow` and `max_flow_min_cost` we have edge with
negative flow. They represent edge that are created in the residual graph.

Here is an example in a linux OS: ` java -jar ./flow_algorithm-1.0-SNAPSHOT.jar -a all --original-flow-only --dir-path /home/test/Projects/operation_research/project/resources -p /home/test/Projects/operation_research/project/resources/graph_dataV.txt`
The norm of the path depend of the system in Windows based system replaces the `/` with `\ `.
In this command we compute the max flow, the min cut and the max flow min cut. We print only the flow in edge of 
the original graph, we don't consider the edge created in the residual graph.

The format for the input is:
- The first line contains 4 numbers : numN odes numArcs sourceNode sinkNode, where numNodes is the
number of nodes of the graph, numArcs is its number of arcs, sourceNode is s, the source node of the flow and
sinkNode is t, the sink of the flow
- Then, each line contains the description of an arc under the form: emanatingN ode, terminatingN ode,
maxCapacity , cost. This defines the arc (emanatingNode, terminatingNode) whose upper bound capacity is
maxCapacity and whose cost is cost.