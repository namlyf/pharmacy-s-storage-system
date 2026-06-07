package com.pharmacy.inventory.dto.request;

import lombok.Data;

@Data
public class RequisitionItemRequest {
    private String drugId;
    private Integer quantity;
    private String unit;
    private String packagingSpec;
    private String preferredsupplierId;
    private String reason;
}





