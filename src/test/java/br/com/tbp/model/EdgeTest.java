package br.com.tbp.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class EdgeTest {

    @Test
    public void testEquals() {
        Node node1 = new Node("1", "nome1");
        Node node2 = new Node("2", "nome2");

        Edge edge1 = new Edge(node1, node2);
        Edge edge2 = new Edge(node2, node1);

        assertEquals(edge1, edge2);
        assertTrue(edge1.equals(edge2));
        assertTrue(edge2.equals(edge1));

    }


}
