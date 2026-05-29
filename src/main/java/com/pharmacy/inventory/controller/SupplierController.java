package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.SupplierRequest;
import com.pharmacy.inventory.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("suppliers", supplierService.getAll());
        return "supplier/supplier-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("supplierRequest", new SupplierRequest());
        return "supplier/supplier-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute SupplierRequest supplierRequest,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "supplier/supplier-form";
        try {
            supplierService.create(supplierRequest);
            redirectAttributes.addFlashAttribute("successMsg", "Supplier created successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/suppliers";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        var supplier = supplierService.getById(id);

        SupplierRequest request = new SupplierRequest();
        request.setSupplierName(supplier.getSupplierName());
        request.setLicenseNumber(supplier.getLicenseNumber());
        request.setTaxCode(supplier.getTaxCode());
        request.setAddress(supplier.getAddress());
        request.setContactPerson(supplier.getContactPerson());
        request.setPhone(supplier.getPhone());
        request.setEmail(supplier.getEmail());
        request.setIsActive(supplier.isActive());

        model.addAttribute("supplierRequest", request);
        model.addAttribute("supplierId", id);
        return "supplier/supplier-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute SupplierRequest supplierRequest,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "supplier/supplier-form";
        supplierService.update(id, supplierRequest);
        redirectAttributes.addFlashAttribute("successMsg", "Supplier updated successfully.");
        return "redirect:/suppliers";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable String id,
                             RedirectAttributes redirectAttributes) {
        supplierService.deactivate(id);
        redirectAttributes.addFlashAttribute("successMsg", "Supplier deactivated.");
        return "redirect:/suppliers";
    }
}