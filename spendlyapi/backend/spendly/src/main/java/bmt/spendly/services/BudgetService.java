package bmt.spendly.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Budget;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.BudgetRepository;

@Service
@Transactional
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private AppUserRepository userRepository;

    /**
     * Crea o inizializza un budget per un utente specifico.
     */
    public Budget createOrGetUserBudget(String username, double initialAmount) {
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("Utente non trovato: " + username);
        }
        
        // Verifica se l'utente ha già un budget
        Budget existingBudget = budgetRepository.findByUserId(user.getId());
        if (existingBudget != null) {
            // Aggiorno l'amount solo se vuoi "sovrascrivere" o aggiungere l'importo:
            existingBudget.setAmount(existingBudget.getAmount() + initialAmount);
            return budgetRepository.save(existingBudget);
        } else {
            // Creiamo un nuovo Budget
            Budget budget = new Budget(user, initialAmount);
            return budgetRepository.save(budget);
        }
    }

    /**
     * Restituisce il budget di un utente (null se non presente).
     */
    public Budget getUserBudget(String username) {
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("Utente non trovato: " + username);
        }
        return budgetRepository.findByUserId(user.getId());
    }

    /**
     * Aggiunge fondi al budget di un utente.
     */
    public Budget addFunds(String username, double amountToAdd) {
        if (amountToAdd <= 0) {
            throw new IllegalArgumentException("L'importo da aggiungere deve essere positivo.");
        }
        Budget budget = createOrGetUserBudget(username, 0.0); // recupera o crea
        budget.setAmount(budget.getAmount() + amountToAdd);
        return budgetRepository.save(budget);
    }

    /**
     * Scala fondi dal budget, se sufficiente.
     */
    public Budget subtractFunds(String username, double amountToSubtract) {
        if (amountToSubtract <= 0) {
            throw new IllegalArgumentException("L'importo da sottrarre deve essere positivo.");
        }
        Budget budget = getUserBudget(username);
        if (budget == null) {
            throw new IllegalStateException("L'utente non ha alcun budget.");
        }
        if (budget.getAmount() < amountToSubtract) {
            throw new IllegalStateException("Fondi insufficienti nel budget.");
        }
        budget.setAmount(budget.getAmount() - amountToSubtract);
        return budgetRepository.save(budget);
    }
}
