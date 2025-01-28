package com.bmt.spendly.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmt.spendly.models.AppUser;
import com.bmt.spendly.models.Cost;
import com.bmt.spendly.models.Group;
import com.bmt.spendly.repositories.AppUserRepository;
import com.bmt.spendly.repositories.CostRepository;
import com.bmt.spendly.repositories.GroupRepository;

@Service
public class CostService {
    
    @Autowired
    private CostRepository costRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AppUserRepository userRepository;

    // Crea una nuova spesa associata a un gruppo e un utente
    public Cost createCost(Cost cost, Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        AppUser user = userRepository.findById(userId).orElse(null);

        if (group == null) {
            throw new IllegalArgumentException("Il gruppo con ID " + groupId + " non esiste.");
        }

        if (user == null) {
            throw new IllegalArgumentException("L'utente con ID " + userId + " non esiste.");
        }

        cost.setGroup(group);
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