package bmt.spendly.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bmt.spendly.models.Cost;
import bmt.spendly.repositories.CostRepository;
import bmt.spendly.services.CostService;

@RestController
@RequestMapping("/api/costs")
public class CostController implements CostControllerIF {

    private static final Logger logger = LoggerFactory.getLogger(CostController.class);

    @Autowired
    private CostService costService;

    @Autowired
    private CostRepository costRepository;

    @Override
    @GetMapping
    public ResponseEntity<List<Cost>> getAllCosts(@RequestParam(value = "username", required = false) String username) {
        List<Cost> costs;
        if (username != null && !username.trim().isEmpty()) {
            // Assicurarsi che il metodo getCostsByUsername() restituisca tutte le spese (sia pagate che non pagate)
            costs = costService.getCostsByUsername(username);
        } else {
            costs = costService.getAllCosts();
        }
        return ResponseEntity.ok(costs);
    }

    @Override
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Cost>> getCostsByGroup(@PathVariable Long groupId) {
        List<Cost> costs = costService.getCostsByGroup(groupId);
        return ResponseEntity.ok(costs);
    }

    @Override
    @PostMapping
    public ResponseEntity<?> createCost(@RequestBody Cost cost,
                                       @RequestParam(value = "username", required = true) String username) {
        logger.info("Creating cost: {}, GroupId: {}, Username: {}", cost, cost.getGroup(), username);

        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
        }

        try {
            Long groupId = (cost.getGroup() != null) ? cost.getGroup().getId() : null;
            Cost newCost = costService.createCost(cost, groupId, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCost);
        } catch (Exception e) {
            logger.error("Error creating cost: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating cost.");
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getCostById(@PathVariable Long id) {
        try {
            Cost cost = costService.getCostById(id);
            return ResponseEntity.ok(cost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cost not found.");
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCost(@PathVariable Long id, @RequestBody Cost updatedCost) {
        try {
            Cost cost = costService.updateCost(id, updatedCost);
            return ResponseEntity.ok(cost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cost not found.");
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCost(@PathVariable Long id) {
        try {
            costService.deleteCost(id);
            return ResponseEntity.ok("Cost deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cost not found.");
        }
    }

    @Override
    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payCost(@PathVariable Long id,
                                     @RequestParam String username) {
        try {
            // Il metodo payCostFromBudget deve aggiornare solo lo stato della spesa senza escluderla dallo storico
            Cost updated = costService.payCostFromBudget(id, username);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}