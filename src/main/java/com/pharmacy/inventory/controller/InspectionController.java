package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.InspectionReportRequest;
import com.pharmacy.inventory.model.DrugBatch;
import com.pharmacy.inventory.model.InspectionReport;
import com.pharmacy.inventory.service.DrugBatchService;
import com.pharmacy.inventory.service.InspectionReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/inspection/reports")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionReportService reportService;
    private final DrugBatchService batchService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reports", reportService.getAll());
        return "inspection/report-list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam String batchId, Model model) {
        DrugBatch batch = batchService.getById(batchId);
        
        InspectionReportRequest request = new InspectionReportRequest();
        request.setBatchId(batchId);
        request.setInspectionDate(LocalDate.now());
        request.setStorageConditionMatch(true);
        request.setInvoiceValid(true);

        model.addAttribute("reportRequest", request);
        model.addAttribute("batch", batch);
        return "inspection/report-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute InspectionReportRequest reportRequest,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        try {
            reportService.create(reportRequest, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Inspection report submitted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/inspection/reports/create?batchId=" + reportRequest.getBatchId();
        }
        return "redirect:/warehouse/batches";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        InspectionReport report = reportService.getById(id);
        model.addAttribute("report", report);
        return "inspection/report-view";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable String id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            reportService.approve(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Inspection report APPROVED.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/inspection/reports/view/" + id;
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable String id, 
                         @RequestParam String reason, 
                         Principal principal, 
                         RedirectAttributes redirectAttributes) {
        try {
            reportService.reject(id, reason, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Inspection report REJECTED.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/inspection/reports/view/" + id;
    }
}
