package bmt.spendly.controllers;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import bmt.spendly.models.Budget;
import bmt.spendly.services.BudgetService;

@ExtendWith(MockitoExtension.class)
 public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private Budget budget;

    @BeforeEach
    void setUp() {
        budget = new Budget();
        budget.setAmount(1000.0);
    }

    @Test
    void testGetBudget() {
        when(budgetService.getUserBudget("testUser")).thenReturn(budget);

        ResponseEntity<?> response = budgetController.getBudget("testUser");

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBudgetNotFound() {
        when(budgetService.getUserBudget("testUser")).thenReturn(null);

        ResponseEntity<?> response = budgetController.getBudget("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nessun budget trovato per l'utente: testUser", response.getBody());
    }

    @Test
    void testCreateBudget() {
        when(budgetService.createOrGetUserBudget("testUser", 500.0)).thenReturn(budget);

        ResponseEntity<?> response = budgetController.createBudget("testUser", 500.0);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddFunds() {
        when(budgetService.addFunds("testUser", 200.0)).thenReturn(budget);

        ResponseEntity<?> response = budgetController.addFunds("testUser", 200.0);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSubtractFunds() {
        when(budgetService.subtractFunds("testUser", 100.0)).thenReturn(budget);

        ResponseEntity<?> response = budgetController.subtractFunds("testUser", 100.0);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
