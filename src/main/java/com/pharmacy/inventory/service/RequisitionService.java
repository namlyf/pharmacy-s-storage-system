package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.RequisitionItemRequest;
import com.pharmacy.inventory.dto.request.RequisitionRequest;
import com.pharmacy.inventory.enums.ApprovalStatus;
import com.pharmacy.inventory.enums.RequisitionStatus;
import com.pharmacy.inventory.model.*;
import com.pharmacy.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequisitionService {

    private final DrugRequisitionRepository requisitionRepository;
    private final DrugRequisitionItemRepository itemRepository;
    private final DrugRepository drugRepository;
    private final SupplierRepository supplierRepository;
    private final AccountRepository accountRepository;

    public List<DrugRequisition> getAll() {
        return requisitionRepository.findAll();
    }

    public DrugRequisition getById(String id) {
        return requisitionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu dự trù: " + id));
    }

    @Transactional
    public DrugRequisition create(RequisitionRequest request, String username) {
        Account creator = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        DrugRequisition requisition = DrugRequisition.builder()
            .createdBy(creator)
            .createdDate(LocalDate.now())
            .overallStatus(RequisitionStatus.PENDING)
            .notes(request.getNotes())
            .build();

        for (RequisitionItemRequest itemReq : request.getItems()) {
            Drug drug = drugRepository.findById(itemReq.getDrugId())
                .orElseThrow(() -> new RuntimeException("Drug not found: " + itemReq.getDrugId()));

            Supplier supplier = null;
            if (itemReq.getPreferredsupplierId() != null && !itemReq.getPreferredsupplierId().isEmpty()) {
                supplier = supplierRepository.findById(itemReq.getPreferredsupplierId()).orElse(null);
            }

            DrugRequisitionItem item = DrugRequisitionItem.builder()
                .drug(drug)
                .quantity(itemReq.getQuantity())
                .unit(itemReq.getUnit())
                .packagingSpec(itemReq.getPackagingSpec())
                .preferredSupplier(supplier)
                .reason(itemReq.getReason())
                .approvalStatus(ApprovalStatus.PENDING)
                .build();

            requisition.addItem(item);
        }

        return requisitionRepository.save(requisition);
    }

    @Transactional
    public void approveItem(String itemId, String managerUsername) {
        updateItemStatus(itemId, ApprovalStatus.APPROVED, null, managerUsername);
    }

    @Transactional
    public void rejectItem(String itemId, String reason, String managerUsername) {
        updateItemStatus(itemId, ApprovalStatus.REJECTED, reason, managerUsername);
    }

    private void updateItemStatus(String itemId, ApprovalStatus status, String rejectionReason, String managerUsername) {
        DrugRequisitionItem item = itemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));

        Account manager = accountRepository.findByUsername(managerUsername)
            .orElseThrow(() -> new RuntimeException("User not found: " + managerUsername));

        item.setApprovalStatus(status);
        item.setRejectionReason(rejectionReason);
        item.setApprovedBy(manager);
        item.setApprovedAt(LocalDateTime.now());

        // --- Tự động chuyển trạng thái thuốc sang ACTIVE khi được duyệt dự trù ---
        if (status == ApprovalStatus.APPROVED) {
            Drug drug = item.getDrug();
            if (drug.getStatus() == com.pharmacy.inventory.enums.DrugStatus.DRAFT) {
                drug.setStatus(com.pharmacy.inventory.enums.DrugStatus.ACTIVE);
                drugRepository.save(drug);
            }
        }

        itemRepository.save(item);

        // Update overall status of the requisition
        updateOverallStatus(item.getRequisition().getRequisitionId());
    }

    private void updateOverallStatus(String requisitionId) {
        DrugRequisition requisition = getById(requisitionId);
        List<DrugRequisitionItem> items = requisition.getItems();

        boolean allApproved = true;
        boolean allRejected = true;
        boolean anyPending = false;

        for (DrugRequisitionItem item : items) {
            if (item.getApprovalStatus() == ApprovalStatus.PENDING) {
                anyPending = true;
            }
            if (item.getApprovalStatus() != ApprovalStatus.APPROVED) {
                allApproved = false;
            }
            if (item.getApprovalStatus() != ApprovalStatus.REJECTED) {
                allRejected = false;
            }
        }

        if (anyPending) {
            requisition.setOverallStatus(RequisitionStatus.PENDING);
        } else if (allApproved) {
            requisition.setOverallStatus(RequisitionStatus.APPROVED);
        } else if (allRejected) {
            requisition.setOverallStatus(RequisitionStatus.REJECTED);
        } else {
            requisition.setOverallStatus(RequisitionStatus.PARTIALLY_APPROVED);
        }

        requisitionRepository.save(requisition);
    }
}





