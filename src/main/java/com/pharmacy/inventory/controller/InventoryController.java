package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.dto.request.InventoryStockRequest;
import com.pharmacy.inventory.model.InventoryStock;
import com.pharmacy.inventory.service.DrugService;
import com.pharmacy.inventory.service.InventoryStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryStockService stockService;
    private final DrugService drugService;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<InventoryStock> allStocks = stockService.getAll();
        List<InventoryStock> lowStocks = stockService.getLowStockItems();

        model.addAttribute("allStocks", allStocks);
        model.addAttribute("lowStocks", lowStocks);
        model.addAttribute("lowStockCount", lowStocks.size());
        model.addAttribute("totalDrugs", allStocks.size());
        
        // Count by Category
        long medicineCount = allStocks.stream().filter(s -> s.getDrug().getCategory().name().equals("MEDICINE")).count();
        long supplementCount = allStocks.stream().filter(s -> s.getDrug().getCategory().name().equals("SUPPLEMENT")).count();
        long cosmeticCount = allStocks.stream().filter(s -> s.getDrug().getCategory().name().equals("COSMETIC")).count();
        
        model.addAttribute("medicineCount", medicineCount);
        model.addAttribute("supplementCount", supplementCount);
        model.addAttribute("cosmeticCount", cosmeticCount);

        return "inventory/dashboard";
    }

    // Danh sách stock
    @GetMapping({"/stocks", "/stock"})
    public String stockList(Model model, 
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) Boolean lowStock) {
        List<InventoryStock> stocks = stockService.getAll();
        
        if (category != null && !category.isEmpty()) {
            stocks = stocks.stream().filter(s -> s.getDrug().getCategory().name().equalsIgnoreCase(category)).toList();
        }
        
        if (lowStock != null && lowStock) {
            stocks = stocks.stream().filter(s -> s.getCurrentQuantity() <= s.getMinimumThreshold()).toList();
        }

        model.addAttribute("stocks", stocks);
        model.addAttribute("lowStockCount", stockService.getLowStockItems().size());
        return "inventory/stock-list";
    }

    // Form thêm stock mới
    @GetMapping("/stocks/create")
    public String createForm(Model model) {
        model.addAttribute("stockRequest", new InventoryStockRequest());
        model.addAttribute("drugs", drugService.getAllActive());
        return "inventory/stock-form";
    }

    @PostMapping("/stocks/create")
    public String create(@Valid @ModelAttribute InventoryStockRequest stockRequest,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("drugs", drugService.getAllActive());
            return "inventory/stock-form";
        }
        try {
            stockService.create(stockRequest);
            redirectAttributes.addFlashAttribute("successMsg",
                "Đã khởi tạo thông tin kho thành công.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/inventory/stocks";
    }

    // Form edit stock
    @GetMapping("/stocks/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        InventoryStock stock = stockService.getById(id);

        InventoryStockRequest request = new InventoryStockRequest();
        request.setDrugId(stock.getDrug().getDrugId());
        request.setCurrentQuantity(stock.getCurrentQuantity());
        request.setMinimumThreshold(stock.getMinimumThreshold());
        request.setStorageLocation(stock.getStorageLocation());

        model.addAttribute("stockRequest", request);
        model.addAttribute("stockId", id);
        model.addAttribute("drugName", stock.getDrug().getDrugName());
        return "inventory/stock-form";
    }

    @PostMapping("/stocks/edit/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute InventoryStockRequest stockRequest,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            InventoryStock stock = stockService.getById(id);
            model.addAttribute("stockId", id);
            model.addAttribute("drugName", stock.getDrug().getDrugName());
            return "inventory/stock-form";
        }
        stockService.update(id, stockRequest);
        redirectAttributes.addFlashAttribute("successMsg",
            "Cập nhật tồn kho thành công.");
        return "redirect:/inventory/stocks";
    }
}





