package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
