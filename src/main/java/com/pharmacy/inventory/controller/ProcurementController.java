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
        model.addAttribute("drugs", drugService.getAll());
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
    public String view(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            DrugRequisition requisition = requisitionService.getById(id);
            model.addAttribute("req", requisition);
            return "procurement/requisition-view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy phiếu dự trù: " + e.getMessage());
            return "redirect:/purchase/requisitions";
        }
    }

    @PostMapping("/item/approve/{itemId}")
    public String approveItem(@PathVariable String itemId,
                              @RequestParam(required = false) String requisitionId,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        if (requisitionId == null || requisitionId.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Thiếu mã phiếu dự trù.");
            return "redirect:/purchase/requisitions";
        }
        try {
            requisitionService.approveItem(itemId, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Dòng hàng đã được phê duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi phê duyệt: " + e.getMessage());
        }
        return "redirect:/purchase/requisitions/view/" + requisitionId;
    }

    @PostMapping("/item/reject/{itemId}")
    public String rejectItem(@PathVariable String itemId,
                             @RequestParam(required = false) String requisitionId,
                             @RequestParam(required = false) String rejectionReason,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        if (requisitionId == null || requisitionId.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Thiếu mã phiếu dự trù.");
            return "redirect:/purchase/requisitions";
        }
        try {
            if (rejectionReason == null || rejectionReason.isBlank()) {
                redirectAttributes.addFlashAttribute("errorMsg", "Lý do từ chối là bắt buộc.");
                return "redirect:/purchase/requisitions/view/" + requisitionId;
            }
            requisitionService.rejectItem(itemId, rejectionReason, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Dòng hàng đã bị từ chối.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi từ chối: " + e.getMessage());
        }
        return "redirect:/purchase/requisitions/view/" + requisitionId;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            requisitionService.deleteRequisition(id);
            redirectAttributes.addFlashAttribute("successMsg", "Đã xóa bản dự trù thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/purchase/requisitions";
    }
}





