package com.pharmacy.inventory.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DrugBatchRequest {
    private String orderId;
    private String drugId;
    private String lotNumber;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private Integer quantityReceived;
    private String storageCondition;
    private String invoiceNumber;
    private LocalDate invoiceDate;
}





