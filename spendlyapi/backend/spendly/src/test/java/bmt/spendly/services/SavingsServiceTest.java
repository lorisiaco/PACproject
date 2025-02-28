package bmt.spendly.services;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bmt.spendly.models.Savings;
import bmt.spendly.models.AppUser;
import bmt.spendly.repositories.SavingsRepository;
import bmt.spendly.repositories.AppUserRepository;


@ExtendWith(MockitoExtension.class)
 public class SavingsServiceTest {

    @Mock
    private SavingsRepository savingsRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private SavingsService savingsService;

    private AppUser testUser;
    private Savings testSaving;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testSaving = new Savings(testUser, "Viaggio", 200.0);
       
    }

    @Test
    void testCreateSaving() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(savingsRepository.save(any(Savings.class))).thenReturn(testSaving);
        
        Savings result = savingsService.createSaving("testuser", "Viaggio", 200.0);
        
        assertNotNull(result);
        assertEquals("Viaggio", result.getName());
        assertEquals(200.0, result.getAmount());
        
        verify(budgetService, times(1)).subtractFunds("testuser", 200.0);
        verify(savingsRepository, times(1)).save(any(Savings.class));
    }

    @Test
    void testGetUserSavings() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(savingsRepository.findByUser(testUser)).thenReturn(Arrays.asList(testSaving));

        List<Savings> savingsList = savingsService.getUserSavings("testuser");

        assertNotNull(savingsList);
        assertEquals(1, savingsList.size());
        assertEquals(testSaving, savingsList.get(0));
    }

    @Test
    void testAddFundsToSaving() {
        when(savingsRepository.findById(1L)).thenReturn(Optional.of(testSaving));
        when(savingsRepository.save(any(Savings.class))).thenReturn(testSaving);
        
        Savings updatedSaving = savingsService.addFundsToSaving("testuser", 1L, 100.0);
        
        assertEquals(300.0, updatedSaving.getAmount());

        verify(budgetService, times(1)).subtractFunds("testuser", 100.0);
        verify(savingsRepository, times(1)).save(any(Savings.class));
    }

    @Test
    void testRemoveFundsFromSaving() {
        when(savingsRepository.findById(1L)).thenReturn(Optional.of(testSaving));
        when(savingsRepository.save(any(Savings.class))).thenReturn(testSaving);
        
        Savings updatedSaving = savingsService.removeFundsFromSaving("testuser", 1L, 50.0);
        
        assertEquals(150.0, updatedSaving.getAmount());

        verify(budgetService, times(1)).addFunds("testuser", 50.0);
        verify(savingsRepository, times(1)).save(any(Savings.class));
    }

    @Test
    void testDeleteSaving() {
        when(savingsRepository.findById(1L)).thenReturn(Optional.of(testSaving));
        doNothing().when(savingsRepository).delete(testSaving);
        
        savingsService.deleteSaving("testuser", 1L);
        
        verify(budgetService, times(1)).addFunds("testuser", 200.0);
        verify(savingsRepository, times(1)).delete(testSaving);
    }
}