package bmt.spendly.services;

import java.util.List;

import bmt.spendly.models.Cost;

public interface CostMngtIF {
    Cost createCost(Cost cost, Long groupId, String username);
    List<Cost> getCostsByUsername(String username);
    List<Cost> getCostsByGroup(Long groupId);
    List<Cost> getAllCosts();
    Cost getCostById(Long id);
    Cost updateCost(Long id, Cost updatedCost);
    void deleteCost(Long id);
    Cost payCostFromBudget(Long costId, String username);
}
