package com.pharmacy.inventory.model;

import com.pharmacy.inventory.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reportID;

    @OneToOne
    @JoinColumn(name = "batch_id", nullable = false, unique = true)
    private DrugBatch batch;

    @ManyToOne
    @JoinColumn(name = "inspected_by", nullable = false)
    private Account inspectedBy;

    @Column(nullable = false)
    private LocalDate inspectionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(nullable = false)
    private boolean sdkValid;

    @Column(nullable = false)
    private boolean shelfLifeValid;

    @Column(nullable = false)
    private boolean invoiceValid;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String visualQualityResult;

    @Column(nullable = false)
    private boolean storageConditionMatch;

    private BigDecimal storageTemperature;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal vatPrice;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Account approvedBy;

    private LocalDateTime approvedAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;
}
