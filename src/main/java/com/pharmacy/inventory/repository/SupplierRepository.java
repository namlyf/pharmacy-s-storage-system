package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, String> {
    List<Supplier> findByIsActiveTrue();
    boolean existsByLicenseNumber(String licenseNumber);
    List<Supplier> findBySupplierNameContainingIgnoreCase(String keyword);
}