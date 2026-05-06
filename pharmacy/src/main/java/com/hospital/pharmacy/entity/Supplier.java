package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @Column(nullable = false, length = 150)
    private String supplierName;

    @Column(unique = true, length = 50)
    private String licenseNumber;

    @Column(length = 255)
    private String contactInfo;
}