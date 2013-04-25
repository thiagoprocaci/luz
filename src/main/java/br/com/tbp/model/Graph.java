package br.com.tbp.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Graph extends CoreEntity {

    private Set<User> userSet;
    private Set<Edge> edgeSet;

    public Graph() {
        setId(UUID.randomUUID().toString());
        userSet = new HashSet<User>();
        edgeSet = new HashSet<Edge>();
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public Set<Edge> getEdgeSet() {
        return edgeSet;
    }

    public void setEdgeSet(Set<Edge> edgeSet) {
        this.edgeSet = edgeSet;
    }
}
