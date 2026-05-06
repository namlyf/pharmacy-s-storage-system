package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.entity.InspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InspectionReportRepository extends JpaRepository<InspectionReport, Long> {
    List<InspectionReport> findByStatus(InspectionReport.Status status);
}