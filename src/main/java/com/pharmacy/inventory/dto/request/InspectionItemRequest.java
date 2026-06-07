package com.pharmacy.inventory.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InspectionItemRequest {
    private String batchId;
    private boolean invoiceValid;
    private String visualQualityResult;
    private boolean storageConditionMatch;
    private Double storageTemperature;
    private Double vatPrice;
}





