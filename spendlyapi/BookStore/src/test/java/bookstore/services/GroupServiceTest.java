package bookstore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Group;
import com.bookstore.BookStore.repositories.AppUserRepository;
import com.bookstore.BookStore.repositories.GroupRepository;
import com.bookstore.BookStore.services.GroupService;

public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreaGruppo() {
        // Arrange
        Group newGroup = new Group("Test Group");
        when(groupRepository.save(any(Group.class))).thenReturn(newGroup);

        // Act
        Group result = groupService.creaGruppo("Test Group");

        // Assert
        assertNotNull(result);
        assertEquals("Test Group", result.getNome());
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testGetAllGroups() {
        // Arrange
        Group group1 = new Group("Group 1");
        Group group2 = new Group("Group 2");
        when(groupRepository.findAll()).thenReturn(Arrays.asList(group1, group2));

        // Act
        List<Group> result = groupService.getAllGroups();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void testGetGroupById_GroupExists() {
        // Arrange
        Group group = new Group("Test Group");
        group.setId(1L);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        // Act
        Group result = groupService.getGroupById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    void testGetGroupById_GroupDoesNotExist() {
        // Arrange
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> groupService.getGroupById(1L));
        assertEquals("Gruppo non trovato con ID: 1", exception.getMessage());
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    void testEliminaGruppo() {
        // Act
        groupService.eliminaGruppo(1L);

        // Assert
        verify(groupRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAggiungiMembro_Success() {
        // Arrange
        Group group = new Group("Test Group");
        group.setId(1L);
        AppUser user = new AppUser();
        user.setEmail("test@example.com");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(groupRepository.save(group)).thenReturn(group);

        // Act
        Group result = groupService.aggiungiMembro(1L, "test@example.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.ContieneMembro(user));
        verify(groupRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testAggiungiMembro_UserAlreadyMember() {
        // Arrange
        Group group = new Group("Test Group");
        group.setId(1L);
        AppUser user = new AppUser();
        user.setEmail("test@example.com");
        group.AggiungiMembro(user);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> groupService.aggiungiMembro(1L, "test@example.com"));
        assertEquals("L'utente è già un membro del gruppo!", exception.getMessage());
        verify(groupRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testRimuoviMembro_Success() {
        // Arrange
        Group group = new Group("Test Group");
        group.setId(1L);
        AppUser user = new AppUser();
        user.setEmail("test@example.com");
        group.AggiungiMembro(user);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(groupRepository.save(group)).thenReturn(group);

        // Act
        Group result = groupService.rimuoviMembro(1L, "test@example.com");

        // Assert
        assertNotNull(result);
        assertFalse(result.ContieneMembro(user));
        verify(groupRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testGetMembri() {
        // Arrange
        Group group = new Group("Test Group");
        group.setId(1L);
        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();
        group.AggiungiMembro(user1);
        group.AggiungiMembro(user2);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        // Act
        List<AppUser> result = groupService.getMembri(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(groupRepository, times(1)).findById(1L);
    }
}
