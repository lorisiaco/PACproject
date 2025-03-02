package bmt.spendly.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmt.spendly.models.Alert;
import bmt.spendly.models.AppUser;
import bmt.spendly.models.ExpenseMacroArea;
import bmt.spendly.models.Group;
import bmt.spendly.repositories.AlertRepository;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.GroupRepository;
import jakarta.transaction.Transactional;

@Service
public class GroupService implements GroupMngtIF {
    
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AlertRepository alertRepository;

    /**
     * 
     * @param nome
     * @return Return the new Group created
     */
    @Override
    public Group creaGruppo(String nome, String username){
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new IllegalArgumentException("L'utente con Username " + username + " non esiste.");
        }
    
        Group gruppo = new Group(nome, user);
        // Oltre a impostare l'admin, aggiungiamo user ai membri
        gruppo.AggiungiMembro(user);
    
        return groupRepository.save(gruppo);
    }

    /**
     * 
     * @return return the all Groups
     */
    @Override
    public List<Group> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups;
    }

    @Override
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
    @Override
    public Group getGroupById(Long id) { // MODIFICATO da int a Long
        return groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gruppo non trovato con ID: " + id));
    }
    
    /**
     * Metohod for Deleting a Group
     * @param groupId
     */
    @Override
    public void eliminaGruppo(Long groupId){ // MODIFICATO da int a Long
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
    @Override
    public Group aggiungiMembro(Long groupId, String username){ // MODIFICATO da int a Long
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
    @Override
    public Group rimuoviMembro(Long groupId, String username) { // MODIFICATO da int a Long
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        AppUser utente = userRepository.findByUsernameIgnoreCase(username);
        if (utente == null) {
            throw new RuntimeException("Utente non trovato con l'email: " + username);
        }

        if(!gruppo.ContieneMembro(utente)){
            throw new IllegalArgumentException("L'utente non è nel gruppo!");
        }
                
        gruppo.RimuoviMembro(utente);
        return groupRepository.save(gruppo);
    }

    /**
     * 
     * @param groupId
     * @return reutnr the members's list of the Group
     */

    @Override
    public List<AppUser> getMembri(Long groupId) { // MODIFICATO da int a Long
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        return gruppo.getMembri();
    }

    @Override
    public AppUser getUserByUsername(String username){
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public Alert creaAlert(String nome, double importo, ExpenseMacroArea macroArea, Long groupId) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        Alert a = new Alert(nome, importo, macroArea, gruppo);
        gruppo.addAlert(a);
        return alertRepository.save(a);
    }


    @Override
    @Transactional
    public void EliminaAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new RuntimeException("Alert non trovato"));

        Group gruppo = alert.getGroup();
    
        if (gruppo != null) {
            gruppo.getAlerts().remove(alert); // Rimuove l'alert dalla lista
            groupRepository.save(gruppo); // Salva la modifica nel database
    }

    alertRepository.delete(alert); // Elimina l'alert dal database
}

    @Override
    public List<Alert> getAlertsForGroup(Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));

        return group.getAlerts();
    }


}
