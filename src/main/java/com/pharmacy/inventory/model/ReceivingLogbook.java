package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "receiving_logbook")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivingLogbook {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String logID;

    @OneToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private WarehouseReceipt receipt;

    @Column(nullable = false)
    private LocalDate logDate;

    private String invoiceSymbol;

    @Column(nullable = false)
    private String invoiceNumber;

    private String voucherNumber;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @Column(nullable = false)
    private String drugNameSnapshot;

    @Column(nullable = false)
    private String concentrationSnapshot;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String lotNumber;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPriceVAT;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "entered_by", nullable = false)
    private Account enteredBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
