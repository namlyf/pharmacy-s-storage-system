package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.InventoryStockRequest;
import com.pharmacy.inventory.model.Drug;
import com.pharmacy.inventory.model.InventoryStock;
import com.pharmacy.inventory.repository.DrugRepository;
import com.pharmacy.inventory.repository.InventoryStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryStockService {

    private final InventoryStockRepository stockRepository;
    private final DrugRepository drugRepository;

    public List<InventoryStock> getAll() {
        return stockRepository.findAll();
    }

    public List<InventoryStock> getLowStockItems() {
        return stockRepository.findLowStockItems();
    }

    public InventoryStock getById(String id) {
        return stockRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stock not found: " + id));
    }

    public InventoryStock getByDrugId(String drugId) {
        return stockRepository.findByDrug_DrugID(drugId)
            .orElseThrow(() -> new RuntimeException("Stock not found for drug: " + drugId));
    }

    public InventoryStock create(InventoryStockRequest request) {
        if (stockRepository.existsByDrug_DrugID(request.getDrugId())) {
            throw new RuntimeException("Stock entry already exists for this drug.");
        }

        Drug drug = drugRepository.findById(request.getDrugId())
            .orElseThrow(() -> new RuntimeException("Drug not found."));

        InventoryStock stock = InventoryStock.builder()
            .drug(drug)
            .currentQuantity(request.getCurrentQuantity())
            .minimumThreshold(request.getMinimumThreshold())
            .storageLocation(request.getStorageLocation())
            .build();

        stock.setLastUpdated(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    public InventoryStock update(String id, InventoryStockRequest request) {
        InventoryStock stock = getById(id);

        stock.setCurrentQuantity(request.getCurrentQuantity());
        stock.setMinimumThreshold(request.getMinimumThreshold());
        stock.setStorageLocation(request.getStorageLocation());
        stock.setLastUpdated(LocalDateTime.now());

        return stockRepository.save(stock);
    }

    // Gọi method này khi BM.04 được approve → cộng thêm số lượng vào kho
    public void increaseStock(String drugId, int quantity) {
        InventoryStock stock = stockRepository.findByDrug_DrugID(drugId)
            .orElseThrow(() -> new RuntimeException("Stock not found for drug: " + drugId));

        stock.setCurrentQuantity(stock.getCurrentQuantity() + quantity);
        stock.setLastUpdated(LocalDateTime.now());
        stockRepository.save(stock);
    }

    public long countLowStock() {
        return stockRepository.findLowStockItems().size();
    }

    // Gọi khi tạo Drug mới → tự tạo stock entry luôn
    public void initializeStock(Drug drug, Integer minimumThreshold) {
        if (stockRepository.existsByDrug_DrugID(drug.getDrugID())) {
            return; // Đã có rồi thì bỏ qua
        }
        InventoryStock stock = new InventoryStock();
        stock.setDrug(drug);
        stock.setCurrentQuantity(0);
        stock.setMinimumThreshold(minimumThreshold != null ? minimumThreshold : 0);
        stockRepository.save(stock);
    }

    // Gọi khi update Drug → cập nhật threshold
    public void updateThreshold(String drugId, Integer minimumThreshold) {
        stockRepository.findByDrug_DrugID(drugId).ifPresent(stock -> {
            stock.setMinimumThreshold(minimumThreshold != null ? minimumThreshold : 0);
            stockRepository.save(stock);
        });
    }
}
