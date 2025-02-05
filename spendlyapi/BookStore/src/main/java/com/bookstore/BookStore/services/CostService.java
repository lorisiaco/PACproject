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

    public Cost createCost(Cost cost, Integer groupId, String username) {
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

    public List<Cost> getCostsByUsername(String username) {
        username = username.trim();
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }
        return costRepository.findByUserId(user.getId());
    }

    // ✅ Metodo per ottenere i costi di un gruppo specifico
    public List<Cost> getCostsByGroup(Integer groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Il gruppo con ID " + groupId + " non esiste."));
        return costRepository.findByGroupId(group.getId());  // 🔥 FIX QUI
    }

    public List<Cost> getAllCosts() {
        return costRepository.findAll();
    }

    public Cost getCostById(int id) {
        return costRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La spesa con ID " + id + " non esiste."));
    }

    public Cost updateCost(int id, Cost updatedCost) {
        Cost existingCost = getCostById(id);
        existingCost.setImporto(updatedCost.getImporto());
        existingCost.setTipologia(updatedCost.getTipologia());
        return costRepository.save(existingCost);
    }

    public void deleteCost(int id) {
        if (!costRepository.existsById(id)) {
            throw new IllegalArgumentException("La spesa con ID " + id + " non esiste.");
        }
        costRepository.deleteById(id);
    }
}

