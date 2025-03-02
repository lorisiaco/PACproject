package bmt.spendly.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import bmt.spendly.models.Cost;

public interface CostControllerIF {
    ResponseEntity<List<Cost>> getAllCosts(String username);
    ResponseEntity<List<Cost>> getCostsByGroup(Long groupId);
    ResponseEntity<?> createCost(Cost cost, String username);
    ResponseEntity<?> getCostById(Long id);
    ResponseEntity<?> updateCost(Long id, Cost updatedCost);
    ResponseEntity<?> deleteCost(Long id);
    ResponseEntity<?> payCost(Long id, String username);
}
