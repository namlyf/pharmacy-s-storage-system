package com.pharmacy.inventory.model;

import com.pharmacy.inventory.enums.RequisitionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drug_requisitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugRequisition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String requisitionId;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Account createdBy;

    @Column(nullable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequisitionStatus overallStatus;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DrugRequisitionItem> items = new ArrayList<>();

    public void addItem(DrugRequisitionItem item) {
        items.add(item);
        item.setRequisition(this);
    }
}





