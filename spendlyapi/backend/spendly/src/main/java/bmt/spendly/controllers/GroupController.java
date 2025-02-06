package bmt.spendly.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Cost;
import bmt.spendly.models.Group;
import bmt.spendly.services.CostService;
import bmt.spendly.services.GroupService;

@RestController
@RequestMapping("/api/groups")  // Modificato per seguire best practice REST
public class GroupController {
    
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private CostService costService;

    // ===========================
    // 1) Recupera i gruppi di un utente
    // ===========================
    @GetMapping
    public ResponseEntity<?> getAllGroups(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
        }
        List<Group> groups = groupService.getAllGroupsForUser(username);
        return ResponseEntity.ok(groups);
    }

    // ===========================
    // 2) Crea un nuovo gruppo
    // ===========================
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
            // Se vuoi restituire il messaggio specifico dell'eccezione:
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Impossibile creare il gruppo: " + e.getMessage());
        }
    }

    // ===========================
    // 3) Elimina un gruppo
    // ===========================
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

    // ===========================
    // 4) Aggiunge un membro a un gruppo
    // ===========================
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
            // Ritorniamo direttamente l'eccezione, se vuoi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to add member: " + e.getMessage());
        }
    }

    // ===========================
    // 5) Rimuove un membro
    // ===========================
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

    // ===========================
    // 6) Restituisce un gruppo singolo
    // ===========================
    @GetMapping("/{groupId}")
    public ResponseEntity<?> showGroup(@PathVariable Long groupId) {
        try {
            Group group = groupService.getGroupById(groupId);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            logger.error("Error retrieving Group: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Group not found: " + e.getMessage());
        }
    }

    // ===========================
    // 7) Restituisce i Cost di un gruppo
    // ===========================
    @GetMapping("/costs/{groupId}")
    public ResponseEntity<List<Cost>> getCostsByGroup(@PathVariable Long groupId) {
        List<Cost> costs = costService.getCostsByGroup(groupId);
        return ResponseEntity.ok(costs);
    }
}
