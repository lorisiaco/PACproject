package bmt.spendly.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Savings;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.SavingsRepository;

@Service
@Transactional
public class SavingsService {

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private BudgetService budgetService;

    public Savings createSaving(String username, String name, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo.");
        }
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("Utente non trovato: " + username);
        }
        // Sottrai l'importo dal budget dell'utente; lancerà un'eccezione se i fondi non sono sufficienti.
        budgetService.subtractFunds(username, amount);
        Savings saving = new Savings(user, name, amount);
        return savingsRepository.save(saving);
    }

    public List<Savings> getUserSavings(String username) {
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("Utente non trovato: " + username);
        }
        return savingsRepository.findByUser(user);
    }

    // New method to add funds to a saving (transfer from Budget to saving)
    public Savings addFundsToSaving(String username, Long savingId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo.");
        }
        Savings saving = savingsRepository.findById(savingId)
                .orElseThrow(() -> new IllegalArgumentException("Risparmio non trovato."));
        if (!saving.getUser().getUsername().equalsIgnoreCase(username)) {
            throw new IllegalArgumentException("Accesso non autorizzato.");
        }
        // Deduce dal Budget
        budgetService.subtractFunds(username, amount);
        saving.setAmount(saving.getAmount() + amount);
        return savingsRepository.save(saving);
    }

    // New method to remove funds from a saving (return funds to Budget)
    public Savings removeFundsFromSaving(String username, Long savingId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("L'importo da rimuovere deve essere positivo.");
        }
        Savings saving = savingsRepository.findById(savingId)
                .orElseThrow(() -> new IllegalArgumentException("Risparmio non trovato."));
        if (!saving.getUser().getUsername().equalsIgnoreCase(username)) {
            throw new IllegalArgumentException("Accesso non autorizzato.");
        }
        if (saving.getAmount() < amount) {
            throw new IllegalStateException("Fondi insufficienti nel risparmio.");
        }
        saving.setAmount(saving.getAmount() - amount);
        // Riporta i soldi al Budget
        budgetService.addFunds(username, amount);
        return savingsRepository.save(saving);
    }

    // New method to delete a saving and return its funds to Budget
    public void deleteSaving(String username, Long savingId) {
        Savings saving = savingsRepository.findById(savingId)
                .orElseThrow(() -> new IllegalArgumentException("Risparmio non trovato."));
        if (!saving.getUser().getUsername().equalsIgnoreCase(username)) {
            throw new IllegalArgumentException("Accesso non autorizzato.");
        }
        double funds = saving.getAmount();
        // Elimina il risparmio
        savingsRepository.delete(saving);
        // Riporta i soldi al Budget
        budgetService.addFunds(username, funds);
    }
}
