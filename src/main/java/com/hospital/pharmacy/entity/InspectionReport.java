package com.hospital.pharmacy.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inspection_reports")
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
        PENDING, APPROVED, REJECTED
    }

    public InspectionReport() {}

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    public DrugBatch getDrugBatch() { return drugBatch; }
    public void setDrugBatch(DrugBatch drugBatch) { this.drugBatch = drugBatch; }

    public Account getInspectedBy() { return inspectedBy; }
    public void setInspectedBy(Account inspectedBy) { this.inspectedBy = inspectedBy; }

    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getVisualQualityResult() { return visualQualityResult; }
    public void setVisualQualityResult(String v) { this.visualQualityResult = v; }

    public Boolean getLegalityValid() { return legalityValid; }
    public void setLegalityValid(Boolean legalityValid) { this.legalityValid = legalityValid; }

    public Boolean getInvoiceValid() { return invoiceValid; }
    public void setInvoiceValid(Boolean invoiceValid) { this.invoiceValid = invoiceValid; }

    public Boolean getShelfLifeValid() { return shelfLifeValid; }
    public void setShelfLifeValid(Boolean shelfLifeValid) { this.shelfLifeValid = shelfLifeValid; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}