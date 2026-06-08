package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.DrugBatchRequest;
import com.pharmacy.inventory.enums.ApprovalStatus;
import com.pharmacy.inventory.model.Order;
import com.pharmacy.inventory.model.OrderItem;
import com.pharmacy.inventory.repository.InspectionItemRepository;
import com.pharmacy.inventory.service.DrugBatchService;
import com.pharmacy.inventory.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/warehouse/batches")
@RequiredArgsConstructor
public class DrugBatchController {

    private final DrugBatchService batchService;
    private final OrderService orderService;
    private final InspectionItemRepository inspectionItemRepository;

    @GetMapping
    public String list(HttpSession session, Model model) {
        model.addAttribute("batches", batchService.getAll());

        // 1. Tạo bản đồ: Batch ID -> Report ID (Chỉ lấy các biên bản PENDING hoặc APPROVED)
        java.util.Map<String, String> batchToReportMap = new java.util.HashMap<>();
        inspectionItemRepository.findAll().stream()
                .filter(item -> item.getReport() != null && 
                                (item.getReport().getStatus() == ApprovalStatus.PENDING || 
                                 item.getReport().getStatus() == ApprovalStatus.APPROVED))
                .filter(item -> item.getBatch() != null)
                .forEach(item -> batchToReportMap.put(item.getBatch().getBatchId(), item.getReport().getReportId()));
        
        model.addAttribute("batchToReportMap", batchToReportMap);

        // 2. Lấy danh sách ID và Đối tượng đầy đủ của các lô đang chờ gửi (session)
        java.util.List<com.pharmacy.inventory.dto.request.InspectionItemRequest> workingList = 
            (java.util.List<com.pharmacy.inventory.dto.request.InspectionItemRequest>) session.getAttribute("workingList");
        
        java.util.List<String> workingBatchIds = new java.util.ArrayList<>();
        java.util.List<com.pharmacy.inventory.model.DrugBatch> workingBatches = new java.util.ArrayList<>();
        
        if (workingList != null) {
            for (com.pharmacy.inventory.dto.request.InspectionItemRequest item : workingList) {
                workingBatchIds.add(item.getBatchId());
                workingBatches.add(batchService.getById(item.getBatchId()));
            }
        }
        
        model.addAttribute("workingBatchIds", workingBatchIds);
        model.addAttribute("workingBatches", workingBatches);

        return "warehouse/batch-list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam String orderId, 
                             @RequestParam String drugId, 
                             Model model) {
        Order order = orderService.getById(orderId);
        OrderItem item = order.getItems().stream()
            .filter(i -> i.getDrug().getDrugId().equals(drugId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Drug not found in order"));

        DrugBatchRequest request = new DrugBatchRequest();
        request.setOrderId(orderId);
        request.setDrugId(drugId);
        request.setQuantityReceived(item.getQuantity());

        model.addAttribute("batchRequest", request);
        model.addAttribute("order", order);
        model.addAttribute("drug", item.getDrug());
        return "warehouse/batch-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute DrugBatchRequest batchRequest,
                         HttpSession session,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        try {
            var batch = batchService.create(batchRequest, principal.getName());
            
            // TỰ ĐỘNG THÊM VÀO WORKING LIST (Danh sách chờ kiểm nhập)
            java.util.List<com.pharmacy.inventory.dto.request.InspectionItemRequest> workingList = 
                (java.util.List<com.pharmacy.inventory.dto.request.InspectionItemRequest>) session.getAttribute("workingList");
            if (workingList == null) {
                workingList = new java.util.ArrayList<>();
                session.setAttribute("workingList", workingList);
            }
            
            com.pharmacy.inventory.dto.request.InspectionItemRequest itemRequest = new com.pharmacy.inventory.dto.request.InspectionItemRequest();
            itemRequest.setBatchId(batch.getBatchId());
            itemRequest.setVatPrice(batchRequest.getVatPrice());
            itemRequest.setVisualQualityResult(batchRequest.getVisualQualityResult());
            itemRequest.setInvoiceValid(batchRequest.isInvoiceValid());
            itemRequest.setStorageConditionMatch(batchRequest.isStorageConditionMatch());
            
            workingList.add(itemRequest);
            
            redirectAttributes.addFlashAttribute("successMsg", "Đã đăng ký lô hàng và thêm vào danh sách chờ kiểm nhập thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/warehouse/batches/create?orderId=" + batchRequest.getOrderId() + "&drugId=" + batchRequest.getDrugId();
        }
        return "redirect:/purchase/orders/view/" + batchRequest.getOrderId();
    }
}





