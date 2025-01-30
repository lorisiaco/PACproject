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
        List<Group> groups = groupRepository.findAll();
        return groups;
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gruppo non trovato con ID: " + id));
    }
    

    public void eliminaGruppo(Long groupId){
        groupRepository.deleteById(groupId);
    }

    public Group aggiungiMembro(Long groupId, String mail){
        Group gruppo = groupRepository.findById(groupId)
        .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        AppUser utente = userRepository.findByEmail(mail);

        if(gruppo.ContieneMembro(utente)){
            throw new IllegalArgumentException("L'utente è già un membro del gruppo!");
        }

        gruppo.AggiungiMembro(utente);
        return groupRepository.save(gruppo);
    }

    public Group rimuoviMembro(Long groupId, String mail) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        AppUser utente = userRepository.findByEmail(mail);
                
        gruppo.RimuoviMembro(utente);
        return groupRepository.save(gruppo);
    }

    public List<AppUser> getMembri(Long groupId) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        return gruppo.getMembri();
    }

}
