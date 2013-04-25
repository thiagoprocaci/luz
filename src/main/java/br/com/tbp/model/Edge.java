package br.com.tbp.model;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Edge extends CoreEntity  {
    private User user1;
    private User user2;
    private Integer weight;

    public Edge(User user1, User user2) {
        if(user1 == null || user2 == null) {
            throw new IllegalArgumentException("User can not be null");
        }
        this.user1 = user1;
        this.user2 = user2;
        this.weight = 1;

        // logica para a construcao do id
        List<String> list = Arrays.asList(user1.getId(), user2.getId());
        Collections.sort(list);
        setId(list.get(0) + "-" + list.get(1));
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

}
