package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.ReceivingLogbook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReceivingLogbookRepository extends JpaRepository<ReceivingLogbook, String> {
    List<ReceivingLogbook> findAllByOrderByLogDateDesc();
}
