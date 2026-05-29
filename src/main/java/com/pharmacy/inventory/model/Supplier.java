package com.pharmacy.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String supplierID;

    @Column(nullable = false, length = 200)
    private String supplierName;

    @Column(nullable = false, length = 100)
    private String licenseNumber;

    @Column(length = 20)
    private String taxCode;

    @Column(nullable = false)
    private String address;

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private boolean isActive = true;

    public Supplier() {}

    // Getters
    public String getSupplierID() { return supplierID; }
    public String getSupplierName() { return supplierName; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getTaxCode() { return taxCode; }
    public String getAddress() { return address; }
    public String getContactPerson() { return contactPerson; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }
    public void setAddress(String address) { this.address = address; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setActive(boolean active) { isActive = active; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Supplier supplier = new Supplier();

        public Builder supplierName(String v) { supplier.supplierName = v; return this; }
        public Builder licenseNumber(String v) { supplier.licenseNumber = v; return this; }
        public Builder taxCode(String v) { supplier.taxCode = v; return this; }
        public Builder address(String v) { supplier.address = v; return this; }
        public Builder contactPerson(String v) { supplier.contactPerson = v; return this; }
        public Builder phone(String v) { supplier.phone = v; return this; }
        public Builder email(String v) { supplier.email = v; return this; }

        public Supplier build() { return supplier; }  // ← trả về Supplier, không phải Builder
    }
}