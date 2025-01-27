package com.bmt.spendly.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bmt.spendly.models.Cost;
import com.bmt.spendly.services.AppUserService;
import com.bmt.spendly.services.CostService;
import com.bmt.spendly.services.GroupService;

@Controller
@RequestMapping("/costs")
public class CostController {

    @Autowired
    private CostService costService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AppUserService userService;

    // Mostra tutte le spese
    @GetMapping
    public String getAllCosts(Model model) {
        List<Cost> costs = costService.getAllCosts();
        model.addAttribute("costs", costs);
        return "listboot"; // Mostra la pagina "listboot.html"
    }

    // Mostra il form per aggiungere una nuova spesa
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cost", new Cost());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("users", userService.getAllUsers());
        return "formboot"; // Mostra la pagina "formboot.html"
    }

    @PostMapping("/new")
    public String createCost(@ModelAttribute("cost") Cost cost,
                            @RequestParam("groupId") Long groupId,
                            @RequestParam("userId") Long userId,
                            Model model) {
        try {
            System.out.println("Received cost: " + cost.getImporto() + ", " + cost.getData() + ", " + cost.getTipologia());
            System.out.println("GroupId: " + groupId + ", UserId: " + userId);
            costService.createCost(cost, groupId, userId);
            return "redirect:/costs";
        } catch (Exception e) {
            System.out.println("Error creating cost: " + e.getMessage());
            e.printStackTrace();
            // Add error message to model and return to form
            model.addAttribute("error", e.getMessage());
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("users", userService.getAllUsers());
            return "formboot";
        }
    }

    // Mostra il form per aggiornare una spesa esistente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Cost cost = costService.getCostById(id);
        model.addAttribute("cost", cost);
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("users", userService.getAllUsers());
        return "formboot"; // Mostra la pagina "formboot.html" per modificare
    }

    // Gestisce l'invio del form per aggiornare una spesa
    @PostMapping("/edit/{id}")
    public String updateCost(@PathVariable Long id, 
                            @ModelAttribute("cost") Cost updatedCost,
                            @RequestParam("groupId") Long groupId,
                            @RequestParam("userId") Long userId) {
        costService.updateCost(id, updatedCost, groupId, userId);
        return "redirect:/costs";
    }

    // Elimina una spesa
    @GetMapping("/delete/{id}")
    public String deleteCost(@PathVariable Long id) {
        costService.deleteCost(id);
        return "redirect:/costs"; // Reindirizza alla lista delle spese
    }
}