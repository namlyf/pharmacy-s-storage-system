package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.DrugBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DrugBatchRepository extends JpaRepository<DrugBatch, String> {
    List<DrugBatch> findByOrder_OrderID(String orderId);
}
