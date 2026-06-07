package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface InventoryStockRepository extends JpaRepository<InventoryStock, String> {

    Optional<InventoryStock> findByDrug_DrugId(String drugId);

    boolean existsByDrug_DrugId(String drugId);

    // Lấy danh sách đang low stock
    @Query("SELECT s FROM InventoryStock s WHERE s.currentQuantity <= s.minimumThreshold")
    List<InventoryStock> findLowStockItems();
}




