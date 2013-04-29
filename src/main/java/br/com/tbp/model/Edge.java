package br.com.tbp.model;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Edge extends CoreEntity  {
    private Node node1;
    private Node node2;
    private Float weight;

    public Edge(Node node1, Node node2) {
        if(node1 == null || node2 == null) {
            throw new IllegalArgumentException("Node can not be null");
        }
        this.node1 = node1;
        this.node2 = node2;
        this.weight = 1F;

        // logica para a construcao do id
        List<String> list = Arrays.asList(node1.getId(), node2.getId());
        Collections.sort(list);
        setId(list.get(0) + "-" + list.get(1));
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public Float getWeight() {
        return weight;
    }

    public void increaseWeight() {
        weight++;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return
                "node1=" + node1 +
                ", node2=" + node2;
    }
}
