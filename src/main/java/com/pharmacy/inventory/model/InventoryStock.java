package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String stockId;

    @OneToOne
    @JoinColumn(name = "drug_id", nullable = false, unique = true)
    private Drug drug;

    @Column(nullable = false)
    @Builder.Default
    private int currentQuantity = 0;

    @Column(nullable = false)
    @Builder.Default
    private int minimumThreshold = 0;

    @Column(length = 100)
    private String storageLocation;

    @Column
    private LocalDateTime lastUpdated;

    // Helper – kiểm tra có đang low stock không
    public boolean isLowStock() {
        return currentQuantity <= minimumThreshold;
    }
}




