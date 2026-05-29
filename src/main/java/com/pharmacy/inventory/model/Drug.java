package com.pharmacy.inventory.model;

import com.pharmacy.inventory.enums.Category;
import com.pharmacy.inventory.enums.DosageForm;
import jakarta.persistence.*;

@Entity
@Table(name = "drugs")
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String drugID;

    @Column(nullable = false, length = 200)
    private String drugName;

    @Column(nullable = false, length = 200)
    private String activeIngredient;

    @Column(nullable = false, length = 100)
    private String concentration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DosageForm dosageForm;

    @Column(nullable = false, length = 50, unique = true)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String manufacturer;

    @Column(nullable = false, length = 100)
    private String countryOfOrigin;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(nullable = false, length = 100)
    private String packagingSpec;

    @Column(length = 200)
    private String storageCondition;

    @Column(nullable = false)
    private boolean isActive = true;

    public Drug() {}

    // Getters
    public String getDrugID() { return drugID; }
    public String getDrugName() { return drugName; }
    public String getActiveIngredient() { return activeIngredient; }
    public String getConcentration() { return concentration; }
    public DosageForm getDosageForm() { return dosageForm; }
    public String getRegistrationNumber() { return registrationNumber; }
    public Category getCategory() { return category; }
    public String getManufacturer() { return manufacturer; }
    public String getCountryOfOrigin() { return countryOfOrigin; }
    public String getUnit() { return unit; }
    public String getPackagingSpec() { return packagingSpec; }
    public String getStorageCondition() { return storageCondition; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setDrugID(String drugID) { this.drugID = drugID; }
    public void setDrugName(String drugName) { this.drugName = drugName; }
    public void setActiveIngredient(String activeIngredient) { this.activeIngredient = activeIngredient; }
    public void setConcentration(String concentration) { this.concentration = concentration; }
    public void setDosageForm(DosageForm dosageForm) { this.dosageForm = dosageForm; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public void setCategory(Category category) { this.category = category; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setPackagingSpec(String packagingSpec) { this.packagingSpec = packagingSpec; }
    public void setStorageCondition(String storageCondition) { this.storageCondition = storageCondition; }
    public void setActive(boolean active) { isActive = active; }

    // Builder pattern thay cho @Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Drug drug = new Drug();

        public Builder drugName(String v) { drug.drugName = v; return this; }
        public Builder activeIngredient(String v) { drug.activeIngredient = v; return this; }
        public Builder concentration(String v) { drug.concentration = v; return this; }
        public Builder dosageForm(DosageForm v) { drug.dosageForm = v; return this; }
        public Builder registrationNumber(String v) { drug.registrationNumber = v; return this; }
        public Builder category(Category v) { drug.category = v; return this; }
        public Builder manufacturer(String v) { drug.manufacturer = v; return this; }
        public Builder countryOfOrigin(String v) { drug.countryOfOrigin = v; return this; }
        public Builder unit(String v) { drug.unit = v; return this; }
        public Builder packagingSpec(String v) { drug.packagingSpec = v; return this; }
        public Builder storageCondition(String v) { drug.storageCondition = v; return this; }
        public Drug build() { return drug; }
    }
}