package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drugs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drugId;

    @Column(nullable = false, length = 150)
    private String drugName;

    @Column(length = 100)
    private String concentration;

    @Column(length = 100)
    private String dosageForm;

    @Column(unique = true, length = 50)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public enum Category {
        MEDICINE,       // Thuốc
        SUPPLEMENT,     // Thực phẩm chức năng
        COSMETIC        // Mỹ phẩm
    }
}