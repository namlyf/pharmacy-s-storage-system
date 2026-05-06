package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.entity.Account;
import com.hospital.pharmacy.entity.DrugRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DrugRequisitionRepository extends JpaRepository<DrugRequisition, Long> {
    List<DrugRequisition> findByCreatedBy(Account account);
    List<DrugRequisition> findByStatus(DrugRequisition.Status status);
}