package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "receiving_logbooks")
public class ReceivingLogbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @OneToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private WarehouseReceipt warehouseReceipt;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DrugBatch drugBatch;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "entered_by", nullable = false)
    private Account enteredBy;

    public ReceivingLogbook() {}

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public WarehouseReceipt getWarehouseReceipt() { return warehouseReceipt; }
    public void setWarehouseReceipt(WarehouseReceipt warehouseReceipt) { this.warehouseReceipt = warehouseReceipt; }

    public DrugBatch getDrugBatch() { return drugBatch; }
    public void setDrugBatch(DrugBatch drugBatch) { this.drugBatch = drugBatch; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Account getEnteredBy() { return enteredBy; }
    public void setEnteredBy(Account enteredBy) { this.enteredBy = enteredBy; }
}