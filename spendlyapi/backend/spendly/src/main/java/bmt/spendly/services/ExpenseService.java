package bmt.spendly.services;


import bmt.spendly.exception.ResourceNotFoundException;
import bmt.spendly.models.AppUser;
import bmt.spendly.models.Expense;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private AppUserRepository appUserRepository;
    
    // Crea una nuova spesa
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
    
    // Aggiorna una spesa esistente
    public Expense updateExpense(Long id, Expense expenseDetails) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        
        expense.setDescription(expenseDetails.getDescription());
        expense.setAmount(expenseDetails.getAmount());
        expense.setGroup(expenseDetails.getGroup());
        expense.setCreatedBy(expenseDetails.getCreatedBy());
        expense.setExpenseType(expenseDetails.getExpenseType());
        expense.setBeneficiary(expenseDetails.getBeneficiary());
        
        return expenseRepository.save(expense);
    }
    
    // Recupera una spesa per id
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
    }
    
    // Recupera tutte le spese
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
    
    // Elimina una spesa
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        expenseRepository.delete(expense);
    }
    
    // Recupera le spese dell'utente attualmente loggato
    public List<Expense> getExpensesForCurrentUser() {
        // Otteniamo l'autenticazione corrente dal contesto di sicurezza
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // supponendo che lo username sia il principal
        
        // Recuperiamo l'utente dal database
        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        // Recuperiamo le spese create da questo utente
        return expenseRepository.findByCreatedBy(currentUser);
    }
}
