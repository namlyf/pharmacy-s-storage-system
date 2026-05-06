package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.entity.DrugBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface DrugBatchRepository extends JpaRepository<DrugBatch, Long> {
    List<DrugBatch> findByExpirationDateBefore(LocalDate date);
}