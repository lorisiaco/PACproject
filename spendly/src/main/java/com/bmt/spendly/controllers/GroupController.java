package com.bmt.spendly.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import com.bmt.spendly.models.AppUser;
import com.bmt.spendly.models.Group;
import com.bmt.spendly.services.GroupService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String getAllGroups(Model model) {
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        return "ListGroup"; // Mostra la pagina "ListGroup.html"
    }

    @GetMapping("/new")
    public String showCreateGroupForm(Model model) {
    return "GroupForm"; // Mostra la pagina "costform.html"
    }

    @PostMapping("/add")
    public String addGroup(@RequestParam("name") String name, Model model) {
        groupService.creaGruppo(name);
        System.out.println("ciao");
        model.addAttribute("success", true); // Aggiungi l'attributo success
        return "redirect:/groups";
    }

    @PostMapping("/delete/{groupId}")
    public ResponseEntity<String> eliminaGruppo(@PathVariable Long groupId) {
    try {
        groupService.eliminaGruppo(groupId);
        return ResponseEntity.ok("Group deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Failed to delete the group");
        }
    }

    @GetMapping("/edit/{groupId}")
    public String showGroup(@PathVariable Long groupId, Model model) {
        Group group = groupService.getGroupById(groupId); // Recupera il gruppo
        List<AppUser> membri = groupService.getMembri(groupId); // Recupera i membri del gruppo

        model.addAttribute("group", group);
        model.addAttribute("membri", membri); // Passa i membri alla vista

    return "showGroup"; // Mostra la pagina "showGroup.html"
    }



    @PostMapping("/edit/{groupId}/addMember")
    public String addMemberToGroup(@PathVariable Long groupId, @RequestParam String email) {
    groupService.aggiungiMembro(groupId, email); // Metodo per aggiungere un utente tramite email
    return "redirect:/groups/edit/" + groupId;
    }



    @PostMapping("edit/{groupId}/removeMember")
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable Long groupId, @RequestParam String email) {
        System.out.println("ciao");
        try {
            groupService.rimuoviMembro(groupId, email);
            return ResponseEntity.ok("Group deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Failed to delete the group");
            }
    }


    @GetMapping("/{groupId}/membri")
    public List<AppUser> getMembri(@PathVariable Long groupId) {
        return groupService.getMembri(groupId);
    }

}


