package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.InspectionReportRequest;
import com.pharmacy.inventory.enums.ApprovalStatus;
import com.pharmacy.inventory.model.*;
import com.pharmacy.inventory.repository.AccountRepository;
import com.pharmacy.inventory.repository.DrugBatchRepository;
import com.pharmacy.inventory.repository.InspectionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionReportService {

    private final InspectionReportRepository reportRepository;
    private final DrugBatchRepository batchRepository;
    private final AccountRepository accountRepository;

    public List<InspectionReport> getAll() {
        return reportRepository.findAll();
    }

    public InspectionReport getById(String id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy biên bản kiểm nhập: " + id));
    }

    public InspectionReport getByBatchId(String batchId) {
        return reportRepository.findByBatch_BatchID(batchId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy biên bản cho lô hàng: " + batchId));
    }

    @Transactional
    public InspectionReport create(InspectionReportRequest request, String username) {
        if (reportRepository.existsByBatch_BatchID(request.getBatchId())) {
            throw new RuntimeException("Biên bản kiểm nhập đã tồn tại cho lô hàng này.");
        }

        DrugBatch batch = batchRepository.findById(request.getBatchId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy lô hàng"));

        Account inspector = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Auto-validations
        boolean sdkValid = validateSdk(batch.getDrug().getRegistrationNumber());
        boolean shelfLifeValid = validateShelfLife(batch.getExpirationDate(), request.getInspectionDate());

        InspectionReport report = InspectionReport.builder()
            .batch(batch)
            .inspectedBy(inspector)
            .inspectionDate(request.getInspectionDate())
            .status(ApprovalStatus.PENDING)
            .sdkValid(sdkValid)
            .shelfLifeValid(shelfLifeValid)
            .invoiceValid(request.isInvoiceValid())
            .visualQualityResult(request.getVisualQualityResult())
            .storageConditionMatch(request.isStorageConditionMatch())
            .storageTemperature(request.getStorageTemperature())
            .vatPrice(request.getVatPrice())
            .build();

        return reportRepository.save(report);
    }

    @Transactional
    public void approve(String id, String managerUsername) {
        InspectionReport report = getById(id);
        Account manager = accountRepository.findByUsername(managerUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

        report.setStatus(ApprovalStatus.APPROVED);
        report.setApprovedBy(manager);
        report.setApprovedAt(LocalDateTime.now());
        reportRepository.save(report);
    }

    @Transactional
    public void reject(String id, String reason, String managerUsername) {
        InspectionReport report = getById(id);
        Account manager = accountRepository.findByUsername(managerUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

        report.setStatus(ApprovalStatus.REJECTED);
        report.setRejectionReason(reason);
        report.setApprovedBy(manager);
        report.setApprovedAt(LocalDateTime.now());
        reportRepository.save(report);
    }

    private boolean validateSdk(String sdk) {
        if (sdk == null) return false;
        String s = sdk.toUpperCase();
        return s.startsWith("VN") || s.startsWith("VD") || 
               s.contains("/YT-") || s.contains("/ATTP-") || 
               s.contains("/MP-QLD") || s.contains("/CBMP-QLD");
    }

    private boolean validateShelfLife(LocalDate expirationDate, LocalDate inspectionDate) {
        if (expirationDate == null || inspectionDate == null) return false;
        long daysRemaining = ChronoUnit.DAYS.between(inspectionDate, expirationDate);
        return daysRemaining > 365;
    }
}
