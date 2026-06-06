package com.pharmacy.inventory.dto.request;

import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Data
public class InspectionReportRequest {
    private LocalDate inspectionDate;
    private List<InspectionItemRequest> items;
}
