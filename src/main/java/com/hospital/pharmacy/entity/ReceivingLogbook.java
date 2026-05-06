package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "receiving_logbooks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivingLogbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @OneToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private WarehouseReceipt warehouseReceipt;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DrugBatch drugBatch;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "entered_by", nullable = false)
    private Account enteredBy;
}