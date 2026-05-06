package com.hospital.pharmacy.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.pharmacy.entity.Account;
import com.hospital.pharmacy.entity.DrugRequisition;
import com.hospital.pharmacy.repository.AccountRepository;
import com.hospital.pharmacy.service.DrugRequisitionService;

@Controller
@RequestMapping("/requisitions")
public class DrugRequisitionController {

    private final DrugRequisitionService requisitionService;
    private final AccountRepository accountRepository;

    public DrugRequisitionController(DrugRequisitionService requisitionService,
                                     AccountRepository accountRepository) {
        this.requisitionService = requisitionService;
        this.accountRepository = accountRepository;
    }

    // Danh sách dự trù
    @GetMapping
    public String list(Model model, Authentication auth) {
        Account account = accountRepository.findByUsername(auth.getName()).orElseThrow();

        if (account.getRole() == Account.Role.MANAGER) {
            // Manager xem tất cả
            model.addAttribute("requisitions", requisitionService.getAll());
        } else {
            // Dược sĩ chỉ xem của mình
            model.addAttribute("requisitions", requisitionService.getByCreatedBy(account));
        }

        model.addAttribute("role", account.getRole().name());
        return "requisition/list";
    }

    // Form tạo mới
    @GetMapping("/create")
    public String createForm(Model model, Authentication auth) {
        Account account = accountRepository.findByUsername(auth.getName()).orElseThrow();
        
        // MANAGER không được tạo dự trù
        if (account.getRole() == Account.Role.MANAGER) {
            return "redirect:/requisitions";
        }
        
        model.addAttribute("requisition", new DrugRequisition());
        return "requisition/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam String medicineName,
                        @RequestParam String dosage,
                        @RequestParam String unit,
                        @RequestParam Integer quantity,
                        @RequestParam String reason,
                        Authentication auth,
                        RedirectAttributes redirectAttributes) {

        Account account = accountRepository.findByUsername(auth.getName()).orElseThrow();

        // MANAGER không được tạo dự trù
        if (account.getRole() == Account.Role.MANAGER) {
            return "redirect:/requisitions";
        }

        DrugRequisition requisition = new DrugRequisition();
        requisition.setMedicineName(medicineName);
        requisition.setDosage(dosage);
        requisition.setUnit(unit);
        requisition.setQuantity(quantity);
        requisition.setReason(reason);
        requisition.setRequisitionDate(LocalDate.now());
        requisition.setStatus(DrugRequisition.Status.PENDING);
        requisition.setCreatedBy(account);

        requisitionService.save(requisition);
        redirectAttributes.addFlashAttribute("successMsg", "✅ Tạo phiếu dự trù thành công!");
        return "redirect:/requisitions";
    }

    // Duyệt dự trù (chỉ MANAGER)
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, 
                        RedirectAttributes redirectAttributes) {
        requisitionService.approve(id);
        redirectAttributes.addFlashAttribute("successMsg", "✅ Đã duyệt phiếu dự trù thành công!");
        return "redirect:/requisitions";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id,
                        RedirectAttributes redirectAttributes) {
        requisitionService.reject(id);
        redirectAttributes.addFlashAttribute("errorMsg", "❌ Đã từ chối phiếu dự trù!");
        return "redirect:/requisitions";
    }

    // Xóa dự trù
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        requisitionService.delete(id);
        return "redirect:/requisitions";
    }
}