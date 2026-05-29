package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.InspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InspectionReportRepository extends JpaRepository<InspectionReport, String> {
    Optional<InspectionReport> findByBatch_BatchID(String batchId);
    boolean existsByBatch_BatchID(String batchId);
}
