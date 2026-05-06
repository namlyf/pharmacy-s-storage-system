package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.entity.WarehouseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseReceiptRepository extends JpaRepository<WarehouseReceipt, Long> {
}