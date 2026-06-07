package com.pharmacy.inventory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReceiptRequest {
    private String reportId;
    private LocalDate receiptDate;
    private List<ReceiptItemData> items = new ArrayList<>();
}





