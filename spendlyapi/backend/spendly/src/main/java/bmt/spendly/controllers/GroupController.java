package bmt.spendly.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import bmt.spendly.models.Alert;
import bmt.spendly.models.AppUser;
import bmt.spendly.models.Cost;
import bmt.spendly.models.Group;
import bmt.spendly.models.GroupResponseDTO;
import bmt.spendly.services.CostService;
import bmt.spendly.services.GroupService;

@RestController
@RequestMapping("/api/groups")  
public class GroupController {
    
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private CostService costService;

    //  api per visualizzare la lista dei gruppi in cui è presente l'utente
    @GetMapping
    public ResponseEntity<?> getAllGroups(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
        }
        List<Group> groups = groupService.getAllGroupsForUser(username);
        return ResponseEntity.ok(groups);
    }

    // api per creare un gruppo
    @PostMapping
    public ResponseEntity<?> addGroup(@RequestBody Group group, @RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
        }
        try {
            Group newGroup = groupService.creaGruppo(group.getNome(), username);
            return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
        } catch (Exception e) {
            logger.error("Error creating Group: " + e.getMessage(), e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Impossibile creare il gruppo: " + e.getMessage());
        }
    }

    //api per eliminare un gruppo ( solo per amministratore)
    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> eliminaGruppo(@PathVariable Long groupId, @RequestParam String username) {
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
            logger.error("Error deleting Group: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to delete the group: " + e.getMessage());
        }
    }

    // api per aggiungere membri (solo per amministratore)
    @PostMapping("/{groupId}/members")
    @Transactional
    public ResponseEntity<String> addMemberToGroup(@PathVariable Long groupId,
                                                   @RequestParam String adminUsername,
                                                   @RequestParam String memberUsername) {
        try {
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }
            AppUser admin = groupService.getUserByUsername(adminUsername);
            AppUser utente = groupService.getUserByUsername(memberUsername);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
            if (utente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User to add not found");
            }

            if (group.getAdmin().getId() != admin.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .body("Only the admin can add members to this group.");
            }

            groupService.aggiungiMembro(groupId, memberUsername);
            return ResponseEntity.ok("Member added successfully");
        } catch (Exception e) {
            logger.error("Error adding member to Group: " + e.getMessage(), e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to add member: " + e.getMessage());
        }
    }

    //api per eliminare membri (solo per amministratore)
    @DeleteMapping("/{groupId}/members")
    @Transactional
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable Long groupId,
                                                        @RequestParam String adminUsername,
                                                        @RequestParam String memberUsername) {
        try {
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }
            AppUser admin = groupService.getUserByUsername(adminUsername);
            AppUser utente = groupService.getUserByUsername(memberUsername);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
            if (utente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User to remove not found");
            }

            if (group.getAdmin().getId() != admin.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .body("Only the admin can remove members from this group.");
            }

            groupService.rimuoviMembro(groupId, memberUsername);
            return ResponseEntity.ok("Member removed successfully");
        } catch (Exception e) {
            logger.error("Error removing member from Group: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to remove the member: " + e.getMessage());
        }
    }

    // api per entrare nei dettagli del gruppo
    @GetMapping("/{groupId}")
    public ResponseEntity<?> showGroup(@PathVariable Long groupId) {
        try {
            Group group = groupService.getGroupById(groupId);
            List<Cost> costs = costService.getCostsByGroup(groupId);
            List<Alert> alerts = groupService.getAlertsForGroup(groupId);
            GroupResponseDTO responseDTO = new GroupResponseDTO(group, costs, alerts);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error retrieving Group: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Group not found: " + e.getMessage());
        }
    }

    
    // api per visualizzare le spese del gruppo
    @GetMapping("/costs/{groupId}")
    public ResponseEntity<List<Cost>> getCostsByGroup(@PathVariable Long groupId) {
        List<Cost> costs = costService.getCostsByGroup(groupId);
        return ResponseEntity.ok(costs);
    }
    //api per visualizzare gli alerts del gruppo
    @GetMapping("/{groupId}/alerts")
    public ResponseEntity<List<Alert>> getAlertsByGroup(@PathVariable Long groupId) {
        List<Alert> alerts = groupService.getAlertsForGroup(groupId);
        return ResponseEntity.ok(alerts);
    }
    //api per aggiungere alert ( solo per amministratore)
    @PostMapping("/{groupId}/alerts")
    public ResponseEntity<?> addAlertToGroup(@PathVariable Long groupId, @RequestParam String adminUsername, @RequestBody Alert alertRequest) {
        try {
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }
            AppUser user = groupService.getUserByUsername(adminUsername);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
            if (group.getAdmin().getId() != user.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the admin can create Alert");
            }
            Alert newAlert = groupService.creaAlert(alertRequest.getNome(), alertRequest.getLimite(),groupId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAlert);
        }
        catch (Exception e) {
            logger.error("Error creating an Alert: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to create an Alert: " + e.getMessage());
        }
    }
    //api per eliminare alert (solo per amministratore)
    @DeleteMapping("/{groupId}/alerts/{alertId}")
    public ResponseEntity<String> DeleteAlert( @RequestParam String adminUsername, @PathVariable Long alertId, @PathVariable Long groupId) {
        try {
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }
            AppUser user = groupService.getUserByUsername(adminUsername);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
            if (group.getAdmin().getId() != user.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the admin can delete Alert");
            }
            groupService.EliminaAlert(alertId);
            return ResponseEntity.ok("Alert deleted successfully");
        }
        catch (Exception e) {
            logger.error("Error creating an Alert: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to create an Alert: " + e.getMessage());
        }
    }
}
