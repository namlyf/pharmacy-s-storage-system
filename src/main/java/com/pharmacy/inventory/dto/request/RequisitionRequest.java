package com.pharmacy.inventory.dto.request;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class RequisitionRequest {
    private String notes;
    private List<RequisitionItemRequest> items = new ArrayList<>();
}





