package bmt.spendly.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import bmt.spendly.models.Alert;
import bmt.spendly.models.Cost;
import bmt.spendly.models.Group;
import bmt.spendly.models.Transazione;

public interface GroupControllerIF {
    ResponseEntity<?> getAllGroups(@RequestParam String username);
    ResponseEntity<?> addGroup(@RequestBody Group group, @RequestParam String username);
    ResponseEntity<String> eliminaGruppo(@PathVariable Long groupId, @RequestParam String username);
    ResponseEntity<String> addMemberToGroup(@PathVariable Long groupId,
                                             @RequestParam String adminUsername,
                                             @RequestParam String memberUsername);
    ResponseEntity<String> removeMemberFromGroup(@PathVariable Long groupId,
                                                  @RequestParam String adminUsername,
                                                  @RequestParam String memberUsername);
    ResponseEntity<?> showGroup(@PathVariable Long groupId);
    ResponseEntity<List<Cost>> getCostsByGroup(@PathVariable Long groupId);
    ResponseEntity<List<Alert>> getAlertsByGroup(@PathVariable Long groupId);
    ResponseEntity<?> addAlertToGroup(@PathVariable Long groupId, @RequestParam String adminUsername, @RequestBody Alert alertRequest);
    ResponseEntity<String> DeleteAlert(@RequestParam String adminUsername, @PathVariable Long alertId, @PathVariable Long groupId);
    ResponseEntity<List<Transazione>> OttimizzaDebiti(@PathVariable Long groupId);
}
