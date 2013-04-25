package br.com.tbp.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class EdgeTest {

    @Test
    public void testEquals() {
        User user1 = new User("1", "nome1");
        User user2 = new User("2", "nome2");

        Edge edge1 = new Edge(user1, user2);
        Edge edge2 = new Edge(user2, user1);

        assertEquals(edge1, edge2);
        assertTrue(edge1.equals(edge2));
        assertTrue(edge2.equals(edge1));

    }


}
