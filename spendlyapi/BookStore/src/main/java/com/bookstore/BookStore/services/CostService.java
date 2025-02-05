package com.bookstore.BookStore.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Cost;
import com.bookstore.BookStore.models.Group;
import com.bookstore.BookStore.repositories.AppUserRepository;
import com.bookstore.BookStore.repositories.CostRepository;
import com.bookstore.BookStore.repositories.GroupRepository;

@Service
public class CostService {
    
    @Autowired
    private CostRepository costRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AppUserRepository userRepository;

    public Cost createCost(Cost cost, Long groupId, String username) {
        username = username.trim(); // Rimuove spazi prima e dopo
    
        System.out.println("DEBUG: Cerco l'utente con username pulito: '" + username + "'");
    
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
    
        if (user == null) {
            System.out.println("ERROR: findByUsername() non ha trovato nulla per '" + username + "'");
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }
    
        System.out.println("DEBUG: Utente trovato -> '" + user.getUsername() + "'");
        cost.setUser(user);
    
        // ✅ Debugging: Controlliamo se il groupId è effettivamente passato
        if (groupId != null) {
            System.out.println("DEBUG: groupId ricevuto -> " + groupId);
    
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new IllegalArgumentException("Il gruppo con ID " + groupId + " non esiste."));
    
            cost.setGroup(group);
            System.out.println("DEBUG: Gruppo assegnato -> " + group.getNome() + " (ID: " + group.getId() + ")");
        } else {
            System.out.println("DEBUG: Nessun gruppo assegnato, groupId è null");
        }
    
        Cost savedCost = costRepository.save(cost);
        System.out.println("DEBUG: Costo salvato con ID -> " + savedCost.getCostId() + ", Group: " + savedCost.getGroup());
    
        return savedCost;
    }
    
    
    
    
    
    public List<Cost> getCostsByUsername(String username) {
        username = username.trim();
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }
        return costRepository.findByUserId(user.getId());
    }

    // ✅ Metodo per ottenere i costi di un gruppo specifico
    public List<Cost> getCostsByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new IllegalArgumentException("Il gruppo con ID " + groupId + " non esiste.");
        }
        return costRepository.findByGroup(group);
    }

    public List<Cost> getAllCosts() {
        return costRepository.findAll();
    }

    public Cost getCostById(Long id) {
        return costRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La spesa con ID " + id + " non esiste."));
    }

    public Cost updateCost(Long id, Cost updatedCost) {
        Cost existingCost = getCostById(id);
        existingCost.setImporto(updatedCost.getImporto());
        existingCost.setTipologia(updatedCost.getTipologia());
        return costRepository.save(existingCost);
    }

    public void deleteCost(Long id) {
        if (!costRepository.existsById(id)) {
            throw new IllegalArgumentException("La spesa con ID " + id + " non esiste.");
        }
        costRepository.deleteById(id);
    }
}
