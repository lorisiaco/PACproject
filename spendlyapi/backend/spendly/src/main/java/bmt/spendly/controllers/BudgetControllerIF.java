package bmt.spendly.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface BudgetControllerIF {
    ResponseEntity<?> getBudget(@RequestParam String username);
    ResponseEntity<?> createBudget(@RequestParam String username, @RequestParam double initialAmount);
    ResponseEntity<?> addFunds(@RequestParam String username, @RequestParam double amount);
    ResponseEntity<?> subtractFunds(@RequestParam String username, @RequestParam double amount);
}
