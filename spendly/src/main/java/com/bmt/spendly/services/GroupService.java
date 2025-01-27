package com.bmt.spendly.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmt.spendly.models.AppUser;
import com.bmt.spendly.models.Group;
import com.bmt.spendly.repositories.AppUserRepository;
import com.bmt.spendly.repositories.GroupRepository;


@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AppUserRepository userRepository;



    public Group creaGruppo(String nome){
        Group gruppo=new Group(nome);
        return groupRepository.save(gruppo);
    }

     // Restituisce tutti i gruppi
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public void eliminaGruppo(Long groupId){
        groupRepository.deleteById(groupId);
    }

    public Group aggiungiMembro(Long groupId, Long UserId){
        Group gruppo = groupRepository.findById(groupId)
        .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        AppUser utente = userRepository.findById(UserId)
        .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        gruppo.AggiungiMembro(utente);
        return groupRepository.save(gruppo);
    }

    public Group rimuoviMembro(Long groupId, Long userId) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        AppUser utente = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        gruppo.RimuoviMembro(utente);
        return groupRepository.save(gruppo);
    }

    public List<AppUser> getMembri(Long groupId) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        return gruppo.getMembri();
    }

}
