package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "drug_requisitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugRequisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requisitionId;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Account createdBy;

    @Column(nullable = false, length = 150)
    private String medicineName;

    @Column(length = 100)
    private String dosage;

    @Column(length = 50)
    private String unit;

    @Column(nullable = false)
    private LocalDate requisitionDate;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        PENDING,    // Chờ duyệt
        APPROVED,   // Đã duyệt
        REJECTED    // Từ chối
    }
}