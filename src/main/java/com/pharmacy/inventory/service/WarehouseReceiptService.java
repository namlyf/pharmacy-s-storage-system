package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.WarehouseReceiptRequest;
import com.pharmacy.inventory.enums.ApprovalStatus;
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
    private final com.pharmacy.inventory.repository.InspectionItemRepository itemRepository;
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
            .orElseThrow(() -> new RuntimeException("Không tìm thấy biên bản kiểm nhập với mã: " + request.getReportId()));

        if (report.getStatus() != ApprovalStatus.APPROVED) {
            throw new RuntimeException("Biên bản kiểm nhập phải ở trạng thái ĐÃ PHÊ DUYỆT trước khi lập phiếu nhập kho.");
        }

        Account creator = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Pre-calculate total and map request data
        java.util.Map<String, com.pharmacy.inventory.dto.request.ReceiptItemData> requestDataMap = 
            request.getItems().stream().collect(java.util.stream.Collectors.toMap(i -> i.getItemId(), i -> i));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (InspectionItem item : report.getItems()) {
            BigDecimal itemTotal = new BigDecimal(item.getVatPrice()).multiply(new BigDecimal(item.getBatch().getQuantityReceived()));
            totalAmount = totalAmount.add(itemTotal);
            
            // Map barcode and accounting code from request
            var data = requestDataMap.get(item.getItemID());
            if (data != null) {
                item.setBarcode(data.getBarcode());
                item.setAccountingCode(data.getAccountingCode());
            }
        }

        WarehouseReceipt receipt = WarehouseReceipt.builder()
            .report(report)
            .enteredBy(creator)
            .receiptDate(request.getReceiptDate())
            .totalAmount(totalAmount)
            .profitRate(BigDecimal.ZERO)
            .retailPrice(BigDecimal.ZERO)
            .importPrice(BigDecimal.ZERO)
            .createdAt(LocalDateTime.now())
            .build();

        WarehouseReceipt savedReceipt = receiptRepository.save(receipt);

        // Process each item: update stock and create logbook entry
        for (InspectionItem item : report.getItems()) {
            BigDecimal importPrice = new BigDecimal(item.getVatPrice());
            BigDecimal itemTotalAmount = importPrice.multiply(new BigDecimal(item.getBatch().getQuantityReceived()));
            
            // Update Inventory Stock
            stockService.increaseStock(item.getBatch().getDrug().getDrugID(), item.getBatch().getQuantityReceived());

            // CREATE LOGBOOK ENTRY (BM.05)
            ReceivingLogbook log = ReceivingLogbook.builder()
                .receipt(savedReceipt)
                .logDate(savedReceipt.getReceiptDate())
                .invoiceNumber(item.getBatch().getInvoiceNumber())
                .supplier(item.getBatch().getOrder().getSupplier())
                .drug(item.getBatch().getDrug())
                .drugNameSnapshot(item.getBatch().getDrug().getDrugName())
                .concentrationSnapshot(item.getBatch().getDrug().getConcentration())
                .unit(item.getBatch().getDrug().getUnit())
                .quantity(item.getBatch().getQuantityReceived())
                .lotNumber(item.getBatch().getLotNumber())
                .expirationDate(item.getBatch().getExpirationDate())
                .unitPriceVAT(importPrice)
                .totalAmount(itemTotalAmount)
                .enteredBy(creator)
                .createdAt(LocalDateTime.now())
                .build();
            
            logbookRepository.save(log);
        }

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
