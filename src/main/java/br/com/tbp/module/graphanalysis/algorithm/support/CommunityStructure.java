package br.com.tbp.module.graphanalysis.algorithm.support;


import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.module.graphanalysis.support.GraphUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class CommunityStructure {

    private HashMap<Community, Float>[] nodeConnectionsWeight;
    private HashMap<Community, Integer>[] nodeConnectionsCount;
    private HashMap<Node, Integer> map;
    private Community[] nodeCommunities;
    private Graph graph;
    private double[] weights;
    private double graphWeightSum;
    private LinkedList<ModEdge>[] topology;
    private LinkedList<Community> communities;
    private int N;
    private HashMap<Integer, Community> invMap;

    public CommunityStructure(Graph graph, boolean useWeight) {
        this.graph = graph;
        N = graph.getNodeSet().size();
        invMap = new HashMap<Integer, Community>();
        nodeConnectionsWeight = new HashMap[N];
        nodeConnectionsCount = new HashMap[N];
        nodeCommunities = new Community[N];
        map = new HashMap<Node, Integer>();
        topology = new LinkedList[N];
        communities = new LinkedList<Community>();
        int index = 0;
        weights = new double[N];
        for (Node node : graph.getNodeSet()) {
            map.put(node, index);
            nodeCommunities[index] = new Community(this);
            nodeConnectionsWeight[index] = new HashMap<Community, Float>();
            nodeConnectionsCount[index] = new HashMap<Community, Integer>();
            weights[index] = 0;
            nodeCommunities[index].seed(index);
            Community hidden = new Community(this);
            hidden.getNodes().add(index);
            invMap.put(index, hidden);
            communities.add(nodeCommunities[index]);
            index++;
        }
        for (Node node : graph.getNodeSet()) {
            int node_index = map.get(node);
            topology[node_index] = new LinkedList<ModEdge>();

            for (Node neighbor : GraphUtils.getNeighbors(node, graph)) {
                if (node.equals(neighbor)) {
                    continue;
                }
                int neighbor_index = map.get(neighbor);
                float weight = 1;
                if (useWeight) {
                    weight = GraphUtils.getEdge(node, neighbor, graph).getWeight();
                }
                weights[node_index] += weight;
                ModEdge me = new ModEdge(node_index, neighbor_index, weight);
                topology[node_index].add(me);
                Community adjCom = nodeCommunities[neighbor_index];
                nodeConnectionsWeight[node_index].put(adjCom, weight);
                nodeConnectionsCount[node_index].put(adjCom, 1);
                nodeCommunities[node_index].getConnectionsWeight().put(adjCom, weight);
                nodeCommunities[node_index].getConnectionsCount().put(adjCom, 1);
                nodeConnectionsWeight[neighbor_index].put(nodeCommunities[node_index], weight);
                nodeConnectionsCount[neighbor_index].put(nodeCommunities[node_index], 1);
                nodeCommunities[neighbor_index].getConnectionsWeight().put(nodeCommunities[node_index], weight);
                nodeCommunities[neighbor_index].getConnectionsCount().put(nodeCommunities[node_index], 1);
                graphWeightSum += weight;
            }
        }
        graphWeightSum /= 2.0;
    }

    private void addNodeTo(int node, Community to) {
        to.add(new Integer(node));
        nodeCommunities[node] = to;
        for (ModEdge e : topology[node]) {
            int neighbor = e.getTarget();
            //Remove Node Connection to this community
            Float neighEdgesTo = nodeConnectionsWeight[neighbor].get(to);
            if (neighEdgesTo == null) {
                nodeConnectionsWeight[neighbor].put(to, e.getWeight());
            } else {
                nodeConnectionsWeight[neighbor].put(to, neighEdgesTo + e.getWeight());
            }
            Integer neighCountEdgesTo = nodeConnectionsCount[neighbor].get(to);
            if (neighCountEdgesTo == null) {
                nodeConnectionsCount[neighbor].put(to, 1);
            } else {
                nodeConnectionsCount[neighbor].put(to, neighCountEdgesTo + 1);
            }
            Community adjCom = nodeCommunities[neighbor];
            Float wEdgesto = adjCom.getConnectionsWeight().get(to);
            if (wEdgesto == null) {
                adjCom.getConnectionsWeight().put(to, e.getWeight());
            } else {
                adjCom.getConnectionsWeight().put(to, wEdgesto + e.getWeight());
            }
            Integer cEdgesto = adjCom.getConnectionsCount().get(to);
            if (cEdgesto == null) {
                adjCom.getConnectionsCount().put(to, 1);
            } else {
                adjCom.getConnectionsCount().put(to, cEdgesto + 1);
            }
            Float nodeEdgesTo = nodeConnectionsWeight[node].get(adjCom);
            if (nodeEdgesTo == null) {
                nodeConnectionsWeight[node].put(adjCom, e.getWeight());
            } else {
                nodeConnectionsWeight[node].put(adjCom, nodeEdgesTo + e.getWeight());
            }
            Integer nodeCountEdgesTo = nodeConnectionsCount[node].get(adjCom);
            if (nodeCountEdgesTo == null) {
                nodeConnectionsCount[node].put(adjCom, 1);
            } else {
                nodeConnectionsCount[node].put(adjCom, nodeCountEdgesTo + 1);
            }
            if (to != adjCom) {
                Float comEdgesto = to.getConnectionsWeight().get(adjCom);
                if (comEdgesto == null) {
                    to.getConnectionsWeight().put(adjCom, e.getWeight());
                } else {
                    to.getConnectionsWeight().put(adjCom, comEdgesto + e.getWeight());
                }
                Integer comCountEdgesto = to.getConnectionsCount().get(adjCom);
                if (comCountEdgesto == null) {
                    to.getConnectionsCount().put(adjCom, 1);
                } else {
                    to.getConnectionsCount().put(adjCom, comCountEdgesto + 1);
                }
            }
        }
    }

    private void removeNodeFrom(int node, Community from) {
        Community community = nodeCommunities[node];
        for (ModEdge e : topology[node]) {
            int neighbor = e.getTarget();
            //Remove Node Connection to this community
            Float edgesTo = nodeConnectionsWeight[neighbor].get(community);
            Integer countEdgesTo = nodeConnectionsCount[neighbor].get(community);
            if (countEdgesTo - 1 == 0) {
                nodeConnectionsWeight[neighbor].remove(community);
                nodeConnectionsCount[neighbor].remove(community);
            } else {
                nodeConnectionsWeight[neighbor].put(community, edgesTo - e.getWeight());
                nodeConnectionsCount[neighbor].put(community, countEdgesTo - 1);
            }
            //Remove Adjacency Community's connection to this community
            Community adjCom = nodeCommunities[neighbor];
            Float oEdgesto = adjCom.getConnectionsWeight().get(community);
            Integer oCountEdgesto = adjCom.getConnectionsCount().get(community);
            if (oCountEdgesto - 1 == 0) {
                adjCom.getConnectionsWeight().remove(community);
                adjCom.getConnectionsCount().remove(community);
            } else {
                adjCom.getConnectionsWeight().put(community, oEdgesto - e.getWeight());
                adjCom.getConnectionsCount().put(community, oCountEdgesto - 1);
            }
            if (node == neighbor) {
                continue;
            }
            if (adjCom != community) {
                Float comEdgesto = community.getConnectionsWeight().get(adjCom);
                Integer comCountEdgesto = community.getConnectionsCount().get(adjCom);
                if (comCountEdgesto - 1 == 0) {
                    community.getConnectionsWeight().remove(adjCom);
                    community.getConnectionsCount().remove(adjCom);
                } else {
                    community.getConnectionsWeight().put(adjCom, comEdgesto - e.getWeight());
                    community.getConnectionsCount().put(adjCom, comCountEdgesto - 1);
                }
            }

            Float nodeEgesTo = nodeConnectionsWeight[node].get(adjCom);
            Integer nodeCountEgesTo = nodeConnectionsCount[node].get(adjCom);
            if (nodeCountEgesTo - 1 == 0) {
                nodeConnectionsWeight[node].remove(adjCom);
                nodeConnectionsCount[node].remove(adjCom);
            } else {
                nodeConnectionsWeight[node].put(adjCom, nodeEgesTo - e.getWeight());
                nodeConnectionsCount[node].put(adjCom, nodeCountEgesTo - 1);
            }
        }
        from.remove(new Integer(node));
    }

    public void moveNodeTo(int node, Community to) {
        Community from = nodeCommunities[node];
        removeNodeFrom(node, from);
        addNodeTo(node, to);
    }

    public void zoomOut() {
        int M = communities.size();
        LinkedList<ModEdge>[] newTopology = new LinkedList[M];
        int index = 0;
        nodeCommunities = new Community[M];
        nodeConnectionsWeight = new HashMap[M];
        nodeConnectionsCount = new HashMap[M];
        HashMap<Integer, Community> newInvMap = new HashMap<Integer, Community>();
        for (int i = 0; i < communities.size(); i++) {//Community com : mCommunities) {
            Community com = communities.get(i);
            nodeConnectionsWeight[index] = new HashMap<Community, Float>();
            nodeConnectionsCount[index] = new HashMap<Community, Integer>();
            newTopology[index] = new LinkedList<ModEdge>();
            nodeCommunities[index] = new Community(com);
            Set<Community> iter = com.getConnectionsWeight().keySet();
            double weightSum = 0;

            Community hidden = new Community(this);
            for (Integer nodeInt : com.getNodes()) {
                Community oldHidden = invMap.get(nodeInt);
                hidden.getNodes().addAll(oldHidden.getNodes());
            }
            newInvMap.put(index, hidden);
            for (Community adjCom : iter) {
                int target = communities.indexOf(adjCom);
                float weight = com.getConnectionsWeight().get(adjCom);
                if (target == index)
                    weightSum += 2. * weight;
                else
                    weightSum += weight;
                ModEdge e = new ModEdge(index, target, weight);
                newTopology[index].add(e);
            }
            weights[index] = weightSum;
            nodeCommunities[index].seed(index);

            index++;
        }
        communities.clear();
        for (int i = 0; i < M; i++) {
            Community com = nodeCommunities[i];
            communities.add(com);
            for (ModEdge e : newTopology[i]) {
                nodeConnectionsWeight[i].put(nodeCommunities[e.getTarget()], e.getWeight());
                nodeConnectionsCount[i].put(nodeCommunities[e.getTarget()], 1);
                com.getConnectionsWeight().put(nodeCommunities[e.getTarget()], e.getWeight());
                com.getConnectionsCount().put(nodeCommunities[e.getTarget()], 1);
            }
        }
        N = M;
        topology = newTopology;
        invMap = newInvMap;
    }

    public HashMap<Community, Float>[] getNodeConnectionsWeight() {
        return nodeConnectionsWeight;
    }

    public void setNodeConnectionsWeight(HashMap<Community, Float>[] nodeConnectionsWeight) {
        this.nodeConnectionsWeight = nodeConnectionsWeight;
    }

    public HashMap<Community, Integer>[] getNodeConnectionsCount() {
        return nodeConnectionsCount;
    }

    public void setNodeConnectionsCount(HashMap<Community, Integer>[] nodeConnectionsCount) {
        this.nodeConnectionsCount = nodeConnectionsCount;
    }

    public HashMap<Node, Integer> getMap() {
        return map;
    }

    public void setMap(HashMap<Node, Integer> map) {
        this.map = map;
    }

    public Community[] getNodeCommunities() {
        return nodeCommunities;
    }

    public void setNodeCommunities(Community[] nodeCommunities) {
        this.nodeCommunities = nodeCommunities;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getGraphWeightSum() {
        return graphWeightSum;
    }

    public void setGraphWeightSum(double graphWeightSum) {
        this.graphWeightSum = graphWeightSum;
    }

    public LinkedList<ModEdge>[] getTopology() {
        return topology;
    }

    public void setTopology(LinkedList<ModEdge>[] topology) {
        this.topology = topology;
    }

    public LinkedList<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(LinkedList<Community> communities) {
        this.communities = communities;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public HashMap<Integer, Community> getInvMap() {
        return invMap;
    }

    public void setInvMap(HashMap<Integer, Community> invMap) {
        this.invMap = invMap;
    }
}