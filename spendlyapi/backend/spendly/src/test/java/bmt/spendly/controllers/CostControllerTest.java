package bmt.spendly.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import bmt.spendly.models.Cost;
import bmt.spendly.models.Group;
import bmt.spendly.services.CostService;
import bmt.spendly.repositories.CostRepository;


@ExtendWith(MockitoExtension.class)
class CostControllerTest {

    @Mock
    private CostService costService;

    @Mock
    private CostRepository costRepository;

    @InjectMocks
    private CostController costController;

    private Cost cost;
    private Group group;

    @BeforeEach
    void setUp() {
        group = new Group();
        group.setId(1L);
        
        cost = new Cost();
        cost.setCostId(1L);
        cost.setGroup(group);
        cost.setImporto(50.0);
    }

    @Test
    void testGetAllCostsWithoutUsername() {
        when(costService.getAllCosts()).thenReturn(Arrays.asList(cost));

        ResponseEntity<List<Cost>> response = costController.getAllCosts(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllCostsWithUsername() {
        when(costService.getCostsByUsername("testUser")).thenReturn(Arrays.asList(cost));

        ResponseEntity<List<Cost>> response = costController.getAllCosts("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCostsByGroup() {
        when(costService.getCostsByGroup(1L)).thenReturn(Arrays.asList(cost));

        ResponseEntity<List<Cost>> response = costController.getCostsByGroup(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCostByIdSuccess() {
        when(costService.getCostById(1L)).thenReturn(cost);

        ResponseEntity<?> response = costController.getCostById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cost, response.getBody());
    }

    @Test
    void testGetCostByIdNotFound() {
        when(costService.getCostById(2L)).thenThrow(new RuntimeException("Cost not found."));

        ResponseEntity<?> response = costController.getCostById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cost not found.", response.getBody());
    }

    @Test
    void testCreateCost() {
        when(costService.createCost(any(Cost.class), eq(1L), eq("testUser"))).thenReturn(cost);

        ResponseEntity<?> response = costController.createCost(cost, "testUser");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cost, response.getBody());
    }

    @Test
    void testUpdateCost() {
        when(costService.updateCost(eq(1L), any(Cost.class))).thenReturn(cost);

        ResponseEntity<?> response = costController.updateCost(1L, cost);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cost, response.getBody());
    }

    @Test
    void testUpdateCostNotFound() {
        when(costService.updateCost(eq(2L), any(Cost.class))).thenThrow(new RuntimeException("Cost not found."));

        ResponseEntity<?> response = costController.updateCost(2L, cost);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cost not found.", response.getBody());
    }

    @Test
    void testDeleteCostSuccess() {
        doNothing().when(costService).deleteCost(1L);

        ResponseEntity<?> response = costController.deleteCost(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cost deleted successfully.", response.getBody());
    }

    @Test
    void testDeleteCostNotFound() {
        doThrow(new RuntimeException("Cost not found.")).when(costService).deleteCost(2L);

        ResponseEntity<?> response = costController.deleteCost(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cost not found.", response.getBody());
    }
}

