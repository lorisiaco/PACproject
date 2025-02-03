package bookstore.controllers;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookstore.BookStore.BookStoreApplication;
import com.bookstore.BookStore.controllers.GroupController;
import com.bookstore.BookStore.models.Group;
import com.bookstore.BookStore.services.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(GroupController.class)  // Solo il contesto per il controller
@ContextConfiguration(classes = BookStoreApplication.class)  // Specifica la configurazione dell'applicazione
//@WithMockUser(username = "testuser", roles = "USER")  // Usa l'autenticazione mockata
public class GroupControllerTest {
   
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

   


    @Test
    public void testGetAllGroups() throws Exception {
        // Simuliamo alcuni gruppi di esempio
        Group group1 = new Group( "Group 1");
        Group group2 = new Group( "Group 2");
        List<Group> groups = Arrays.asList(group1, group2);

        // Configura il mock del servizio
        when(groupService.getAllGroups()).thenReturn(groups);

        // Esegui la richiesta GET e verifica la risposta
        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Group 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Group 2"));
    }

    @Test
    
    public void testAddGroup() throws Exception {
        // Simuliamo la creazione di un gruppo
        Group newGroup = new Group("New Group");

        when(groupService.creaGruppo("New Group")).thenReturn(new Group( "New Group"));

        // Esegui la richiesta POST
        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nome").value("New Group"));
    }

    @Test
    
    public void testDeleteGroup() throws Exception {
        doNothing().when(groupService).eliminaGruppo(1L);

        mockMvc.perform(delete("/api/groups/{groupId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Group deleted successfully"));
    }

    @Test
    public void testAddMemberToGroup() throws Exception {
        doNothing().when(groupService).aggiungiMembro(1L, "user@example.com");

        mockMvc.perform(post("/api/groups/{groupId}/members", 1L)
                .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Member added successfully"));
    }

    @Test
    public void testRemoveMemberFromGroup() throws Exception {
        doNothing().when(groupService).rimuoviMembro(1L, "user@example.com");

        mockMvc.perform(delete("/api/groups/{groupId}/members", 1L)
                .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Member removed successfully"));
    }

    @Test
    public void testShowGroup() throws Exception {
        Group group = new Group( "Group 1");

        when(groupService.getGroupById(1L)).thenReturn(group);

        mockMvc.perform(get("/api/groups/{groupId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Group 1"));
    }
    

}
