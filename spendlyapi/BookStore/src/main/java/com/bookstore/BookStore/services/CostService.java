package com.bookstore.BookStore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Cost;
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
        System.out.println("DEBUG: Cerco l'utente con username: " + username);
    
        List<AppUser> users = userRepository.findAll();
        for (AppUser u : users) {
            System.out.println("DEBUG: User in DB -> " + u.getUsername());
        }
    
        AppUser user = userRepository.findByUsername(username);
    
        if (user == null) {
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }
    
        System.out.println("DEBUG: Utente trovato -> " + user.getUsername());
    
        cost.setUser(user);
        return costRepository.save(cost);
    }
    
    

    // Ottieni tutte le spese
    public List<Cost> getAllCosts() {
        return costRepository.findAll();
    }

    // Ottieni una spesa per ID
    public Cost getCostById(Long id) {
        return costRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La spesa con ID " + id + " non esiste."));
    }

    // Aggiorna una spesa
    public Cost updateCost(Long id, Cost updatedCost) {
        Cost existingCost = getCostById(id);

        existingCost.setImporto(updatedCost.getImporto());
        existingCost.setTipologia(updatedCost.getTipologia());

        return costRepository.save(existingCost);
    }

    // Elimina una spesa
    public void deleteCost(Long id) {
        if (!costRepository.existsById(id)) {
            throw new IllegalArgumentException("La spesa con ID " + id + " non esiste.");
        }
        costRepository.deleteById(id);
    }
}