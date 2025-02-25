package bmt.spendly.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bmt.spendly.models.Savings;
import bmt.spendly.services.SavingsService;

@RestController
@RequestMapping("/api/savings")
public class SavingsController {

    @Autowired
    private SavingsService savingsService;

    @PostMapping("/create")
    public ResponseEntity<?> createSaving(@RequestParam String username,
                                          @RequestParam String name,
                                          @RequestParam double amount) {
        try {
            Savings saving = savingsService.createSaving(username, name, amount);
            return ResponseEntity.ok(saving);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getSavings(@RequestParam String username) {
        try {
            List<Savings> savings = savingsService.getUserSavings(username);
            return ResponseEntity.ok(savings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // New endpoint to add funds to a saving
    @PutMapping("/addFunds")
    public ResponseEntity<?> addFundsToSaving(@RequestParam String username,
                                              @RequestParam Long savingId,
                                              @RequestParam double amount) {
        try {
            Savings saving = savingsService.addFundsToSaving(username, savingId, amount);
            return ResponseEntity.ok(saving);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // New endpoint to remove funds from a saving
    @PutMapping("/removeFunds")
    public ResponseEntity<?> removeFundsFromSaving(@RequestParam String username,
                                                   @RequestParam Long savingId,
                                                   @RequestParam double amount) {
        try {
            Savings saving = savingsService.removeFundsFromSaving(username, savingId, amount);
            return ResponseEntity.ok(saving);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // New endpoint to delete a saving
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSaving(@RequestParam String username,
                                          @RequestParam Long savingId) {
        try {
            savingsService.deleteSaving(username, savingId);
            return ResponseEntity.ok("Risparmio eliminato ed importo restituito al Budget.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
