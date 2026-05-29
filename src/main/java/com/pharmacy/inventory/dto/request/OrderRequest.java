package com.pharmacy.inventory.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequest {
    private String requisitionId;
    private String supplierId;
    private LocalDate expectedDeliveryDate;
    private String notes;
    private List<String> requisitionItemIds; // Approved items to include
}
