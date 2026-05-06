package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "warehouse_receipts")
public class WarehouseReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false)
    private InspectionReport inspectionReport;

    @OneToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DrugBatch drugBatch;

    @Column(nullable = false)
    private LocalDate receiptDate;

    @ManyToOne
    @JoinColumn(name = "entered_by", nullable = false)
    private Account enteredBy;

    @Column(precision = 15, scale = 2)
    private BigDecimal retailPrice;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Account approvedBy;

    public WarehouseReceipt() {}

    public Long getReceiptId() { return receiptId; }
    public void setReceiptId(Long receiptId) { this.receiptId = receiptId; }

    public InspectionReport getInspectionReport() { return inspectionReport; }
    public void setInspectionReport(InspectionReport inspectionReport) { this.inspectionReport = inspectionReport; }

    public DrugBatch getDrugBatch() { return drugBatch; }
    public void setDrugBatch(DrugBatch drugBatch) { this.drugBatch = drugBatch; }

    public LocalDate getReceiptDate() { return receiptDate; }
    public void setReceiptDate(LocalDate receiptDate) { this.receiptDate = receiptDate; }

    public Account getEnteredBy() { return enteredBy; }
    public void setEnteredBy(Account enteredBy) { this.enteredBy = enteredBy; }

    public BigDecimal getRetailPrice() { return retailPrice; }
    public void setRetailPrice(BigDecimal retailPrice) { this.retailPrice = retailPrice; }

    public Account getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Account approvedBy) { this.approvedBy = approvedBy; }
}