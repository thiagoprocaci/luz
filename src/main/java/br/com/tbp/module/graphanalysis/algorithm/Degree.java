package br.com.tbp.module.graphanalysis.algorithm;

import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.module.graphanalysis.support.GraphUtils;

public class Degree {

    public void execute(Graph graph) {
        for (Node node : graph.getNodeSet()) {
            node.setDegree(GraphUtils.getTotalDegree(node, graph));
        }
    }

}
