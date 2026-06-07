package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.model.Order;
import com.pharmacy.inventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/purchase/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final com.pharmacy.inventory.repository.DrugBatchRepository batchRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAll());
        return "procurement/order-list";
    }

    @PostMapping("/generate/{requisitionId}")
    public String generate(@PathVariable String requisitionId,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        try {
            var orders = orderService.generateDraftOrdersFromRequisition(requisitionId, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", 
                "Đã tạo thành công " + orders.size() + " đơn đặt hàng nháp từ bản dự trù.");
            return "redirect:/purchase/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi tạo đơn hàng: " + e.getMessage());
            return "redirect:/purchase/requisitions/view/" + requisitionId;
        }
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        
        // Tìm danh sách thuốc đã được đăng ký lô hàng cho đơn này
        java.util.List<String> registeredDrugIds = batchRepository.findByOrder_OrderId(id).stream()
                .map(batch -> batch.getDrug().getDrugId())
                .toList();
        model.addAttribute("registeredDrugIds", registeredDrugIds);
        
        return "procurement/order-view";
    }

    @PostMapping("/send/{id}")
    public String sendOrder(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            orderService.markAsSent(id);
            redirectAttributes.addFlashAttribute("successMsg", "Order marked as SENT.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/purchase/orders/view/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("successMsg", "Đã xóa đơn hàng thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/purchase/orders";
    }
}





