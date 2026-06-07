package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "drug_batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String batchId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @Column(nullable = false, length = 100)
    private String lotNumber;

    @Column(nullable = false)
    private LocalDate manufactureDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Integer quantityReceived;

    @Column(length = 200)
    private String storageCondition;

    @Column(nullable = false, length = 100)
    private String invoiceNumber;

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Account createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}





