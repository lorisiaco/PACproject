package bmt.spendly.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import bmt.spendly.models.Alert;
import bmt.spendly.models.AppUser;
import bmt.spendly.models.Group;
import bmt.spendly.repositories.AlertRepository;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.GroupRepository;


@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private GroupService groupService;

    private AppUser user;
    private Group group;
    private Alert testAlert;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUsername("testUser");
        group = new Group("Test Group", user);
        testAlert = new Alert("Test Alert", 50.0, group);
        group.addAlert(testAlert);
    }

    @Test
    void testCreaGruppo() {
        when(userRepository.findByUsernameIgnoreCase("testUser")).thenReturn(user);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group createdGroup = groupService.creaGruppo("Test Group", "testUser");
        
        assertNotNull(createdGroup);
        assertEquals("Test Group", createdGroup.getNome());
    }

    @Test
    void testCreaGruppo_UserNotFound() {
        when(userRepository.findByUsernameIgnoreCase("unknownUser")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.creaGruppo("Test Group", "unknownUser");
        });

        assertEquals("L'utente con Username unknownUser non esiste.", exception.getMessage());
    }

    @Test
    void testGetAllGroups() {
        List<Group> groups = Arrays.asList(group);
        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> result = groupService.getAllGroups();
        assertEquals(1, result.size());
    }

    @Test
    void testGetGroupById() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        Group foundGroup = groupService.getGroupById(1L);
        assertNotNull(foundGroup);
        assertEquals("Test Group", foundGroup.getNome());
    }

    @Test
    void testGetGroupById_NotFound() {
        when(groupRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.getGroupById(2L);
        });

        assertEquals("Gruppo non trovato con ID: 2", exception.getMessage());
    }

    @Test
    void testEliminaGruppo() {
        when(groupRepository.existsById(1L)).thenReturn(true);
        doNothing().when(groupRepository).deleteById(1L);

        assertDoesNotThrow(() -> groupService.eliminaGruppo(1L));
    }

    @Test
    void testEliminaGruppo_NotFound() {
        when(groupRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.eliminaGruppo(2L);
        });

        assertEquals("Il gruppo con ID 2 non esiste.", exception.getMessage());
    }

    @Test
    void testAggiungiMembro() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findByUsernameIgnoreCase("testUser")).thenReturn(user);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group updatedGroup = groupService.aggiungiMembro(1L, "testUser");
        assertNotNull(updatedGroup);
    }
    
    @Test
    void testRimuoviMembro() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findByUsernameIgnoreCase("testUser")).thenReturn(user);
        group.AggiungiMembro(user);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group updatedGroup = groupService.rimuoviMembro(1L, "testUser");
        assertNotNull(updatedGroup);
    }

    @Test
    void testGetAllGroupsForUser() {
        when(userRepository.findByUsernameIgnoreCase("testUser")).thenReturn(user);
        when(groupRepository.findByMembriContains(user)).thenReturn(Arrays.asList(group));

        List<Group> groups = groupService.getAllGroupsForUser("testUser");
        assertEquals(1, groups.size());
    }

    @Test
    void testCreaAlert() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(alertRepository.save(any(Alert.class))).thenReturn(testAlert);

        Alert result = groupService.creaAlert("Test Alert", 50.0, 1L);

        assertNotNull(result);
        assertEquals("Test Alert", result.getNome());
        assertEquals(50.0, result.getLimite());
        assertEquals(group, result.getGroup());
    }

    
    @Test
    void testEliminaAlert() {
        when(alertRepository.findById(1L)).thenReturn(Optional.of(testAlert));
        groupService.EliminaAlert(1L);
        verify(alertRepository, times(1)).delete(testAlert);
        
    }

    
    @Test
    void testGetAlertsForGroup() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        List<Alert> alerts = groupService.getAlertsForGroup(1L);

        assertNotNull(alerts);
        assertEquals(1, alerts.size());
        assertEquals("Test Alert", alerts.get(0).getNome());
    }
}
