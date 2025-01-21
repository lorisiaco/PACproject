package spendly.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import spendly.models.Group;
import spendly.models.UserApp;
import spendly.repositories.GroupRepository;
import spendly.repositories.UserRepository;

public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;



    public Group creaGruppo(String nome){
        Group gruppo=new Group(nome);
        return groupRepository.save(gruppo);
    }

    public void eliminaGruppo(Long groupId){
        groupRepository.deleteById(groupId);
    }

    public Group aggiungiMembro(Long groupId, Integer UserId){
        Group gruppo = groupRepository.findById(groupId)
        .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        UserApp utente = userRepository.findById(UserId)
        .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        gruppo.AggiungiMembro(utente);
        return groupRepository.save(gruppo);
    }

    public Group rimuoviMembro(Long groupId, Integer userId) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        UserApp utente = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        gruppo.RimuoviMembro(utente);
        return groupRepository.save(gruppo);
    }

    public List<UserApp> getMembri(Long groupId) {
        Group gruppo = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Gruppo non trovato"));
        return gruppo.getMembri();
    }

}
