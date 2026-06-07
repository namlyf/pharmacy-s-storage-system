package com.pharmacy.inventory.dto.request;

import com.pharmacy.inventory.enums.DosageForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DrugRequest {

    @NotBlank(message = "Drug name is required")
    private String drugName;

    @NotBlank(message = "Active ingredient is required")
    private String activeIngredient;

    @NotBlank(message = "Concentration is required")
    private String concentration;

    @NotNull(message = "Dosage form is required")
    private DosageForm dosageForm;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Country of origin is required")
    private String countryOfOrigin;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotBlank(message = "Packaging spec is required")
    private String packagingSpec;

    private String storageCondition;

    private Integer minimumThreshold = 0;  // ← THÊM MỚI

    public DrugRequest() {}

    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }

    public String getActiveIngredient() { return activeIngredient; }
    public void setActiveIngredient(String activeIngredient) { this.activeIngredient = activeIngredient; }

    public String getConcentration() { return concentration; }
    public void setConcentration(String concentration) { this.concentration = concentration; }

    public DosageForm getDosageForm() { return dosageForm; }
    public void setDosageForm(DosageForm dosageForm) { this.dosageForm = dosageForm; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getCountryOfOrigin() { return countryOfOrigin; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getPackagingSpec() { return packagingSpec; }
    public void setPackagingSpec(String packagingSpec) { this.packagingSpec = packagingSpec; }

    public String getStorageCondition() { return storageCondition; }
    public void setStorageCondition(String storageCondition) { this.storageCondition = storageCondition; }

    public Integer getMinimumThreshold() { return minimumThreshold; }
    public void setMinimumThreshold(Integer minimumThreshold) { this.minimumThreshold = minimumThreshold; }
}




