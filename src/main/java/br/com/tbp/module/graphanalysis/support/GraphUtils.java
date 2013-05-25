package br.com.tbp.module.graphanalysis.support;


import br.com.tbp.model.Edge;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;

import java.util.HashSet;
import java.util.Set;

public class GraphUtils {

    public static Set<Edge> getEdges(Node source, Graph graph) {
        Set<Edge> edges = new HashSet<Edge>();
        for (Edge edge : graph.getEdgeSet()) {
            if (edge.getNode1().equals(source) || edge.getNode2().equals(source)) {
                edges.add(edge);
            }
        }
        return edges;
    }

    public static Node getOpposite(Node node, Edge edge) {
        if(edge.getNode1().equals(node)) {
            return edge.getNode2();
        }
        if(edge.getNode2().equals(node)) {
            return edge.getNode1();
        }
        return null;
    }

    public static Set<Node> getNeighbors(Node node, Graph graph) {
        Set<Edge> edges = getEdges(node, graph);
        Set<Node> nodeSet = new HashSet<Node>();
        for (Edge edge: edges) {
            nodeSet.add(getOpposite(node, edge));
        }
        return nodeSet;
    }

    public static Edge getEdge(Node node1, Node node2, Graph graph) {
        Set<Edge> edges = new HashSet<Edge>();
        for (Edge edge : graph.getEdgeSet()) {
            if (edge.getNode1().equals(node1) || edge.getNode2().equals(node2)) {
                return edge;
            }
            // um or resolve isso aqui...
            if (edge.getNode1().equals(node2) || edge.getNode2().equals(node1)) {
                return edge;
            }
        }
        return null;
    }

    public static double getTotalDegree(Node node, Graph graph) {
        Set<Edge> edges = getEdges(node, graph);
        return edges.size();
    }

}
