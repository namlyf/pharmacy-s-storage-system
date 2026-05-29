package com.pharmacy.inventory.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InspectionReportRequest {
    private String batchId;
    private LocalDate inspectionDate;
    private boolean invoiceValid;
    private String visualQualityResult;
    private boolean storageConditionMatch;
    private BigDecimal storageTemperature;
    private BigDecimal vatPrice;
}
