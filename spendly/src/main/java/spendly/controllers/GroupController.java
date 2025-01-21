package spendly.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import spendly.models.Group;
import spendly.models.UserApp;
import spendly.services.GroupService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public Group creaGruppo(@RequestParam String nome) {
        return groupService.creaGruppo(nome);
    }

        
    @DeleteMapping("/{groupId}")
    public void eliminaGruppo(@PathVariable Long groupId) {
        groupService.eliminaGruppo(groupId);
    }

    
    @PostMapping("/{groupId}/membri")
    public Group aggiungiMembro(@PathVariable Long groupId, @RequestParam Integer userId) {
        return groupService.aggiungiMembro(groupId, userId);
    }

    
    @DeleteMapping("/{groupId}/membri")
    public Group rimuoviMembro(@PathVariable Long groupId, @RequestParam Integer userId) {
        return groupService.rimuoviMembro(groupId, userId);
    }

    
    @GetMapping("/{groupId}/membri")
    public List<UserApp> getMembri(@PathVariable Long groupId) {
        return groupService.getMembri(groupId);
    }


    



}
