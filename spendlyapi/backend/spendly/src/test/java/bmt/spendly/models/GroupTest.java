package bmt.spendly.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GroupTest {
    private Group group;
    private AppUser user1;
    private AppUser user2;
    private Alert alert;

    @BeforeEach
    void setUp() {   
        user1 = new AppUser();
        user2 = new AppUser();
        group =new Group("Test Group");
        alert=new Alert();
    }
    
    @Test
    void testGroupConstructor() {
        assertNotNull(group);
        assertEquals("Test Group", group.getNome());
    }
    
    @Test
    void testGroupAdmin(){
        group.setAdmin(user1);
        assertEquals(user1, group.getAdmin());
    }

    
    @Test
    void testAggiungiMembro() {
        group.AggiungiMembro(user1);
        assertTrue(group.getMembri().contains(user1));
    }

    
    @Test
    void testAggiungiMembroDuplicate() {
        group.AggiungiMembro(user1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> group.AggiungiMembro(user1));
        assertEquals("L'utente è già nel gruppo", exception.getMessage());
    }

    
    @Test
    void testRimuoviMembro() {
        group.AggiungiMembro(user1);
        group.RimuoviMembro(user1);
        assertFalse(group.getMembri().contains(user1));
    }

    
    @Test
    void testRimuoviMembroNonEsistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> group.RimuoviMembro(user1));
        assertEquals("L'utente non è nel gruppo", exception.getMessage());
    }

    
    @Test
    void testContieneMembro() {
        group.AggiungiMembro(user1);
        assertTrue(group.ContieneMembro(user1));
        assertFalse(group.ContieneMembro(user2));
    }

    @Test
    void testAggiungiAlert() {
        group.addAlert(alert);
        assertNotNull(group.getAlerts());
    }

    @Test
    void testRimuoviAlert() {
        group.addAlert(alert);
        group.removeAlert(alert);
        assertTrue(group.getAlerts().isEmpty());
    }
}
