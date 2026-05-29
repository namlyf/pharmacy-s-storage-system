package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.Drug;
import com.pharmacy.inventory.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DrugRepository extends JpaRepository<Drug, String> {
    List<Drug> findByIsActiveTrue();
    List<Drug> findByCategoryAndIsActiveTrue(Category category);
    Optional<Drug> findByRegistrationNumber(String registrationNumber);
    boolean existsByRegistrationNumber(String registrationNumber);
    List<Drug> findByDrugNameContainingIgnoreCase(String keyword);
}