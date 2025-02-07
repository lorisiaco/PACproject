package bmt.spendly.controllers;


import static org.mockito.Mockito.*;
import bmt.spendly.models.AppUser;
import bmt.spendly.models.Group;
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

    
}
