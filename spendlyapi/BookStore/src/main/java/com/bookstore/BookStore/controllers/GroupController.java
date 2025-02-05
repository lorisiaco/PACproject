package com.bookstore.BookStore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.BookStore.models.Cost;
import com.bookstore.BookStore.models.Group;
import com.bookstore.BookStore.services.CostService;
import com.bookstore.BookStore.services.GroupService;

@RestController
@RequestMapping("/api/groups")  // Modificato per seguire best practice REST

public class GroupController {
    

    @Autowired
    private GroupService groupService;

    @Autowired
    private CostService costService;

    /**
     * Return all the groups in the DB
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    /**
     * Create a new Group in the DB
     * @param group
     * @return
     */
    @PostMapping
    public ResponseEntity<Group> addGroup(@RequestBody Group group) {
        Group newGroup = groupService.creaGruppo(group.getNome());
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }

    /**
     * Delete a Group on the Id
     * @param groupId
     * @return
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> eliminaGruppo(@PathVariable Long groupId) {
        try {
            groupService.eliminaGruppo(groupId);
            return ResponseEntity.ok("Group deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to delete the group");
        }
    }

    /**
     * Add a User to a Group on the Id and the email of the User
     * @param groupId
     * @param email
     * @return
     */
    @PostMapping("/{groupId}/members")
    @Transactional
    public ResponseEntity<String> addMemberToGroup(@PathVariable Long groupId, @RequestParam String email) {
        groupService.aggiungiMembro(groupId, email);
        return ResponseEntity.ok("Member added successfully");
    }

    /**
     * Remove an user from a Group 
     * @param groupId
     * @param email
     * @return
     */
    @DeleteMapping("/{groupId}/members")
    @Transactional
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable Long groupId, @RequestParam String email) {
        try {
            groupService.rimuoviMembro(groupId, email);
            return ResponseEntity.ok("Member removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to remove the member");
        }
    }

    /**
     * Get 
     * @param groupId
     * @return
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<Group> showGroup(@PathVariable Long groupId) {
        Group group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/costs/{groupId}")
    public ResponseEntity<List<Cost>> getCostsByGroup(@PathVariable Long groupId) {
        List<Cost> costs = costService.getCostsByGroup(groupId);
        return ResponseEntity.ok(costs);
    }





}
