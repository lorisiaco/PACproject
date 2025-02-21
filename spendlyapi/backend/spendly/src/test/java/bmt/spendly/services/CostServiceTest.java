package bmt.spendly.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import bmt.spendly.models.AppUser;
import bmt.spendly.models.Cost;
import bmt.spendly.models.Group;
import bmt.spendly.repositories.AppUserRepository;
import bmt.spendly.repositories.CostRepository;
import bmt.spendly.repositories.GroupRepository;


@ExtendWith(MockitoExtension.class)
class CostServiceTest {
    
    @Mock
    private CostRepository costRepository;
    
    @Mock
    private GroupRepository groupRepository;
    
    @Mock
    private AppUserRepository userRepository;
    
    @InjectMocks
    private CostService costService;
    
    private AppUser testUser;
    private Group testGroup;
    private Cost testCost;
    
    
    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1);
        testUser.setUsername("testuser");
        
        testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setNome("Test Group");
        
        testCost = new Cost();
        testCost.setCostId(1L);
        testCost.setImporto(100.0);
        testCost.setUser(testUser);
        testCost.setGroup(testGroup);
    }
    
    @Test
    void testCreateCost() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        when(costRepository.save(any(Cost.class))).thenReturn(testCost);
        Cost result = costService.createCost(new Cost(), 1L, "testuser");
        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testGroup, result.getGroup());
        
    }
    
    @Test
    void testGetCostsByUsername() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);
        when(costRepository.findByUserId(1)).thenReturn(Arrays.asList(testCost));
        List<Cost> costs = costService.getCostsByUsername("testuser");
        assertNotNull(costs);
        assertEquals(1, costs.size());
        assertEquals(testCost, costs.get(0));
        
        
    }
    
    @Test
    void testGetCostsByGroup() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        when(costRepository.findByGroupId(1L)).thenReturn(Arrays.asList(testCost));
        List<Cost> costs = costService.getCostsByGroup(1L);
        assertNotNull(costs);
        assertEquals(1, costs.size());
        assertEquals(testCost, costs.get(0));
        
        
    }
    
    @Test
    void testGetAllCosts() {
        when(costRepository.findAll()).thenReturn(Arrays.asList(testCost));
        List<Cost> costs = costService.getAllCosts();
        assertNotNull(costs);
        assertEquals(1, costs.size());
        
       
    }
    
    @Test
    void testGetCostById() {
        when(costRepository.findById(1L)).thenReturn(Optional.of(testCost));
        Cost result = costService.getCostById(1L);
        assertNotNull(result);
        assertEquals(testCost, result);
        
        
    }
    
    @Test
    void testUpdateCost() {
        Cost updatedCost = new Cost();
        updatedCost.setImporto(200.0);
        when(costRepository.findById(1L)).thenReturn(Optional.of(testCost));
        when(costRepository.save(any(Cost.class))).thenReturn(testCost);
        Cost result = costService.updateCost(1L, updatedCost);
        assertNotNull(result);
        assertEquals(200.0, result.getImporto());
        
        
        
    }
    
    @Test
    void testDeleteCost() {
        when(costRepository.existsById(1L)).thenReturn(true);
        doNothing().when(costRepository).deleteById(1L);
        costService.deleteCost(1L);
        
    }
}
