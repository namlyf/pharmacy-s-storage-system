package com.pharmacy.inventory.model;

import com.pharmacy.inventory.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "drug_requisition_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugRequisitionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String itemID;

    @ManyToOne
    @JoinColumn(name = "requisition_id", nullable = false)
    private DrugRequisition requisition;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(length = 100)
    private String packagingSpec;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier preferredSupplier;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Account approvedBy;

    private LocalDateTime approvedAt;
}
