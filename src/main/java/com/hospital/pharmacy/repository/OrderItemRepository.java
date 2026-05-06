package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.entity.OrderItem;
import com.hospital.pharmacy.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByPurchaseOrder(PurchaseOrder purchaseOrder);
}