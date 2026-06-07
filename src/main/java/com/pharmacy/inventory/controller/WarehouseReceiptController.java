package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.WarehouseReceiptRequest;
import com.pharmacy.inventory.model.InspectionItem;
import com.pharmacy.inventory.model.InspectionReport;
import com.pharmacy.inventory.model.WarehouseReceipt;
import com.pharmacy.inventory.service.InspectionReportService;
import com.pharmacy.inventory.service.WarehouseReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/warehouse/receipts")
@RequiredArgsConstructor
public class WarehouseReceiptController {

    private final WarehouseReceiptService receiptService;
    private final InspectionReportService reportService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("receipts", receiptService.getAll());
        return "warehouse/receipt-list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam String reportId, Model model) {
        InspectionReport report = reportService.getById(reportId);
        
        WarehouseReceiptRequest request = new WarehouseReceiptRequest();
        request.setReportId(reportId);
        request.setReceiptDate(LocalDate.now());

        model.addAttribute("receiptRequest", request);
        model.addAttribute("report", report);
        return "warehouse/receipt-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute WarehouseReceiptRequest receiptRequest,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        try {
            WarehouseReceipt saved = receiptService.create(receiptRequest, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Đã lập phiếu nhập kho thành công. Tổng tiền: " + saved.getTotalAmount() + " đ");
            return "redirect:/warehouse/receipts";
        } catch (Exception e) {
            e.printStackTrace(); // Log ra console để debug
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi lập phiếu: " + e.getMessage());
            // Quay lại trang tạo form với reportId cũ để người dùng không phải nhập lại từ đầu (nếu có thể)
            return "redirect:/warehouse/receipts/create?reportId=" + receiptRequest.getReportId();
        }
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        WarehouseReceipt receipt = receiptService.getById(id);
        model.addAttribute("r", receipt);
        return "warehouse/receipt-view";
    }
}





