package com.pharmacy.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InventoryStockRequest {

    @NotBlank(message = "Drug is required")
    private String drugId;

    @NotNull
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer currentQuantity;

    @NotNull
    @Min(value = 0, message = "Threshold cannot be negative")
    private Integer minimumThreshold;

    private String storageLocation;

    public InventoryStockRequest() {}

    public String getDrugId() { return drugId; }
    public void setDrugId(String drugId) { this.drugId = drugId; }

    public Integer getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }

    public Integer getMinimumThreshold() { return minimumThreshold; }
    public void setMinimumThreshold(Integer minimumThreshold) { this.minimumThreshold = minimumThreshold; }

    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
}