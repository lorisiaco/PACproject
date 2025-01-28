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
import org.springframework.web.bind.annotation.ResponseBody;


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

    @PostMapping("/{groupId}/membri")
    public Group aggiungiMembro(@PathVariable Long groupId, @RequestParam Long userId) {
        return groupService.aggiungiMembro(groupId, userId);
    }


    @DeleteMapping("/{groupId}/membri")
    public Group rimuoviMembro(@PathVariable Long groupId, @RequestParam Long userId) {
        return groupService.rimuoviMembro(groupId, userId);
    }


    @GetMapping("/{groupId}/membri")
    public List<AppUser> getMembri(@PathVariable Long groupId) {
        return groupService.getMembri(groupId);
    }

}


