package bmt.spendly.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Group;

public class GroupTest {
    private Group group;
    private AppUser user1;
    private AppUser user2;

    @BeforeEach
    void setUp() {   
        user1 = new AppUser();
        user2 = new AppUser();
        group =new Group("Test Group");
    }
    /**
     *  Test for the Group constructor
     */
    @Test
    void testGroupConstructor() {
        assertNotNull(group);
        assertEquals("Test Group", group.getNome());
    }
    /**
     * Test for the admin Group
     */
    @Test
    void testGroupAdmin(){
        group.setAdmin(user1);
        assertEquals(user1, group.getAdmin());
    }

    /**
     *  Test for the adding members method
     */
    @Test
    void testAggiungiMembro() {
        group.AggiungiMembro(user1);
        assertTrue(group.getMembri().contains(user1));
    }

    /**
     *  Test for the adding of a member that is in the group yet
     */
    @Test
    void testAggiungiMembroDuplicate() {
        group.AggiungiMembro(user1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> group.AggiungiMembro(user1));
        assertEquals("L'utente è già nel gruppo", exception.getMessage());
    }

    /**
     * Test for removing a member from a Group
     */
    @Test
    void testRimuoviMembro() {
        group.AggiungiMembro(user1);
        group.RimuoviMembro(user1);
        assertFalse(group.getMembri().contains(user1));
    }

    /**
    *  Test for removing a member that is not i nthe group
    */
    @Test
    void testRimuoviMembroNonEsistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> group.RimuoviMembro(user1));
        assertEquals("L'utente non è nel gruppo", exception.getMessage());
    }

    /**
     * Test for the method contains
     */
    @Test
    void testContieneMembro() {
        group.AggiungiMembro(user1);
        assertTrue(group.ContieneMembro(user1));
        assertFalse(group.ContieneMembro(user2));
    }
}
