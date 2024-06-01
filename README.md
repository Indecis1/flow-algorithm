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
negative flow. They represent edge that are created in the residual graph