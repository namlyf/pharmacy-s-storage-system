package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.InspectionItemRequest;
import com.pharmacy.inventory.dto.request.InspectionReportRequest;
import com.pharmacy.inventory.model.DrugBatch;
import com.pharmacy.inventory.model.InspectionReport;
import com.pharmacy.inventory.service.DrugBatchService;
import com.pharmacy.inventory.service.InspectionReportService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/working-list")
    public String workingList(HttpSession session, Model model) {
        List<InspectionItemRequest> workingList = getOrCreateWorkingList(session);
        List<DrugBatch> batches = new ArrayList<>();
        for (InspectionItemRequest item : workingList) {
            batches.add(batchService.getById(item.getBatchId()));
        }
        model.addAttribute("workingBatches", batches);
        model.addAttribute("workingList", workingList);
        return "inspection/working-list";
    }

    @PostMapping("/add-to-list")
    public String addToList(@ModelAttribute InspectionItemRequest itemRequest,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        List<InspectionItemRequest> workingList = getOrCreateWorkingList(session);
        
        // Check if already in list
        boolean exists = workingList.stream()
                .anyMatch(i -> i.getBatchId().equals(itemRequest.getBatchId()));
        
        if (!exists) {
            workingList.add(itemRequest);
            redirectAttributes.addFlashAttribute("successMsg", "Đã thêm vào danh sách chờ kiểm nhập.");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Lô hàng này đã có trong danh sách chờ.");
        }
        
        return "redirect:/warehouse/batches";
    }

    @PostMapping("/remove-from-list/{batchId}")
    public String removeFromList(@PathVariable String batchId, HttpSession session) {
        List<InspectionItemRequest> workingList = getOrCreateWorkingList(session);
        workingList.removeIf(i -> i.getBatchId().equals(batchId));
        return "redirect:/inspection/reports/working-list";
    }

    @PostMapping("/submit")
    public String submitReport(HttpSession session, Principal principal, RedirectAttributes redirectAttributes) {
        List<InspectionItemRequest> workingList = getOrCreateWorkingList(session);
        if (workingList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Danh sách chờ trống.");
            return "redirect:/inspection/reports/working-list";
        }

        try {
            InspectionReportRequest request = new InspectionReportRequest();
            request.setInspectionDate(LocalDate.now());
            request.setItems(new ArrayList<>(workingList));
            
            reportService.create(request, principal.getName());
            
            // Clear the working list
            session.removeAttribute("workingList");
            
            redirectAttributes.addFlashAttribute("successMsg", "Đã gửi biên bản tổng cho quản lý phê duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/inspection/reports";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        InspectionReport report = reportService.getById(id);
        model.addAttribute("report", report);
        return "inspection/report-view";
    }

    @SuppressWarnings("unchecked")
    private List<InspectionItemRequest> getOrCreateWorkingList(HttpSession session) {
        List<InspectionItemRequest> list = (List<InspectionItemRequest>) session.getAttribute("workingList");
        if (list == null) {
            list = new ArrayList<>();
            session.setAttribute("workingList", list);
        }
        return list;
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable String id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            reportService.approve(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Biên bản đã được PHÊ DUYỆT.");
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
            redirectAttributes.addFlashAttribute("successMsg", "Biên bản đã bị TỪ CHỐI.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/inspection/reports/view/" + id;
    }
}





