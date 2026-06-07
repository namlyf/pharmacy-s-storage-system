package com.pharmacy.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    private String taxCode;

    @NotBlank(message = "Address is required")
    private String address;

    private String contactPerson;
    private String phone;
    private String email;

    @Builder.Default
    private boolean active = true;
}




