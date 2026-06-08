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
import java.time.LocalDateTime;
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

        // Chỉ lấy các item đã APPROVED và CHƯA ĐƯỢC ĐẶT HÀNG
        List<DrugRequisitionItem> approvedItems = requisition.getItems().stream()
            .filter(item -> item.getApprovalStatus() == ApprovalStatus.APPROVED && !item.isOrdered())
            .toList();

        if (approvedItems.isEmpty()) {
            throw new RuntimeException("Không tìm thấy mặt hàng nào đã duyệt (hoặc tất cả đã được tạo đơn hàng trước đó).");
        }

        // Gom nhóm theo Supplier
        Map<Supplier, List<DrugRequisitionItem>> groupedBySupplier = approvedItems.stream()
            .filter(item -> item.getPreferredSupplier() != null)
            .collect(Collectors.groupingBy(DrugRequisitionItem::getPreferredSupplier));

        if (groupedBySupplier.isEmpty()) {
            String missingSupplierDrugs = approvedItems.stream()
                .filter(item -> item.getPreferredSupplier() == null)
                .map(item -> item.getDrug().getDrugName())
                .collect(Collectors.joining(", "));
            throw new RuntimeException("Các mặt hàng được duyệt (" + missingSupplierDrugs + ") chưa được chỉ định Nhà cung cấp dự kiến. Vui lòng kiểm tra lại bản dự trù.");
        }

        List<Order> createdOrders = new ArrayList<>();

        for (Map.Entry<Supplier, List<DrugRequisitionItem>> entry : groupedBySupplier.entrySet()) {
            Supplier supplier = entry.getKey();
            List<DrugRequisitionItem> items = entry.getValue();

            Order order = Order.builder()
                .requisition(requisition)
                .supplier(supplier)
                .createdBy(creator)
                .orderDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.DRAFT)
                .notes("Được tạo tự động từ bản dự trù: " + requisitionId)
                .build();

            for (DrugRequisitionItem reqItem : items) {
                OrderItem orderItem = OrderItem.builder()
                    .drug(reqItem.getDrug())
                    .quantity(reqItem.getQuantity())
                    .unit(reqItem.getUnit())
                    .packagingSpec(reqItem.getPackagingSpec())
                    .build();
                order.addItem(orderItem);
                
                // Đánh dấu đã đặt hàng
                reqItem.setOrdered(true);
            }

            createdOrders.add(orderRepository.save(order));
            requisitionItemRepository.saveAll(items);
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

    @Transactional
    public void deleteOrder(String orderId) {
        Order order = getById(orderId);
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new RuntimeException("Chỉ có thể xóa đơn hàng ở trạng thái NHÁP (DRAFT).");
        }
        
        // --- RESET TRẠNG THÁI CHO BẢN DỰ TRÙ (Cách tiếp cận chính xác hơn) ---
        if (order.getRequisition() != null) {
            // Lấy danh sách ID các thuốc có trong đơn hàng này
            List<String> drugIdsInOrder = order.getItems().stream()
                .map(item -> item.getDrug().getDrugId())
                .toList();

            // Tìm và reset các item trong dự trù tương ứng
            List<DrugRequisitionItem> itemsToReset = order.getRequisition().getItems().stream()
                .filter(item -> drugIdsInOrder.contains(item.getDrug().getDrugId()) && item.isOrdered())
                .toList();
            
            for (DrugRequisitionItem item : itemsToReset) {
                item.setOrdered(false);
            }
            requisitionItemRepository.saveAll(itemsToReset);
        }
        
        orderRepository.delete(order);
    }
}





