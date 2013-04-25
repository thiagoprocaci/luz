package br.com.tbp.model;


public class Edge  {
    private User user1;
    private User user2;
    private Integer weight;

    public Edge(User user1, User user2) {
        if(user1 == null || user2 == null) {
            throw new IllegalArgumentException("User can not be null");
        }
        this.user1 = user1;
        this.user2 = user2;
        weight = 1;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Integer getWeight() {
        return weight;
    }

    public void increaseWeight() {
        weight++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge edge = (Edge) o;
        if(user1.equals(edge.user1) && user2.equals(user2)) {
            return true;
        }
        if(user1.equals(edge.user2) && user2.equals(user1)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = user1 != null ? user1.hashCode() : 0;
        result = 31 * result + (user2 != null ? user2.hashCode() : 0);
        return result;
    }
}
