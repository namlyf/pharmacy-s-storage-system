package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_stocks")
public class InventoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String stockID;

    @OneToOne
    @JoinColumn(name = "drug_id", nullable = false, unique = true)
    private Drug drug;

    @Column(nullable = false)
    private int currentQuantity = 0;

    @Column(nullable = false)
    private int minimumThreshold = 0;

    @Column(length = 100)
    private String storageLocation;

    @Column
    private LocalDateTime lastUpdated;

    public InventoryStock() {}

    // Getters
    public String getStockID() { return stockID; }
    public Drug getDrug() { return drug; }
    public int getCurrentQuantity() { return currentQuantity; }
    public int getMinimumThreshold() { return minimumThreshold; }
    public String getStorageLocation() { return storageLocation; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    // Setters
    public void setStockID(String stockID) { this.stockID = stockID; }
    public void setDrug(Drug drug) { this.drug = drug; }
    public void setCurrentQuantity(int currentQuantity) { this.currentQuantity = currentQuantity; }
    public void setMinimumThreshold(int minimumThreshold) { this.minimumThreshold = minimumThreshold; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    // Helper – kiểm tra có đang low stock không
    public boolean isLowStock() {
        return currentQuantity <= minimumThreshold;
    }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final InventoryStock stock = new InventoryStock();

        public Builder drug(Drug v) { stock.drug = v; return this; }
        public Builder currentQuantity(int v) { stock.currentQuantity = v; return this; }
        public Builder minimumThreshold(int v) { stock.minimumThreshold = v; return this; }
        public Builder storageLocation(String v) { stock.storageLocation = v; return this; }
        public InventoryStock build() { return stock; }
    }
}