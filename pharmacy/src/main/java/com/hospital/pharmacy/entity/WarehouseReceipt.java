package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "warehouse_receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false)
    private InspectionReport inspectionReport;

    @OneToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DrugBatch drugBatch;

    @Column(nullable = false)
    private LocalDate receiptDate;

    @ManyToOne
    @JoinColumn(name = "entered_by", nullable = false)
    private Account enteredBy;

    @Column(precision = 15, scale = 2)
    private BigDecimal retailPrice;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Account approvedBy;
}