package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String supplierId;

    @Column(nullable = false, length = 200)
    private String supplierName;

    @Column(nullable = false, length = 100)
    private String licenseNumber;

    @Column(length = 20)
    private String taxCode;

    @Column(nullable = false)
    private String address;

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}




