package br.com.tbp.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testEquals() {
        Node node1 = new Node("1", "nome1");
        Node node2 = new Node("1", "nome2");
        Node node3 = new Node("2", "nome2");

        assertEquals(node1, node2);
        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));

        assertNotSame(node1, node3);
        assertNotSame(node2, node3);
        assertFalse(node1.equals(node3));
        assertFalse(node3.equals(node1));
        assertFalse(node2.equals(node3));
        assertFalse(node3.equals(node2));
    }

}
