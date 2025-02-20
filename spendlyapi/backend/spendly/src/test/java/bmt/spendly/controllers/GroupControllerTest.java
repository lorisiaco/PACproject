package bmt.spendly.controllers;


import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import bmt.spendly.models.Alert;
import bmt.spendly.models.AppUser;
import bmt.spendly.models.Cost;
import bmt.spendly.models.Group;
import bmt.spendly.models.Transazione;
import bmt.spendly.services.CostService;
import bmt.spendly.services.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @Mock
    private CostService costService;

    @InjectMocks
    private GroupController groupController;

    private AppUser user;
    private Group group;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUsername("testUser");
        group = new Group("Test Group", user);
    }

    @Test
    void testGetAllGroups() {
        when(groupService.getAllGroupsForUser("testUser")).thenReturn(Arrays.asList(group));

        ResponseEntity<?> response = groupController.getAllGroups("testUser");

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK , response.getStatusCode());
    }

    @Test
    void testAddGroup() {
        when(groupService.creaGruppo("Test Group", "testUser")).thenReturn(group);

        ResponseEntity<?> response = groupController.addGroup(group, "testUser");

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testEliminaGruppo() {
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(groupService.getUserByUsername("testUser")).thenReturn(user);
        doNothing().when(groupService).eliminaGruppo(1L);

        ResponseEntity<String> response = groupController.eliminaGruppo(1L, "testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddMemberToGroup() {
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(groupService.getUserByUsername("adminUser")).thenReturn(user);
        when(groupService.getUserByUsername("testUser")).thenReturn(user);
        when(groupService.aggiungiMembro(1L, "testUser")).thenReturn(group);

        ResponseEntity<String> response = groupController.addMemberToGroup(1L, "adminUser", "testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveMemberFromGroup() {
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(groupService.getUserByUsername("adminUser")).thenReturn(user);
        when(groupService.getUserByUsername("testUser")).thenReturn(user);
        when(groupService.rimuoviMembro(1L, "testUser")).thenReturn(group);

        ResponseEntity<String> response = groupController.removeMemberFromGroup(1L, "adminUser", "testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testShowGroup() {
        when(groupService.getGroupById(1L)).thenReturn(group);

        ResponseEntity<?> response = groupController.showGroup(1L);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAlertsByGroup() {
        Long groupId = 1L;
        when(groupService.getAlertsForGroup(groupId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Alert>> response = groupController.getAlertsByGroup(groupId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddAlertToGroup() {
        Long groupId = 1L;
        String adminUsername = "adminUser";
        Group mockGroup = new Group();
        AppUser adminUser = new AppUser();
        adminUser.setId(1);
        mockGroup.setAdmin(adminUser);

        Alert mockAlert = new Alert("Spesa Alta", 100.0, mockGroup);

        when(groupService.getGroupById(groupId)).thenReturn(mockGroup);
        when(groupService.getUserByUsername(adminUsername)).thenReturn(adminUser);
        when(groupService.creaAlert(anyString(), anyDouble(), eq(groupId))).thenReturn(mockAlert);

        ResponseEntity<?> response = groupController.addAlertToGroup(groupId, adminUsername, mockAlert);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testDeleteAlert() {
        Long groupId = 1L;
        Long alertId = 1L;
        String adminUsername = "adminUser";
        Group mockGroup = new Group();
        AppUser adminUser = new AppUser();
        adminUser.setId(1);
        mockGroup.setAdmin(adminUser);

        when(groupService.getGroupById(groupId)).thenReturn(mockGroup);
        when(groupService.getUserByUsername(adminUsername)).thenReturn(adminUser);
        doNothing().when(groupService).EliminaAlert(alertId);

        ResponseEntity<String> response = groupController.DeleteAlert(adminUsername, alertId, groupId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testOttimizzaDebiti() {
        Long groupId = 1L;
        List<Cost> mockSpese = Collections.emptyList();
        List<Transazione> mockTransazioni = Collections.emptyList();

        when(costService.getCostsByGroup(groupId)).thenReturn(mockSpese);

        ResponseEntity<List<Transazione>> response = groupController.OttimizzaDebiti(groupId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
}
