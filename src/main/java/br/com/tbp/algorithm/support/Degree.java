package br.com.tbp.algorithm.support;

import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.support.GraphUtils;

public class Degree {

    public void execute(Graph graph) {
        for (Node node: graph.getNodeSet()) {
            node.setDegree(GraphUtils.getTotalDegree(node, graph));
        }
    }

}
