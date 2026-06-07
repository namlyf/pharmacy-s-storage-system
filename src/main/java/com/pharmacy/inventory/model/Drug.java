package com.pharmacy.inventory.model;

import com.pharmacy.inventory.enums.Category;
import com.pharmacy.inventory.enums.DosageForm;
import com.pharmacy.inventory.enums.DrugStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drugs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String drugId;

    @Column(nullable = false, length = 200)
    private String drugName;

    @Column(nullable = false, length = 200)
    private String activeIngredient;

    @Column(nullable = false, length = 100)
    private String concentration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DosageForm dosageForm;

    @Column(nullable = false, length = 50, unique = true)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String manufacturer;

    @Column(nullable = false, length = 100)
    private String countryOfOrigin;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(nullable = false, length = 100)
    private String packagingSpec;

    @Column(length = 200)
    private String storageCondition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DrugStatus status = DrugStatus.DRAFT;
}




