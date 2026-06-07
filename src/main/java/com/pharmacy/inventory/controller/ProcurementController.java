package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.RequisitionItemRequest;
import com.pharmacy.inventory.dto.request.RequisitionRequest;
import com.pharmacy.inventory.model.Drug;
import com.pharmacy.inventory.model.DrugRequisition;
import com.pharmacy.inventory.service.DrugService;
import com.pharmacy.inventory.service.RequisitionService;
import com.pharmacy.inventory.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/purchase/requisitions")
@RequiredArgsConstructor
public class ProcurementController {

    private final RequisitionService requisitionService;
    private final DrugService drugService;
    private final SupplierService supplierService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("requisitions", requisitionService.getAll());
        return "procurement/requisition-list";
    }

    @GetMapping("/create")
    public String createForm(Model model, @RequestParam(value = "drugId", required = false) String drugId) {
        RequisitionRequest request = new RequisitionRequest();
        request.setItems(new ArrayList<>());

        if (drugId != null) {
            Drug drug = drugService.getById(drugId);
            RequisitionItemRequest item = new RequisitionItemRequest();
            item.setDrugId(drugId);
            item.setUnit(drug.getUnit());
            item.setPackagingSpec(drug.getPackagingSpec());
            request.getItems().add(item);
        } else {
            // Add one empty item by default
            request.getItems().add(new RequisitionItemRequest());
        }

        model.addAttribute("requisitionRequest", request);
        model.addAttribute("drugs", drugService.getAllActive());
        model.addAttribute("suppliers", supplierService.getAllActive());
        return "procurement/requisition-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute RequisitionRequest request,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        try {
            requisitionService.create(request, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Procurement forecast created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
            return "redirect:/purchase/requisitions/create";
        }
        return "redirect:/purchase/requisitions";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        DrugRequisition requisition = requisitionService.getById(id);
        model.addAttribute("req", requisition);
        return "procurement/requisition-view";
    }

    @PostMapping("/item/approve/{itemId}")
    public String approveItem(@PathVariable String itemId,
                              @RequestParam String requisitionId,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        requisitionService.approveItem(itemId, principal.getName());
        redirectAttributes.addFlashAttribute("successMsg", "Line item approved.");
        return "redirect:/purchase/requisitions/view/" + requisitionId;
    }

    @PostMapping("/item/reject/{itemId}")
    public String rejectItem(@PathVariable String itemId,
                             @RequestParam String requisitionId,
                             @RequestParam String rejectionReason,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        if (rejectionReason == null || rejectionReason.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Rejection reason is required.");
            return "redirect:/purchase/requisitions/view/" + requisitionId;
        }
        requisitionService.rejectItem(itemId, rejectionReason, principal.getName());
        redirectAttributes.addFlashAttribute("successMsg", "Line item rejected.");
        return "redirect:/purchase/requisitions/view/" + requisitionId;
    }
}





