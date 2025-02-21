package bmt.spendly.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bmt.spendly.models.Budget;
import bmt.spendly.services.BudgetService;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    /**
     * Ottieni il budget di un utente.
     */
    @GetMapping
    public ResponseEntity<?> getBudget(@RequestParam String username) {
        try {
            Budget budget = budgetService.getUserBudget(username);
            if (budget == null) {
                return ResponseEntity.ok("Nessun budget trovato per l'utente: " + username);
            }
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Crea (o inizializza) un budget per un utente, con un importo iniziale.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createBudget(@RequestParam String username,
                                          @RequestParam double initialAmount) {
        try {
            Budget budget = budgetService.createOrGetUserBudget(username, initialAmount);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Aggiunge fondi al budget di un utente.
     */
    @PutMapping("/add")
    public ResponseEntity<?> addFunds(@RequestParam String username,
                                      @RequestParam double amount) {
        try {
            Budget budget = budgetService.addFunds(username, amount);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Sottrae fondi dal budget (ad esempio in caso di pagamento).
     */
    @PutMapping("/subtract")
    public ResponseEntity<?> subtractFunds(@RequestParam String username,
                                           @RequestParam double amount) {
        try {
            Budget budget = budgetService.subtractFunds(username, amount);
            return ResponseEntity.ok(budget);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
