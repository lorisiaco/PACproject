package com.bookstore.BookStore.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Cost;
import com.bookstore.BookStore.models.Group;
import com.bookstore.BookStore.services.CostService;
import com.bookstore.BookStore.services.GroupService;

@RestController
@RequestMapping("/api/groups")  // Modificato per seguire best practice REST

public class GroupController {
    
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private CostService costService;

    /**
     * Return all the groups in the DB
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getAllGroups(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
        }
        List<Group> groups = groupService.getAllGroupsForUser(username);
        return ResponseEntity.ok(groups);
    }

    /**
     * Create a new Group in the DB
     * @param group
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addGroup(@RequestBody Group group, @RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
        }
        try{
        Group newGroup = groupService.creaGruppo(group.getNome(),username);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
        } catch(Exception e){
            logger.error("Error creating Group: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating cost.");
        }
    }

    /**
     * Delete a Group on the Id
     * @param groupId
     * @return
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> eliminaGruppo(@PathVariable int groupId, @RequestParam String username) {
        try {
            Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        AppUser user = groupService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }
        if (group.getAdmin().getId() != user.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the admin can delete this group.");
        }
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
    public ResponseEntity<String> addMemberToGroup(@PathVariable int groupId, @RequestParam String adminUsername,
                                                   @RequestParam String memberUsername) {
        try{
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }
            AppUser admin = groupService.getUserByUsername(adminUsername);
            AppUser utente=groupService.getUserByUsername(memberUsername);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
            if (utente==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            if (group.getAdmin().getId() != admin.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the admin can delete this group.");
            }        
                                                   
        groupService.aggiungiMembro(groupId, memberUsername);
        return ResponseEntity.ok("Member added successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add member.");
        }
    }

    /**
     * Remove an user from a Group 
     * @param groupId
     * @param email
     * @return
     */
    @DeleteMapping("/{groupId}/members")
    @Transactional
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable int groupId, @RequestParam String adminUsername,
                                                        @RequestParam String memberUsername) {
        try {
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }
            AppUser admin = groupService.getUserByUsername(adminUsername);
            AppUser utente=groupService.getUserByUsername(memberUsername);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
            if (utente==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            if (group.getAdmin().getId() != admin.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the admin can delete this group.");
            }   
            groupService.rimuoviMembro(groupId, memberUsername);
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
    public ResponseEntity<?> showGroup(@PathVariable int groupId) {
       try{
        Group group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(group);
       }catch (Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found.");
       }
    }

    @GetMapping("/costs/{groupId}")
    public ResponseEntity<List<Cost>> getCostsByGroup(@PathVariable int groupId) {
        List<Cost> costs = costService.getCostsByGroup(groupId);
        return ResponseEntity.ok(costs);
    }





}
