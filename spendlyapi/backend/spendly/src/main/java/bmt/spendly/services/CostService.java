package bmt.spendly.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Cost;
import bmt.spendly.models.Group;
import bmt.spendly.models.PaymentStatus;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.CostRepository;
import bmt.spendly.repositories.GroupRepository;

@Service
public class CostService implements CostMngtIF {
    
    @Autowired
    private CostRepository costRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private BudgetService budgetService;

    @Override
    public Cost createCost(Cost cost, Long groupId, String username) { // MODIFICATO da Integer a Long
        username = username.trim();
        System.out.println("DEBUG: Cerco l'utente con username pulito: '" + username + "'");
    
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
    
        if (user == null) {
            System.out.println("ERROR: findByUsername() non ha trovato nulla per '" + username + "'");
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }

        cost.setUser(user);

        if (groupId != null) {
            System.out.println("DEBUG: groupId ricevuto -> " + groupId);
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new IllegalArgumentException("Il gruppo con ID " + groupId + " non esiste."));
            cost.setGroup(group);
        } else {
            System.out.println("DEBUG: Nessun gruppo assegnato, groupId è null");
        }
    
        return costRepository.save(cost);
    }

    @Override
    public List<Cost> getCostsByUsername(String username) {
        username = username.trim();
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }
        return costRepository.findByUserId(user.getId());
    }

    @Override
    public List<Cost> getCostsByGroup(Long groupId) { // MODIFICATO da Integer a Long
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Il gruppo con ID " + groupId + " non esiste."));
        return costRepository.findByGroupId(group.getId());  // 🔥 FIX QUI
    }

    @Override
    public List<Cost> getAllCosts() {
        return costRepository.findAll();
    }

    @Override
    public Cost getCostById(Long id) { // MODIFICATO da int a Long
        return costRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La spesa con ID " + id + " non esiste."));
    }

    @Override
    public Cost updateCost(Long id, Cost updatedCost) {
        Cost existingCost = getCostById(id);
        existingCost.setImporto(updatedCost.getImporto());
        existingCost.setTipologia(updatedCost.getTipologia());
        
        // Aggiorniamo anche lo stato di pagamento
        existingCost.setPaymentStatus(updatedCost.getPaymentStatus());
    
        return costRepository.save(existingCost);
    }
    
    @Override
    public void deleteCost(Long id) { // MODIFICATO da int a Long
        if (!costRepository.existsById(id)) {
            throw new IllegalArgumentException("La spesa con ID " + id + " non esiste.");
        }
        costRepository.deleteById(id);
    }

    @Override
    public Cost payCostFromBudget(Long costId, String username) {
        // 1) Recupera la spesa
        Cost cost = getCostById(costId);
        
        // 2) Verifica che la spesa appartenga all'utente (o che l'utente abbia i permessi)
        if (!cost.getUser().getUsername().equalsIgnoreCase(username)) {
            throw new IllegalArgumentException("Non puoi pagare il costo di un altro utente!");
        }
        
        // 3) Scala i fondi dal budget (se insufficienti, lancio un'eccezione)
        budgetService.subtractFunds(username, cost.getImporto());

        // 4) Segna la spesa come PAGATA
        cost.setPaymentStatus(PaymentStatus.PAGATO);

        // 5) Salva e restituisci
        return costRepository.save(cost);
    }
}

