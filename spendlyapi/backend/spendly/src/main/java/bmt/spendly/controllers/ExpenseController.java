package bmt.spendly.controllers;

import bmt.spendly.models.Expense;
import bmt.spendly.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    
    // Endpoint per recuperare tutte le spese
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }
    
    // Endpoint per recuperare una spesa per id
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }
    
    // Endpoint per creare una nuova spesa
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Expense createdExpense = expenseService.createExpense(expense);
        return ResponseEntity.status(201).body(createdExpense);
    }
    
    // Endpoint per aggiornare una spesa esistente
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        Expense updatedExpense = expenseService.updateExpense(id, expense);
        return ResponseEntity.ok(updatedExpense);
    }
    
    // Endpoint per eliminare una spesa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
    
    // Nuovo endpoint per recuperare le spese dell'utente loggato (JWT)
    @GetMapping("/me")
    public ResponseEntity<List<Expense>> getExpensesForCurrentUser() {
        List<Expense> expenses = expenseService.getExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }
}
