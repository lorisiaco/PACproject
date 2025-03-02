package bmt.spendly.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import bmt.spendly.models.Savings;
import bmt.spendly.models.AppUser;
import bmt.spendly.services.SavingsService;

@ExtendWith(MockitoExtension.class)
public class SavingsControllerTest {

    @Mock
    private SavingsService savingsService;

    @InjectMocks
    private SavingsController savingsController;

    private AppUser testUser;
    private Savings testSaving;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1);
        testUser.setUsername("testUser");

        testSaving = new Savings(testUser, "Viaggio", 200.0);
    }

    @Test
    void testCreateSaving() {
        when(savingsService.createSaving("testUser", "Viaggio", 200.0)).thenReturn(testSaving);

        ResponseEntity<?> response = savingsController.createSaving("testUser", "Viaggio", 200.0);

        assertNotNull(response.getBody());
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    void testGetSavings() {
        List<Savings> savingsList = Arrays.asList(testSaving);
        when(savingsService.getUserSavings("testUser")).thenReturn(savingsList);

        ResponseEntity<?> response = savingsController.getSavings("testUser");

        assertNotNull(response.getBody());
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    void testAddFundsToSaving() {
        when(savingsService.addFundsToSaving("testUser", 1L, 50.0)).thenReturn(testSaving);

        ResponseEntity<?> response = savingsController.addFundsToSaving("testUser", 1L, 50.0);

        assertNotNull(response.getBody());
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    void testRemoveFundsFromSaving() {
        when(savingsService.removeFundsFromSaving("testUser", 1L, 50.0)).thenReturn(testSaving);

        ResponseEntity<?> response = savingsController.removeFundsFromSaving("testUser", 1L, 50.0);

        assertNotNull(response.getBody());
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    void testDeleteSaving() {
        doNothing().when(savingsService).deleteSaving("testUser", 1L);

        ResponseEntity<?> response = savingsController.deleteSaving("testUser", 1L);

        assertEquals(OK, response.getStatusCode());
    }
}
