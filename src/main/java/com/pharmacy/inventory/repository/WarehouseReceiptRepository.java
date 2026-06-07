package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.WarehouseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseReceiptRepository extends JpaRepository<WarehouseReceipt, String> {
    boolean existsByReport_ReportId(String reportId);
}





