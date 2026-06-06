package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String itemID;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private InspectionReport report;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DrugBatch batch;

    @Column(nullable = false)
    private boolean sdkValid;

    @Column(nullable = false)
    private boolean shelfLifeValid;

    @Column(nullable = false)
    private boolean invoiceValid;

    @Column(columnDefinition = "TEXT")
    private String visualQualityResult;

    @Column(nullable = false)
    private boolean storageConditionMatch;

    private Double storageTemperature;

    @Column(nullable = false)
    private Double vatPrice;

    @Column(length = 100)
    private String barcode;

    @Column(length = 50)
    private String accountingCode;
}
