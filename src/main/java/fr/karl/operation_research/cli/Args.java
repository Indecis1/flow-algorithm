package fr.karl.operation_research.cli;

import com.beust.jcommander.Parameter;

public class Args {

    @Parameter(names = "-a", description = "The computation to do on the graph")
    public String algorithm;
    @Parameter(names = "-p", description = "The path of the file holding the graph")
    public String filePath;
    @Parameter(names = "-d", description = "Delimiter used in the file")
    public String delimiter = ",";
    @Parameter(names = "--original-flow-only", description = "print only the flow in each edge of the original graph not the residual graph")
    public boolean originalFlowOnly = false;
    @Parameter(names = {"-h","--help"}, help = true, description="Give the Help")
    public boolean help;
}
