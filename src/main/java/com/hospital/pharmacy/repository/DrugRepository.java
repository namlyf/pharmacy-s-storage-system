package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DrugRepository extends JpaRepository<Drug, Long> {
    Optional<Drug> findByRegistrationNumber(String registrationNumber);
    List<Drug> findByCategory(Drug.Category category);
    List<Drug> findByDrugNameContainingIgnoreCase(String name);
}