package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.DrugBatchRequest;
import com.pharmacy.inventory.enums.OrderStatus;
import com.pharmacy.inventory.model.*;
import com.pharmacy.inventory.repository.AccountRepository;
import com.pharmacy.inventory.repository.DrugBatchRepository;
import com.pharmacy.inventory.repository.DrugRepository;
import com.pharmacy.inventory.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugBatchService {

    private final DrugBatchRepository batchRepository;
    private final OrderRepository orderRepository;
    private final DrugRepository drugRepository;
    private final AccountRepository accountRepository;

    public List<DrugBatch> getAll() {
        return batchRepository.findAll();
    }

    public List<DrugBatch> getByOrderId(String orderId) {
        return batchRepository.findByOrder_OrderId(orderId);
    }

    public DrugBatch getById(String id) {
        return batchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Batch not found: " + id));
    }

    @Transactional
    public DrugBatch create(DrugBatchRequest request, String username) {
        Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new RuntimeException("Order not found"));

        Drug drug = drugRepository.findById(request.getDrugId())
            .orElseThrow(() -> new RuntimeException("Drug not found"));

        Account creator = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        DrugBatch batch = DrugBatch.builder()
            .order(order)
            .drug(drug)
            .lotNumber(request.getLotNumber())
            .manufactureDate(request.getManufactureDate())
            .expirationDate(request.getExpirationDate())
            .quantityReceived(request.getQuantityReceived())
            .storageCondition(request.getStorageCondition())
            .invoiceNumber(request.getInvoiceNumber())
            .invoiceDate(request.getInvoiceDate())
            .createdBy(creator)
            .createdAt(LocalDateTime.now())
            .build();

        DrugBatch savedBatch = batchRepository.save(batch);

        // Cập nhật trạng thái Order: Phân biệt RECEIVED và PARTIALLY_RECEIVED
        java.util.List<DrugBatch> registeredBatches = batchRepository.findByOrder_OrderId(order.getOrderId());
        long registeredDrugsCount = registeredBatches.stream()
                .map(b -> b.getDrug().getDrugId())
                .distinct()
                .count();
        
        long totalDrugsInOrder = order.getItems().size();

        if (registeredDrugsCount >= totalDrugsInOrder) {
            order.setStatus(OrderStatus.RECEIVED);
        } else {
            order.setStatus(OrderStatus.PARTIALLY_RECEIVED);
        }
        orderRepository.save(order);

        return savedBatch;
    }
}





