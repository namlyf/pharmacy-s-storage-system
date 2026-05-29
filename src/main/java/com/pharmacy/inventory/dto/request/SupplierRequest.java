package com.pharmacy.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;

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
    private boolean isActive = true;

    public SupplierRequest() {}

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean active) { isActive = active; }
}