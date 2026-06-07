package com.pharmacy.inventory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItemData {
    private String itemId;
    private String barcode;
    private String accountingCode;
}





