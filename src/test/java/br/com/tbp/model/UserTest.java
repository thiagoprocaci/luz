package br.com.tbp.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testEquals() {
        User user1 = new User("1", "nome1");
        User user2 = new User("1", "nome2");
        User user3 = new User("2", "nome2");

        assertEquals(user1, user2);
        assertTrue(user1.equals(user2));
        assertTrue(user2.equals(user1));

        assertNotSame(user1, user3);
        assertNotSame(user2, user3);
        assertFalse(user1.equals(user3));
        assertFalse(user3.equals(user1));
        assertFalse(user2.equals(user3));
        assertFalse(user3.equals(user2));
    }

}
