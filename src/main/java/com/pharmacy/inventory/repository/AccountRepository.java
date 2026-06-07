package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);
}




