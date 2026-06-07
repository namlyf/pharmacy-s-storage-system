package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.InspectionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionItemRepository extends JpaRepository<InspectionItem, String> {
    boolean existsByBatch_BatchId(String batchId);
}





