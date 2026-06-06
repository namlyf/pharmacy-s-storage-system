package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.WarehouseReceiptRequest;
import com.pharmacy.inventory.model.*;
import com.pharmacy.inventory.repository.AccountRepository;
import com.pharmacy.inventory.repository.InspectionReportRepository;
import com.pharmacy.inventory.repository.ReceivingLogbookRepository;
import com.pharmacy.inventory.repository.WarehouseReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseReceiptService {

    private final WarehouseReceiptRepository receiptRepository;
    private final InspectionReportRepository reportRepository;
    private final AccountRepository accountRepository;
    private final InventoryStockService stockService;
    private final ReceivingLogbookRepository logbookRepository;

    public List<WarehouseReceipt> getAll() {
        return receiptRepository.findAll();
    }

    public WarehouseReceipt getById(String id) {
        return receiptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Warehouse receipt not found: " + id));
    }

    @Transactional
    public WarehouseReceipt create(WarehouseReceiptRequest request, String username) {
        if (receiptRepository.existsByReport_ReportID(request.getReportId())) {
            throw new RuntimeException("Phiếu nhập kho đã tồn tại cho biên bản kiểm nhập này.");
        }

        InspectionReport report = reportRepository.findById(request.getReportId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy biên bản kiểm nhập"));

        if (!report.getStatus().name().equals("APPROVED")) {
            throw new RuntimeException("Biên bản kiểm nhập phải ở trạng thái ĐÃ PHÊ DUYỆT trước khi lập phiếu nhập kho.");
        }

        Account creator = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        BigDecimal importPrice = report.getVatPrice();
        BigDecimal profitRate = calculateProfitRate(importPrice);
        BigDecimal retailPrice = importPrice.multiply(BigDecimal.ONE.add(profitRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)));
        
        retailPrice = retailPrice.setScale(0, RoundingMode.CEILING);

        BigDecimal totalAmount = importPrice.multiply(new BigDecimal(report.getBatch().getQuantityReceived()));

        WarehouseReceipt receipt = WarehouseReceipt.builder()
            .report(report)
            .enteredBy(creator)
            .receiptDate(request.getReceiptDate())
            .barcode(request.getBarcode())
            .accountingCode(request.getAccountingCode())
            .importPrice(importPrice)
            .totalAmount(totalAmount)
            .bhytPrice(request.getBhytPrice())
            .profitRate(profitRate)
            .retailPrice(retailPrice)
            .createdAt(LocalDateTime.now())
            .build();

        WarehouseReceipt savedReceipt = receiptRepository.save(receipt);

        // Update Inventory Stock
        stockService.increaseStock(report.getBatch().getDrug().getDrugID(), report.getBatch().getQuantityReceived());

        // CREATE LOGBOOK ENTRY
        ReceivingLogbook log = ReceivingLogbook.builder()
            .receipt(savedReceipt)
            .logDate(savedReceipt.getReceiptDate())
            .invoiceNumber(report.getBatch().getInvoiceNumber())
            .supplier(report.getBatch().getOrder().getSupplier())
            .drug(report.getBatch().getDrug())
            .drugNameSnapshot(report.getBatch().getDrug().getDrugName())
            .concentrationSnapshot(report.getBatch().getDrug().getConcentration())
            .unit(report.getBatch().getDrug().getUnit())
            .quantity(report.getBatch().getQuantityReceived())
            .lotNumber(report.getBatch().getLotNumber())
            .expirationDate(report.getBatch().getExpirationDate())
            .unitPriceVAT(importPrice)
            .totalAmount(totalAmount)
            .enteredBy(creator)
            .createdAt(LocalDateTime.now())
            .build();
        
        logbookRepository.save(log);

        return savedReceipt;
    }

    private BigDecimal calculateProfitRate(BigDecimal price) {
        double p = price.doubleValue();
        if (p <= 1000) return new BigDecimal("15");
        if (p <= 5000) return new BigDecimal("10");
        if (p <= 100000) return new BigDecimal("7");
        if (p <= 1000000) return new BigDecimal("5");
        return new BigDecimal("2");
    }
}
