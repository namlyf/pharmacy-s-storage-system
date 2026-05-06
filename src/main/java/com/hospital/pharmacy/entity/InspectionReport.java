package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "inspection_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @OneToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DrugBatch drugBatch;

    @ManyToOne
    @JoinColumn(name = "inspected_by", nullable = false)
    private Account inspectedBy;

    @Column(nullable = false)
    private LocalDate inspectionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(length = 255)
    private String visualQualityResult;

    private Boolean legalityValid;
    private Boolean invoiceValid;
    private Boolean shelfLifeValid;

    @Column(length = 255)
    private String rejectionReason;

    public enum Status {
        PENDING,    // Chờ kiểm
        APPROVED,   // Đạt
        REJECTED    // Không đạt
    }
}