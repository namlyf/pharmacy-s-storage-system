package com.pharmacy.inventory.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WarehouseReceiptRequest {
    private String reportId;
    private LocalDate receiptDate;
    private String barcode;
    private String accountingCode;
    private BigDecimal bhytPrice;
}
