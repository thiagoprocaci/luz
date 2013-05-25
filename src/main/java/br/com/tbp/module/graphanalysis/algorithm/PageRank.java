package br.com.tbp.module.graphanalysis.algorithm;


import br.com.tbp.model.Edge;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.module.graphanalysis.support.GraphUtils;

import java.util.HashMap;


public class PageRank {

    private double epsilon = 0.001;
    private double probability = 0.85;


    public void execute(Graph graph, boolean useEdgeWeight) {
        int N = graph.getNodeSet().size();
        double[] pageranks = new double[N];
        double[] temp = new double[N];
        HashMap<Node, Integer> indicies = new HashMap<Node, Integer>();
        int index = 0;
        double[] weights = null;
        if (useEdgeWeight) {
            weights = new double[N];
        }
        for (Node s : graph.getNodeSet()) {
            indicies.put(s, index);
            pageranks[index] = 1.0f / N;
            if (useEdgeWeight) {
                double sum = 0;
                for (Edge edge : GraphUtils.getEdges(s, graph)) {
                    sum += edge.getWeight();
                }
                weights[index] = sum;
            }
            index++;
        }
        while (true) {
            double r = 0;
            for (Node s : graph.getNodeSet()) {
                int s_index = indicies.get(s);
                boolean out;
                out = GraphUtils.getTotalDegree(s, graph) > 0;
                if (out) {
                    r += (1.0 - probability) * (pageranks[s_index] / N);
                } else {
                    r += (pageranks[s_index] / N);
                }
            }
            boolean done = true;
            for (Node s : graph.getNodeSet()) {
                int s_index = indicies.get(s);
                temp[s_index] = r;

                for (Edge edge : GraphUtils.getEdges(s, graph)) {
                    Node neighbor = GraphUtils.getOpposite(s, edge);
                    int neigh_index = indicies.get(neighbor);
                    int normalize = (int) GraphUtils.getTotalDegree(neighbor, graph);
                    if (useEdgeWeight) {
                        double weight = edge.getWeight() / weights[neigh_index];
                        temp[s_index] += probability * pageranks[neigh_index] * weight;
                    } else {
                        temp[s_index] += probability * (pageranks[neigh_index] / normalize);
                    }

                }

                if ((temp[s_index] - pageranks[s_index]) / pageranks[s_index] >= epsilon) {
                    done = false;
                }
            }
            pageranks = temp;
            temp = new double[N];
            if ((done)) {
                break;
            }
        }
        for (Node s : graph.getNodeSet()) {
            int s_index = indicies.get(s);
            s.setPagerank(pageranks[s_index]);
        }
    }

    public void setProbability(double prob) {
        probability = prob;
    }

    public void setEpsilon(double eps) {
        epsilon = eps;
    }

    public double getProbability() {
        return probability;
    }

    public double getEpsilon() {
        return epsilon;
    }

}
