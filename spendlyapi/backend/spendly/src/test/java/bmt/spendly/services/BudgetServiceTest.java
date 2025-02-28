package bmt.spendly.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Budget;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.BudgetRepository;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {
    
    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    private AppUser testUser;
    private Budget testBudget;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testBudget = new Budget(testUser, 100.0);
    }

    @Test
    void testCreateOrGetUserBudget_NewBudget() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(budgetRepository.findByUserId(testUser.getId())).thenReturn(null);
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);

        Budget result = budgetService.createOrGetUserBudget("testuser", 100.0);

        assertNotNull(result);
        assertEquals(100.0, result.getAmount());
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void testCreateOrGetUserBudget_ExistingBudget() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(budgetRepository.findByUserId(testUser.getId())).thenReturn(testBudget);
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);

        Budget result = budgetService.createOrGetUserBudget("testuser", 50.0);

        assertNotNull(result);
        assertEquals(150.0, result.getAmount());
        verify(budgetRepository, times(1)).save(testBudget);
    }

    @Test
    void testGetUserBudget() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(budgetRepository.findByUserId(testUser.getId())).thenReturn(testBudget);

        Budget result = budgetService.getUserBudget("testuser");

        assertNotNull(result);
        assertEquals(100.0, result.getAmount());
    }

    @Test
    void testAddFunds() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(budgetRepository.findByUserId(testUser.getId())).thenReturn(testBudget);
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);

        Budget result = budgetService.addFunds("testuser", 50.0);

        assertNotNull(result);
        assertEquals(150.0, result.getAmount());
    }

    @Test
    void testSubtractFunds() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(budgetRepository.findByUserId(testUser.getId())).thenReturn(testBudget);
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);

        Budget result = budgetService.subtractFunds("testuser", 50.0);

        assertNotNull(result);
        assertEquals(50.0, result.getAmount());
    }
}

