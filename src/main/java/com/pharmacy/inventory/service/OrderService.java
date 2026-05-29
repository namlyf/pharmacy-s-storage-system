package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.OrderRequest;
import com.pharmacy.inventory.enums.ApprovalStatus;
import com.pharmacy.inventory.enums.OrderStatus;
import com.pharmacy.inventory.model.*;
import com.pharmacy.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DrugRequisitionRepository requisitionRepository;
    private final DrugRequisitionItemRepository requisitionItemRepository;
    private final SupplierRepository supplierRepository;
    private final AccountRepository accountRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(String id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    /**
     * Tự động tạo các Đơn hàng nháp từ một Phiếu dự trù đã được phê duyệt.
     * Gom nhóm các dòng hàng đã APPROVED theo Nhà cung cấp.
     */
    @Transactional
    public List<Order> generateDraftOrdersFromRequisition(String requisitionId, String username) {
        DrugRequisition requisition = requisitionRepository.findById(requisitionId)
            .orElseThrow(() -> new RuntimeException("Requisition not found"));

        Account creator = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Chỉ lấy các item đã APPROVED và chưa có trong Order nào (giả định đơn giản cho MVP)
        List<DrugRequisitionItem> approvedItems = requisition.getItems().stream()
            .filter(item -> item.getApprovalStatus() == ApprovalStatus.APPROVED)
            .toList();

        if (approvedItems.isEmpty()) {
            throw new RuntimeException("No approved items found in this requisition.");
        }

        // Gom nhóm theo Supplier
        Map<Supplier, List<DrugRequisitionItem>> groupedBySupplier = approvedItems.stream()
            .filter(item -> item.getPreferredSupplier() != null)
            .collect(Collectors.groupingBy(DrugRequisitionItem::getPreferredSupplier));

        List<Order> createdOrders = new ArrayList<>();

        for (Map.Entry<Supplier, List<DrugRequisitionItem>> entry : groupedBySupplier.entrySet()) {
            Supplier supplier = entry.getKey();
            List<DrugRequisitionItem> items = entry.getValue();

            Order order = Order.builder()
                .requisition(requisition)
                .supplier(supplier)
                .createdBy(creator)
                .orderDate(LocalDate.now())
                .status(OrderStatus.DRAFT)
                .notes("Generated from Requisition: " + requisitionId)
                .build();

            for (DrugRequisitionItem reqItem : items) {
                OrderItem orderItem = OrderItem.builder()
                    .drug(reqItem.getDrug())
                    .quantity(reqItem.getQuantity())
                    .unit(reqItem.getUnit())
                    .packagingSpec(reqItem.getPackagingSpec())
                    .build();
                order.addItem(orderItem);
            }

            createdOrders.add(orderRepository.save(order));
        }

        return createdOrders;
    }

    @Transactional
    public void markAsSent(String orderId) {
        Order order = getById(orderId);
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new RuntimeException("Only DRAFT orders can be marked as SENT.");
        }
        order.setStatus(OrderStatus.SENT);
        orderRepository.save(order);
    }
}
