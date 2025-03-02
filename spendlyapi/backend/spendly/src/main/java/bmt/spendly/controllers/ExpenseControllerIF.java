package bmt.spendly.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import bmt.spendly.models.Expense;

public interface ExpenseControllerIF {
    ResponseEntity<List<Expense>> getAllExpenses();
    ResponseEntity<Expense> getExpenseById(Long id);
    ResponseEntity<Expense> createExpense(Expense expense);
    ResponseEntity<Expense> updateExpense(Long id, Expense expense);
    ResponseEntity<Void> deleteExpense(Long id);
    ResponseEntity<List<Expense>> getExpensesForCurrentUser();
}
