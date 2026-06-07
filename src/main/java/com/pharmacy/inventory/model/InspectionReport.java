package com.pharmacy.inventory.model;

import com.pharmacy.inventory.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String reportId;

    @ManyToOne
    @JoinColumn(name = "inspected_by", nullable = false)
    private Account inspectedBy;

    @Column(nullable = false)
    private LocalDate inspectionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InspectionItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Account approvedBy;

    private LocalDateTime approvedAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @OneToOne(mappedBy = "report")
    private WarehouseReceipt receipt;

    public void addItem(InspectionItem item) {
        items.add(item);
        item.setReport(this);
    }
}





