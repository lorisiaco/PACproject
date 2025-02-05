package com.bookstore.BookStore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Group;
import com.bookstore.BookStore.repositories.AppUserRepository;
import com.bookstore.BookStore.repositories.GroupRepository;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AppUserRepository userRepository;


    /**
     * 
     * @param nome
     * @return Return the new Group created
     */
    public Group creaGruppo(String nome,String username){
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
    
        if (user == null) {
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }
        Group gruppo=new Group(nome,user);
        return groupRepository.save(gruppo);
    }

    /**
     * 
     * @return return the all Groups
     */
    public List<Group> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups;
    }

    public List<Group> getAllGroupsForUser(String username) {
        AppUser utente = userRepository.findByUsernameIgnoreCase(username);     //andrebbe aggiunta un' eccezione anche qua se inserisco un utente che non esiste
        if (utente == null) {
            throw new RuntimeException("Utente non trovato con l'email: " + username);
        }
        List<Group> groups = groupRepository.findByMembriContains(utente);
        return groups;
    }
    



    /**
     * 
     * @param id
     * @return return the Group through the Id 
     */
    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gruppo non trovato con ID: " + id));
    }
    
    /**
     * Metohod for Deleting a Group
     * @param groupId
     */
    public void eliminaGruppo(Long groupId){
        if (!groupRepository.existsById(groupId)) {
            throw new IllegalArgumentException("Il gruppo con ID " + groupId + " non esiste.");
        }
        groupRepository.deleteById(groupId);
    }

    /**
     * 
     * @param groupId
     * @param mail
     * @return Return the Group with the User added
     */
    public Group aggiungiMembro(Long groupId, String username){
        Group gruppo = groupRepository.findById(groupId)
        .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        AppUser utente = userRepository.findByUsernameIgnoreCase(username);     //andrebbe aggiunta un' eccezione anche qua se inserisco un utente che non esiste
        if (utente == null) {
            throw new RuntimeException("Utente non trovato con l'email: " + username);
        }

        if(gruppo.ContieneMembro(utente)){
            throw new IllegalArgumentException("L'utente è già un membro del gruppo!");
        }

        gruppo.AggiungiMembro(utente);
        return groupRepository.save(gruppo);
    }
    /**
     * 
     * @param groupId
     * @param mail
     * @return return the Group with the User removed
     */
    public Group rimuoviMembro(Long groupId, String username) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        AppUser utente = userRepository.findByUsernameIgnoreCase(username);
        if (utente == null) {
            throw new RuntimeException("Utente non trovato con l'email: " + username);
        }

        if(!gruppo.ContieneMembro(utente)){
            throw new IllegalArgumentException("L'utente è già un membro del gruppo!");
        }
                
        gruppo.RimuoviMembro(utente);
        return groupRepository.save(gruppo);
    }

    /**
     * 
     * @param groupId
     * @return reutnr the members's list of the Group
     */

    public List<AppUser> getMembri(Long groupId) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        return gruppo.getMembri();
    }

    public AppUser getUserByUsername(String username){
        return userRepository.findByUsernameIgnoreCase(username);
    }
}
