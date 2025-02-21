package bmt.spendly.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GestioneSpeseTest {
    private List<Cost> listaSpese;

    @BeforeEach
    void setUp() {
        listaSpese = new ArrayList<>();
        
        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();
        AppUser user3 = new AppUser();
        user1.setUsername("Alice");
        user2.setUsername("Bob");
        user3.setUsername("Charlie");
        
        Group group = new Group();
        group.AggiungiMembro(user1);
        group.AggiungiMembro(user2);
        group.AggiungiMembro(user3);

        Cost uno=new Cost();
        uno.setUser(user1);
        uno.setGroup(group);
        uno.setImporto(60.0);
        Cost due=new Cost();
        due.setUser(user2);
        due.setGroup(group);
        due.setImporto(30.0);
        listaSpese.add(uno);
        listaSpese.add(due);
    }

    @Test
    void testCreaDizionario() {
        Map<String, Double> result = GestioneSpese.creaDizionario(listaSpese);
        assertNotNull(result);
        assertEquals(30.0, result.get("Alice"));
        assertEquals(0.0, result.get("Bob"));
        assertEquals(-30.0, result.get("Charlie"));
    }

    @Test
    void testCalcolaDebiti() {
        List<Transazione> transazioni = GestioneSpese.calcolaDebiti(listaSpese);
        assertNotNull(transazioni);
        boolean foundCharlieToAlice = transazioni.stream()
            .anyMatch(t -> t.getDebitore().equals("Charlie") && t.getCreditore().equals("Alice") && t.getImporto() == 30.0);
        assertTrue(foundCharlieToAlice);
    }
}
