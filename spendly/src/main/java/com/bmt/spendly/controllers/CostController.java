package com.bmt.spendly.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmt.spendly.models.Cost;
import com.bmt.spendly.services.AppUserService;
import com.bmt.spendly.services.CostService;
import com.bmt.spendly.services.GroupService;
import org.springframework.ui.Model;

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
        return "list"; // Mostra la pagina "list.html"
    }

    // Mostra il form per aggiungere una nuova spesa
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cost", new Cost());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("users", userService.getAllUsers());
        return "form"; // Mostra la pagina "form.html"
    }

    // Gestisce l'invio del form per creare una nuova spesa
    @PostMapping
    public String createCost(@ModelAttribute("cost") Cost cost,
                             @RequestParam("groupId") Long groupId,
                             @RequestParam("userId") int userId) {
        costService.createCost(cost, groupId, userId);
        return "redirect:/costs"; // Reindirizza alla lista delle spese
    }

    // Mostra il form per aggiornare una spesa esistente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Cost cost = costService.getCostById(id);
        model.addAttribute("cost", cost);
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("users", userService.getAllUsers());
        return "form"; // Mostra la pagina "form.html" (stessa del "create")
    }

    // Gestisce l'invio del form per aggiornare una spesa
    @PostMapping("/update/{id}")
    public String updateCost(@PathVariable int id, @ModelAttribute("cost") Cost updatedCost) {
        costService.updateCost(id, updatedCost);
        return "redirect:/costs"; // Reindirizza alla lista delle spese
    }

    // Elimina una spesa
    @GetMapping("/delete/{id}")
    public String deleteCost(@PathVariable int id) {
        costService.deleteCost(id);
        return "redirect:/costs"; // Reindirizza alla lista delle spese
    }
}
