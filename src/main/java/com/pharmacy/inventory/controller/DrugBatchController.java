package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.DrugBatchRequest;
import com.pharmacy.inventory.model.Order;
import com.pharmacy.inventory.model.OrderItem;
import com.pharmacy.inventory.service.DrugBatchService;
import com.pharmacy.inventory.service.OrderService;
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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("batches", batchService.getAll());
        return "warehouse/batch-list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam String orderId, 
                             @RequestParam String drugId, 
                             Model model) {
        Order order = orderService.getById(orderId);
        OrderItem item = order.getItems().stream()
            .filter(i -> i.getDrug().getDrugID().equals(drugId))
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
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        try {
            batchService.create(batchRequest, principal.getName());
            redirectAttributes.addFlashAttribute("successMsg", "Drug batch registered successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/warehouse/batches/create?orderId=" + batchRequest.getOrderId() + "&drugId=" + batchRequest.getDrugId();
        }
        return "redirect:/purchase/orders/view/" + batchRequest.getOrderId();
    }
}
