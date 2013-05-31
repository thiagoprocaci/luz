package br.com.tbp.model;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class Graph extends CoreEntity {

    private Set<Node> nodeSet;
    private Set<Edge> edgeSet;

    // atributos que serao preechidos com os algoritmos
    private int diameter;
    private double avgDist;
    private int shortestPaths;
    private int radius;
    private double modularity;
    private double modularityResolution;
    private int numberOfCommunities;
    private Set<Token> tokens;

    public Graph() {
        setId(UUID.randomUUID().toString());
        nodeSet = new LinkedHashSet<Node>();
        edgeSet = new LinkedHashSet<Edge>();
    }

    public Set<Node> getNodeSet() {
        return nodeSet;
    }

    public void setNodeSet(Set<Node> nodeSet) {
        this.nodeSet = nodeSet;
    }

    public Set<Edge> getEdgeSet() {
        return edgeSet;
    }

    public void setEdgeSet(Set<Edge> edgeSet) {
        this.edgeSet = edgeSet;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public double getAvgDist() {
        return avgDist;
    }

    public void setAvgDist(double avgDist) {
        this.avgDist = avgDist;
    }

    public int getShortestPaths() {
        return shortestPaths;
    }

    public void setShortestPaths(int shortestPaths) {
        this.shortestPaths = shortestPaths;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getModularity() {
        return modularity;
    }

    public void setModularity(double modularity) {
        this.modularity = modularity;
    }

    public double getModularityResolution() {
        return modularityResolution;
    }

    public void setModularityResolution(double modularityResolution) {
        this.modularityResolution = modularityResolution;
    }

    public int getNumberOfCommunities() {
        return numberOfCommunities;
    }

    public void setNumberOfCommunities(int numberOfCommunities) {
        this.numberOfCommunities = numberOfCommunities;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }
}
