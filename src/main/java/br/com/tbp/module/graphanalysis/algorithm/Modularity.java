package br.com.tbp.module.graphanalysis.algorithm;


import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.module.graphanalysis.algorithm.support.Community;
import br.com.tbp.module.graphanalysis.algorithm.support.CommunityStructure;
import br.com.tbp.module.graphanalysis.support.GraphUtils;

import java.util.Random;
import java.util.Set;

public class Modularity {

    private double resolution = 1.;

    public void execute(Graph graph, boolean isRandomized, boolean useWeight) {
        Random rand = new Random();
        CommunityStructure structure = new CommunityStructure(graph, useWeight);
        double totalWeight = structure.getGraphWeightSum();
        double[] nodeDegrees = structure.getWeights().clone();
        boolean someChange = true;
        while (someChange) {
            someChange = false;
            boolean localChange = true;
            while (localChange) {
                localChange = false;
                int start = 0;
                if (isRandomized) {
                    start = Math.abs(rand.nextInt()) % structure.getN();
                }
                int step = 0;
                for (int i = start; step < structure.getN(); i = (i + 1) % structure.getN()) {
                    step++;
                    double best = 0.;
                    Community bestCommunity = null;
                    // Community nodecom = structure.getNodeCommunities()[i];
                    Set<Community> iter = structure.getNodeConnectionsWeight()[i].keySet();
                    for (Community com : iter) {
                        double qValue = q(i, com, structure);
                        if (qValue > best) {
                            best = qValue;
                            bestCommunity = com;
                        }
                    }
                    if ((structure.getNodeCommunities()[i] != bestCommunity) && (bestCommunity != null)) {
                        structure.moveNodeTo(i, bestCommunity);
                        localChange = true;
                    }

                }
                someChange = localChange || someChange;
            }

            if (someChange) {
                structure.zoomOut();
            }
        }

        int[] comStructure = new int[graph.getNodeSet().size()];
        int count = 0;
        double[] degreeCount = new double[structure.getCommunities().size()];
        for (Community com : structure.getCommunities()) {
            for (Integer node : com.getNodes()) {
                Community hidden = structure.getInvMap().get(node);
                for (Integer nodeInt : hidden.getNodes()) {
                    comStructure[nodeInt] = count;
                }
            }
            count++;
        }
        for (Node node : graph.getNodeSet()) {
            int index = structure.getMap().get(node);
            if (useWeight) {
                degreeCount[comStructure[index]] += nodeDegrees[index];
            } else {
                degreeCount[comStructure[index]] += GraphUtils.getTotalDegree(node, graph);
            }

        }

        double modularity = finalQ(comStructure, degreeCount, graph, totalWeight, 1., structure, useWeight);
        double modularityResolution = finalQ(comStructure, degreeCount, graph, totalWeight, resolution, structure, useWeight);

        graph.setModularity(modularity);
        graph.setModularityResolution(modularityResolution);
        graph.setNumberOfCommunities(structure.getCommunities().size());

        for (Node n : graph.getNodeSet()) {
            int n_index = structure.getMap().get(n);
            n.setModularityClass(comStructure[n_index]);
        }


    }

    private double finalQ(int[] struct, double[] degrees, Graph hgraph, double totalWeight, double usedResolution, CommunityStructure structure, boolean useWeight) {
        double res = 0;
        double[] internal = new double[degrees.length];
        for (Node n : hgraph.getNodeSet()) {
            int n_index = structure.getMap().get(n);
            for (Node neighbor : GraphUtils.getNeighbors(n, hgraph)) {
                if (n == neighbor) {
                    continue;
                }
                int neigh_index = structure.getMap().get(neighbor);
                if (struct[neigh_index] == struct[n_index]) {
                    if (useWeight) {
                        internal[struct[neigh_index]] += GraphUtils.getEdge(n, neighbor, hgraph).getWeight();
                    } else {
                        internal[struct[neigh_index]]++;
                    }
                }
            }
        }
        for (int i = 0; i < degrees.length; i++) {
            internal[i] /= 2.0;
            res += usedResolution * (internal[i] / totalWeight) - Math.pow(degrees[i] / (2 * totalWeight), 2);//HERE
        }
        return res;
    }

    private double q(int node, Community community, CommunityStructure structure) {
        Float edgesToFloat = structure.getNodeConnectionsWeight()[node].get(community);
        double edgesTo = 0;
        if (edgesToFloat != null) {
            edgesTo = edgesToFloat.doubleValue();
        }
        double weightSum = community.getWeightSum();
        double nodeWeight = structure.getWeights()[node];
        double qValue = resolution * edgesTo - (nodeWeight * weightSum) / (2.0 * structure.getGraphWeightSum());
        if ((structure.getNodeCommunities()[node] == community) && (structure.getNodeCommunities()[node].size() > 1)) {
            qValue = resolution * edgesTo - (nodeWeight * (weightSum - nodeWeight)) / (2.0 * structure.getGraphWeightSum());
        }
        if ((structure.getNodeCommunities()[node] == community) && (structure.getNodeCommunities()[node].size() == 1)) {
            qValue = 0.;
        }
        return qValue;
    }

}
