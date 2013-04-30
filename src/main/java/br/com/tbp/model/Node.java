package br.com.tbp.model;

public class Node extends CoreEntity {
    
    private String name;
    private double eccentricity;
    private double closeness;
    private double betweenness;
    private Integer modularityClass;
    private double degree;
    private double pagerank;

    public Node(String id, String name) {
        if(id == null) {
            throw new IllegalArgumentException("The user id can not be null");
        }
        setId(id.trim());
        this.name = name.trim();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public double getCloseness() {
        return closeness;
    }

    public void setCloseness(double closeness) {
        this.closeness = closeness;
    }

    public double getBetweenness() {
        return betweenness;
    }

    public void setBetweenness(double betweenness) {
        this.betweenness = betweenness;
    }

    public Integer getModularityClass() {
        return modularityClass;
    }

    public void setModularityClass(Integer modularityClass) {
        this.modularityClass = modularityClass;
    }

    public double getDegree() {
        return degree;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public double getPagerank() {
        return pagerank;
    }

    public void setPagerank(double pagerank) {
        this.pagerank = pagerank;
    }

    public String toString() {
        return name;
    }
}
