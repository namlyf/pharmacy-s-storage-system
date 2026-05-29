package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_receipts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String receiptID;

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    private InspectionReport report;

    @ManyToOne
    @JoinColumn(name = "entered_by", nullable = false)
    private Account enteredBy;

    @Column(nullable = false)
    private LocalDate receiptDate;

    @Column(length = 100)
    private String barcode;

    @Column(length = 50)
    private String accountingCode;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal importPrice; // VAT price from report

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal bhytPrice;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal profitRate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal retailPrice;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Account approvedBy;

    private LocalDateTime approvedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
