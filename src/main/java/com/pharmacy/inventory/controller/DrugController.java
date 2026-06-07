package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.DrugRequest;
import com.pharmacy.inventory.enums.DosageForm;
import com.pharmacy.inventory.service.DrugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/drugs")
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("drugs", drugService.search(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("drugs", drugService.getAll());
        }
        return "drug/drug-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("drugRequest", new DrugRequest());
        model.addAttribute("dosageForms", DosageForm.values());
        return "drug/drug-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute DrugRequest drugRequest,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("dosageForms", DosageForm.values());
            return "drug/drug-form";
        }
        try {
            drugService.create(drugRequest);
            redirectAttributes.addFlashAttribute("successMsg", "Drug created successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/drugs";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        var drug = drugService.getById(id);
        var stock = drugService.getInventoryStockService().getByDrugId(id);

        DrugRequest request = new DrugRequest();
        request.setDrugName(drug.getDrugName());
        request.setActiveIngredient(drug.getActiveIngredient());
        request.setConcentration(drug.getConcentration());
        request.setDosageForm(drug.getDosageForm());
        request.setRegistrationNumber(drug.getRegistrationNumber());
        request.setManufacturer(drug.getManufacturer());
        request.setCountryOfOrigin(drug.getCountryOfOrigin());
        request.setUnit(drug.getUnit());
        request.setPackagingSpec(drug.getPackagingSpec());
        request.setStorageCondition(drug.getStorageCondition());
        request.setMinimumThreshold(stock.getMinimumThreshold());

        model.addAttribute("drugRequest", request);
        model.addAttribute("drugId", id);
        model.addAttribute("dosageForms", DosageForm.values());
        return "drug/drug-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute DrugRequest drugRequest,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("dosageForms", DosageForm.values());
            model.addAttribute("drugId", id);
            return "drug/drug-form";
        }
        drugService.update(id, drugRequest);
        redirectAttributes.addFlashAttribute("successMsg", "Drug updated successfully.");
        return "redirect:/drugs";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable String id,
                             RedirectAttributes redirectAttributes) {
        drugService.deactivate(id);
        redirectAttributes.addFlashAttribute("successMsg", "Drug deactivated.");
        return "redirect:/drugs";
    }

    @PostMapping("/activate/{id}")
    public String activate(@PathVariable String id,
                           RedirectAttributes redirectAttributes) {
        drugService.activate(id);
        redirectAttributes.addFlashAttribute("successMsg", "Drug activated.");
        return "redirect:/drugs";
    }
}




