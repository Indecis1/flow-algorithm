package fr.karl.operation_research.cli;

import com.beust.jcommander.Parameter;

public class Args {

    @Parameter(names = "-a", description = "The computation to do on the graph (max_flow | min_cut | max_flow_min_cost | all)")
    public String algorithm;
    @Parameter(names = "-p", description = "The path of the file holding the graph")
    public String filePath;
    @Parameter(names = "-d", description = "Delimiter used in the file")
    public String delimiter = ",";
    @Parameter(names = "--dir-path", description = "Directory path in which the graphs images will be export")
    public String directory = "";
    @Parameter(names = "--original-flow-only", description = "print only the flow in each edge of the original graph not the residual graph")
    public boolean originalFlowOnly = false;
    @Parameter(names = {"-h","--help"}, help = true, description="Give the Help")
    public boolean help;
}
