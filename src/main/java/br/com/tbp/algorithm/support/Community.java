package br.com.tbp.algorithm.support;


import java.util.HashMap;
import java.util.LinkedList;

public class Community {

    private double weightSum;
    private CommunityStructure structure;
    private LinkedList<Integer> nodes;
    private HashMap<Community, Float> connectionsWeight;
    private HashMap<Community, Integer> connectionsCount;

    public Community(Community com) {
        structure = com.structure;
        connectionsWeight = new HashMap<Community, Float>();
        connectionsCount = new HashMap<Community, Integer>();
        nodes = new LinkedList<Integer>();
    }

    public Community(CommunityStructure structure) {
        this.structure = structure;
        connectionsWeight = new HashMap<Community, Float>();
        connectionsCount = new HashMap<Community, Integer>();
        nodes = new LinkedList<Integer>();
    }

    public int size() {
        return nodes.size();
    }

    public void seed(int node) {
        nodes.add(node);
        weightSum += structure.getWeights()[node];
    }

    public boolean add(int node) {
        nodes.addLast(new Integer(node));
        weightSum += structure.getWeights()[node];
        return true;
    }

    public boolean remove(int node) {
        boolean result = nodes.remove(new Integer(node));
        weightSum -= structure.getWeights()[node];
        if (nodes.size() == 0) {
            structure.getCommunities().remove(this);
        }
        return result;
    }

    public double getWeightSum() {
        return weightSum;
    }

    public void setWeightSum(double weightSum) {
        this.weightSum = weightSum;
    }

    public CommunityStructure getStructure() {
        return structure;
    }

    public void setStructure(CommunityStructure structure) {
        this.structure = structure;
    }

    public LinkedList<Integer> getNodes() {
        return nodes;
    }

    public void setNodes(LinkedList<Integer> nodes) {
        this.nodes = nodes;
    }

    public HashMap<Community, Float> getConnectionsWeight() {
        return connectionsWeight;
    }

    public void setConnectionsWeight(HashMap<Community, Float> connectionsWeight) {
        this.connectionsWeight = connectionsWeight;
    }

    public HashMap<Community, Integer> getConnectionsCount() {
        return connectionsCount;
    }

    public void setConnectionsCount(HashMap<Community, Integer> connectionsCount) {
        this.connectionsCount = connectionsCount;
    }
}
