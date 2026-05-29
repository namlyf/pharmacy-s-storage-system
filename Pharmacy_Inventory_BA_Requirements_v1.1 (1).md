# Hospital Pharmacy Inventory Intake System — Business Analysis Requirements Document

| **Document Version** | 1.1 |
|---|---|
| **Project Name** | Pharmacy Inventory Intake System (Web-based) |
| **Organization** | Hospital Pharmacy – Hanoi Medical University Hospital |
| **Address** | No.1 Ton That Tung Street, Dong Da District, Hanoi |
| **Based on** | SOP 01-GPP: Drug Procurement and Quality Control (2023) |
| **Prepared for** | Development Team |
| **Date** | May 2026 |

---

## 1. Project Overview

### 1.1 Background

The Hospital Pharmacy of Hanoi Medical University Hospital currently manages all drug procurement and intake processes using manual paper-based forms. This creates inefficiencies in data accuracy, traceability, approval delays, and compliance reporting. The system must digitalize the complete drug receiving workflow as defined in SOP 01-GPP (Drug Procurement and Quality Control, 2023 edition).

### 1.2 Objectives

- Replace all manual paper forms (BM.01 through BM.05) with a structured digital workflow.
- Enforce regulatory validation rules automatically (SĐK check, shelf-life check, invoice legality).
- Implement role-based approval flows reflecting the real organizational hierarchy.
- Maintain full traceability and audit history for all drug batches.
- Automatically calculate retail prices based on the 5-tier markup schedule defined in SOP 01.
- Provide a real-time inventory dashboard with low-stock alerts to trigger procurement forecasting.

### 1.3 Scope

This system covers the following steps from SOP 01-GPP:

| Step | SOP Reference | Description | In Scope |
|---|---|---|---|
| 1 | – | Procurement Planning (triggers from inventory) | Yes |
| 2 | BM.01.D.01 | Drug Procurement Forecast (Dự trù thuốc) | Yes |
| 3 | BM.02.D.01 | Purchase Order (Đơn đặt hàng) | Yes |
| 4 | BM.03.D.01 | Goods Inspection / Receiving Inspection (Biên bản kiểm nhập) | Yes |
| 5 | BM.04.D.01 | Warehouse Entry / Inventory Intake (Phiếu nhập kho) | Yes |
| 6 | BM.05.D.01 | Receiving Logbook (Sổ kiểm nhập) | Yes |
| 7 | – | Price Listing (Niêm yết giá) | Yes |
| 8 | – | Classification, Arrangement, Storage | Yes |
| – | – | Drug dispensing and sales to patients | No |
| – | – | Controlled substance SOP (SOP 02-GPP) | No |

### 1.4 Stakeholders

| Role | Vietnamese Title | Responsibilities in System |
|---|---|---|
| Pharmacist | Dược sĩ / Tổ cung ứng | Create procurement forecasts (BM.01), create purchase orders (BM.02), view inventory dashboard |
| Warehouse Staff | Nhân viên kho | Create drug batches on goods arrival, perform receiving inspection (BM.03), create warehouse receipts (BM.04), view logbook |
| Manager | Trưởng khoa Dược / Hội đồng kiểm nhập | Approve/reject procurement forecast line items, approve/reject inspection reports, oversee inventory |

---

## 2. Implementation Order

All modules must be built in the following sequence due to data dependencies. No module can be functionally tested without its prerequisites being complete.

| Phase | Module | Depends On | Key Output |
|---|---|---|---|
| 1 | Authentication & User Management | – | Logged-in users with assigned roles |
| 2 | Master Data: Drug & Supplier | Phase 1 | Drug catalog and supplier registry |
| 3 | Inventory Stock (Kho) | Phase 2 | Current stock quantities and low-stock alerts |
| 4 | BM.01 – Procurement Forecast | Phase 3 | Approved forecast line items |
| 5 | BM.02 – Purchase Order | Phase 4 | Sent purchase orders grouped by supplier |
| 6 | Drug Batch Creation | Phase 5 | Registered incoming drug batches |
| 7 | BM.03 – Receiving Inspection | Phase 6 | Approved inspection reports |
| 8 | BM.04 – Warehouse Receipt | Phase 7 | Issued receipts; inventory quantities updated |
| 9 | BM.05 – Receiving Logbook | Phase 8 | Auto-generated audit log |
| 10 | Inventory Dashboard & Classification | Phase 9 | Full inventory view with auto-classification |

> **Key Insight:** The Inventory Stock module (Phase 3) must exist before BM.01, because procurement forecasting is always triggered by observing current stock levels. A drug procurement forecast without an existing inventory is not operationally valid.

---

## 3. Data Model

### 3.1 Entity Overview

The following entities form the core data model. Each entity maps directly to either an official SOP form or a system support structure.

| Entity | SOP Form | Description |
|---|---|---|
| Account | – | System users with role-based access |
| Drug | – | Master catalog of all drug products |
| Supplier | – | Registered pharmaceutical suppliers |
| InventoryStock | – | Current stock levels per drug (NEW – not in original design) |
| DrugRequisition | BM.01.D.01 | Procurement forecast header |
| DrugRequisitionItem | BM.01.D.01 | Individual drug line in a forecast (NEW – for per-line approval) |
| Order | BM.02.D.01 | Purchase order header per supplier |
| OrderItem | BM.02.D.01 | Individual drug line in a purchase order (NEW) |
| DrugBatch | – | Registered incoming batch upon goods arrival |
| InspectionReport | BM.03.D.01 | Receiving inspection record |
| WarehouseReceipt | BM.04.D.01 | Warehouse intake receipt (Form C21) |
| ReceivingLogbook | BM.05.D.01 | Auto-generated receiving log entry |

### 3.2 Detailed Field Specifications

#### 3.2.1 Account

| Field | Type | Required | Notes |
|---|---|---|---|
| UserID | UUID | Yes | Primary key |
| Username | VARCHAR(50) | Yes | Unique login name |
| Password | VARCHAR(255) | Yes | Hashed (bcrypt) |
| FullName | VARCHAR(100) | Yes | Display name |
| Role | ENUM | Yes | Values: PHARMACIST, WAREHOUSE_STAFF, MANAGER |
| IsActive | BOOLEAN | Yes | Account enabled/disabled |
| CreatedAt | TIMESTAMP | Yes | Auto-generated |

#### 3.2.2 Drug

| Field | Type | Required | Notes |
|---|---|---|---|
| DrugID | UUID | Yes | Primary key |
| DrugName | VARCHAR(200) | Yes | Official drug name |
| ActiveIngredient | VARCHAR(200) | Yes | Hoạt chất |
| Concentration | VARCHAR(100) | Yes | e.g. 500mg, 10mg/5ml |
| DosageForm | ENUM | Yes | TABLET, CAPSULE, SYRUP, INJECTION, CREAM, DROPS, SUPPOSITORY, GRANULE, OTHER |
| RegistrationNumber | VARCHAR(50) | Yes | SĐK – used for auto-classification |
| Category | ENUM | Auto | MEDICINE, SUPPLEMENT, COSMETIC – derived from RegistrationNumber prefix |
| Manufacturer | VARCHAR(200) | Yes | Hãng sản xuất |
| CountryOfOrigin | VARCHAR(100) | Yes | Nước sản xuất |
| Unit | VARCHAR(50) | Yes | ĐVT – box, vial, tube, etc. |
| PackagingSpec | VARCHAR(100) | Yes | Quy cách đóng gói – e.g. 10 viên/vỉ, 10 vỉ/hộp |
| StorageCondition | VARCHAR(200) | No | e.g. Store below 25°C, 2–8°C |
| IsActive | BOOLEAN | Yes | Active in catalog |

> **Auto-Classification Rule:** Category is derived from RegistrationNumber prefix. VN / VD = MEDICINE; /YT-CBTC / /YT-CNTC / /ATTP-XNCB = SUPPLEMENT; /MP-QLD / /CBMP-QLD = COSMETIC.

#### 3.2.3 Supplier

| Field | Type | Required | Notes |
|---|---|---|---|
| SupplierID | UUID | Yes | Primary key |
| SupplierName | VARCHAR(200) | Yes | Legal company name |
| LicenseNumber | VARCHAR(100) | Yes | Pharmaceutical distribution license |
| TaxCode | VARCHAR(20) | No | Mã số thuế |
| Address | TEXT | Yes | Full address |
| ContactPerson | VARCHAR(100) | No | |
| Phone | VARCHAR(30) | No | |
| Email | VARCHAR(100) | No | |
| IsActive | BOOLEAN | Yes | Active supplier |

#### 3.2.4 InventoryStock (NEW)

> **Why This Entity Exists:** Inventory Stock represents the current physical state of the warehouse. It must be created and populated before procurement forecasting can begin, because forecasts are triggered by observing low stock levels.

| Field | Type | Required | Notes |
|---|---|---|---|
| StockID | UUID | Yes | Primary key |
| DrugID | UUID FK | Yes | References Drug |
| CurrentQuantity | INTEGER | Yes | Current on-hand quantity |
| MinimumThreshold | INTEGER | Yes | Low-stock alert threshold – triggers forecast suggestion |
| StorageLocation | VARCHAR(100) | No | Physical location in warehouse |
| LastUpdated | TIMESTAMP | Auto | Updated whenever BM.04 is approved |

> **Update Trigger:** CurrentQuantity increases automatically when a WarehouseReceipt (BM.04) is approved. The system should display a visual warning when CurrentQuantity <= MinimumThreshold.

#### 3.2.5 DrugRequisition – BM.01.D.01 (Header)

| Field | Type | Required | Notes |
|---|---|---|---|
| RequisitionID | UUID | Yes | Primary key |
| CreatedBy | UUID FK | Yes | References Account (Pharmacist or Warehouse Staff) |
| CreatedDate | DATE | Yes | Date of forecast |
| OverallStatus | ENUM | Auto | PENDING / PARTIALLY_APPROVED / APPROVED / REJECTED – derived from line items |
| Notes | TEXT | No | General notes on the forecast |

#### 3.2.6 DrugRequisitionItem – BM.01.D.01 (Line Item) (NEW)

> **Why Separate Entity:** SOP 01 states the Manager approves each drug line individually. One forecast may contain 10 drugs; some may be approved and others rejected. A flat header model cannot support per-line approval status.

| Field | Type | Required | Notes |
|---|---|---|---|
| ItemID | UUID | Yes | Primary key |
| RequisitionID | UUID FK | Yes | References DrugRequisition |
| DrugID | UUID FK | Yes | References Drug |
| Quantity | INTEGER | Yes | Requested quantity |
| Unit | VARCHAR(50) | Yes | Unit of measure |
| PackagingSpec | VARCHAR(100) | No | Requested packaging specification |
| SupplierID | UUID FK | No | Preferred supplier for this line |
| Reason | TEXT | Conditional | Required only when quantity is an unusual spike; optional for routine restocking |
| ApprovalStatus | ENUM | Auto | PENDING / APPROVED / REJECTED |
| RejectionReason | TEXT | Conditional | Required if Manager rejects this line |
| ApprovedBy | UUID FK | No | References Account (Manager) |
| ApprovedAt | TIMESTAMP | No | Time of approval/rejection |

#### 3.2.7 Order – BM.02.D.01 (Header)

> **Grouping Rule:** One DrugRequisition may produce multiple Orders. Each Order covers one Supplier. The system should allow the Pharmacist to group approved DrugRequisitionItems by Supplier and generate one Order per Supplier.

| Field | Type | Required | Notes |
|---|---|---|---|
| OrderID | UUID | Yes | Primary key |
| RequisitionID | UUID FK | Yes | References DrugRequisition |
| SupplierID | UUID FK | Yes | References Supplier |
| CreatedBy | UUID FK | Yes | References Account |
| OrderDate | DATE | Yes | |
| ExpectedDeliveryDate | DATE | No | NEW – estimated arrival date from supplier |
| Status | ENUM | Yes | DRAFT / SENT / RECEIVED / PARTIALLY_RECEIVED / RETURNED |
| Notes | TEXT | No | |

#### 3.2.8 OrderItem – BM.02.D.01 (Line Item) (NEW)

| Field | Type | Required | Notes |
|---|---|---|---|
| OrderItemID | UUID | Yes | Primary key |
| OrderID | UUID FK | Yes | References Order |
| DrugID | UUID FK | Yes | References Drug |
| Quantity | INTEGER | Yes | Ordered quantity |
| Unit | VARCHAR(50) | Yes | |
| PackagingSpec | VARCHAR(100) | No | |

#### 3.2.9 DrugBatch

> **When Created:** Warehouse Staff creates a DrugBatch when physical goods arrive at the pharmacy, before inspection begins. This represents one lot/shipment of one drug from one supplier.

| Field | Type | Required | Notes |
|---|---|---|---|
| BatchID | UUID | Yes | Primary key |
| OrderID | UUID FK | Yes | References Order |
| DrugID | UUID FK | Yes | References Drug |
| LotNumber | VARCHAR(100) | Yes | Số lô – batch number from manufacturer |
| ManufactureDate | DATE | Yes | Ngày sản xuất |
| ExpirationDate | DATE | Yes | Hạn dùng |
| QuantityReceived | INTEGER | Yes | Actual quantity received (may differ from ordered) |
| StorageCondition | VARCHAR(200) | No | Required storage condition from label |
| InvoiceNumber | VARCHAR(100) | Yes | NEW – Số hóa đơn |
| InvoiceDate | DATE | Yes | NEW – Ngày hóa đơn |
| CreatedBy | UUID FK | Yes | References Account (Warehouse Staff) |
| CreatedAt | TIMESTAMP | Auto | |

#### 3.2.10 InspectionReport – BM.03.D.01

| Field | Type | Required | Notes |
|---|---|---|---|
| ReportID | UUID | Yes | Primary key |
| BatchID | UUID FK | Yes | References DrugBatch (one report per batch) |
| InspectedBy | UUID FK | Yes | References Account (Warehouse Staff) |
| InspectionDate | DATE | Yes | |
| Status | ENUM | Auto | PENDING / APPROVED / REJECTED |
| SdkValid | BOOLEAN | Auto | System checks SĐK format against classification rules |
| ShelfLifeValid | BOOLEAN | Auto | System checks ExpirationDate – today > 12 months |
| InvoiceValid | BOOLEAN | Manual | Staff confirms invoice is present and complete |
| VisualQualityResult | TEXT | Yes | Staff records sensory check results per dosage form |
| StorageConditionMatch | BOOLEAN | Yes | Does actual storage at warehouse match label requirement? |
| StorageTemperature | DECIMAL(5,2) | No | NEW – Nhiệt độ BQ recorded at time of inspection |
| VATPrice | DECIMAL(15,2) | Yes | NEW – Giá nhập VAT from invoice |
| ApprovedBy | UUID FK | No | References Account (Manager) |
| ApprovedAt | TIMESTAMP | No | |
| RejectionReason | TEXT | Conditional | Required if Status = REJECTED |

> **On Rejection:** If the inspection report is rejected, the corresponding DrugBatch is flagged as RETURNED. Order status is updated to RETURNED or PARTIALLY_RECEIVED. The inventory stock is NOT updated. Staff must physically return goods and invoice to supplier.

#### 3.2.11 WarehouseReceipt – BM.04.D.01 (Form C21)

| Field | Type | Required | Notes |
|---|---|---|---|
| ReceiptID | UUID | Yes | Primary key |
| ReportID | UUID FK | Yes | References InspectionReport (must be APPROVED) |
| EnteredBy | UUID FK | Yes | Người lập – References Account |
| ReceiptDate | DATE | Yes | Date of warehouse entry |
| Barcode | VARCHAR(100) | No | NEW – Mã vạch |
| AccountingCode | VARCHAR(50) | No | NEW – Mã kế toán |
| ImportPrice | DECIMAL(15,2) | Yes | Giá nhập |
| TotalAmount | DECIMAL(15,2) | Auto | Thành tiền = InspectionReport.VATPrice × Quantity |
| BHYTPrice | DECIMAL(15,2) | No | NEW – Giá BHYT (health insurance reimbursement price) |
| ProfitRate | DECIMAL(5,2) | Auto | Tỷ lệ lãi – computed from 5-tier markup table |
| RetailPrice | DECIMAL(15,2) | Auto | Giá bán = InspectionReport.VATPrice × (1 + ProfitRate / 100) |
| ApprovedBy | UUID FK | No | Phụ trách bộ phận nhập kho |
| ApprovedAt | TIMESTAMP | No | |

#### 3.2.12 ReceivingLogbook – BM.05.D.01

> **Auto-generated:** This record is created automatically by the system when a WarehouseReceipt is saved. No manual entry is required from staff.

| Field | Type | Required | Notes |
|---|---|---|---|
| LogID | UUID | Yes | Primary key |
| ReceiptID | UUID FK | Yes | References WarehouseReceipt |
| LogDate | DATE | Auto | Date of warehouse entry |
| InvoiceSymbol | VARCHAR(50) | No | NEW – Ký hiệu HĐ |
| InvoiceNumber | VARCHAR(100) | Yes | NEW – Số HĐ (carried from DrugBatch) |
| VoucherNumber | VARCHAR(100) | No | NEW – Số phiếu |
| SupplierID | UUID FK | Yes | NEW – Nhà CC (carried from DrugBatch) |
| DrugID | UUID FK | Yes | References Drug |
| DrugNameSnapshot | VARCHAR(200) | Yes | Drug name at time of entry (immutable) |
| Concentration | VARCHAR(100) | Yes | Snapshot at time of entry |
| Unit | VARCHAR(50) | Yes | ĐVT |
| Quantity | INTEGER | Yes | Số lượng |
| LotNumber | VARCHAR(100) | Yes | Số lô |
| ExpirationDate | DATE | Yes | HSD |
| UnitPriceVAT | DECIMAL(15,2) | Yes | NEW – Đơn giá VAT |
| TotalAmount | DECIMAL(15,2) | Yes | Thành tiền |
| EnteredBy | UUID FK | Yes | References Account |

---

## 4. Business Rules

### 4.1 Drug Auto-Classification

When a Drug record is created or updated, the system automatically sets Category based on RegistrationNumber prefix:

| Category | Issued By | RegistrationNumber Prefix | Example |
|---|---|---|---|
| MEDICINE (Thuốc) | Drug Administration of Vietnam | VN, VD | VD-12345-12 |
| SUPPLEMENT (Thực phẩm chức năng) | Food Safety Authority | /YT-CBTC, /YT-CNTC, /ATTP-XNCB | 0001/YT-CBTC |
| COSMETIC (Mỹ phẩm) | Drug Administration of Vietnam | /MP-QLD, /CBMP-QLD | 123/MP-QLD |

### 4.2 Retail Price Calculation (5-Tier Markup)

Source: SOP 01-GPP, Section IV.3 – Giá bán lẻ thuốc. Retail price must not exceed the maximum calculated using VATPrice per smallest packaging unit:

| Tier | VATPrice per Smallest Unit | Maximum Markup | Formula |
|---|---|---|---|
| 1 | <= 1,000 VND | 15% | RetailPrice = VATPrice × 1.15 |
| 2 | 1,001 – 5,000 VND | 10% | RetailPrice = VATPrice × 1.10 |
| 3 | 5,001 – 100,000 VND | 7% | RetailPrice = VATPrice × 1.07 |
| 4 | 100,001 – 1,000,000 VND | 5% | RetailPrice = VATPrice × 1.05 |
| 5 | > 1,000,000 VND | 2% | RetailPrice = VATPrice × 1.02 |

> **Important:** This calculation must be performed automatically when a WarehouseReceipt is created. Staff may view but must not override the computed RetailPrice after Manager approval.

### 4.3 Shelf-Life Validation

- Minimum remaining shelf life at time of receipt: > 12 months.
- System calculates: ExpirationDate – InspectionDate > 365 days.
- If this check fails, ShelfLifeValid = FALSE and the inspection report cannot be approved.
- System must display the remaining shelf life in days on the inspection form.

### 4.4 SĐK Validation

- System checks that RegistrationNumber format matches one of the known patterns (VN, VD, /YT-*, /ATTP-*, /MP-QLD, /CBMP-QLD).
- If format is unrecognized, SdkValid = FALSE and a warning is shown.
- Staff may proceed with inspection but must note the discrepancy. Approval is blocked unless Manager overrides.

### 4.5 Inventory Stock Update

- InventoryStock.CurrentQuantity is ONLY updated when a WarehouseReceipt (BM.04) is saved successfully.
- Update formula: CurrentQuantity = CurrentQuantity + DrugBatch.QuantityReceived.
- If an InspectionReport is rejected, stock is NOT updated.
- System must display a low-stock alert on the dashboard when CurrentQuantity <= MinimumThreshold.

### 4.6 Record Immutability

- BatchNumber and ExpirationDate on InspectionReport must not be editable after Manager approval. VATPrice on InspectionReport must not be editable after Manager approval. WarehouseReceipt fields are auto-populated from InspectionReport and are immutable by design.
- ReceivingLogbook entries are immutable after creation.
- DrugNameSnapshot and Concentration in ReceivingLogbook must be copied at the time of creation and never updated even if the Drug master record changes.

### 4.7 Record Retention

- All drug procurement and quality control records must be retained for at least 1 year after the drug's expiration date (per SOP 01, Section VI).
- The system must not allow deletion of any record within this retention window.
- Antibiotic and controlled substance data must be backed up monthly to an exportable format (Excel).

---

## 5. Functional Requirements

### F1 – Authentication & Authorization

#### F1.1 Login

- All users must authenticate with username and password before accessing any page.
- Session expires after a configurable period of inactivity (default: 60 minutes).
- Failed login attempts must be logged.

#### F1.2 Role-Based Access Control

| Feature | Pharmacist | Warehouse Staff | Manager |
|---|---|---|---|
| View Inventory Dashboard | Yes | Yes | Yes |
| Create BM.01 (Forecast) | Yes | Yes | No |
| Approve/Reject BM.01 lines | No | No | Yes |
| Create BM.02 (Purchase Order) | Yes | No | No |
| Create DrugBatch | No | Yes | No |
| Create BM.03 (Inspection Report) | No | Yes | No |
| Approve/Reject BM.03 | No | No | Yes |
| Create BM.04 (Warehouse Receipt) | No | Yes | No |
| View BM.05 (Logbook) | Yes | Yes | Yes |
| Manage Drug Catalog | Yes | No | Yes |
| Manage Suppliers | Yes | No | Yes |
| Manage Users | No | No | Yes |

### F2 – Master Data Management

#### F2.1 Drug Catalog

- Pharmacist and Manager can create, view, edit, and deactivate Drug records.
- System automatically derives Category from RegistrationNumber on save.
- DosageForm selection determines which visual inspection checklist is shown in BM.03.
- Deactivated drugs cannot be selected in new forms but existing references are preserved.

#### F2.2 Supplier Registry

- Pharmacist and Manager can create, view, edit, and deactivate Supplier records.
- Supplier must have a valid pharmaceutical distribution license number.

### F3 – Inventory Stock

#### F3.1 Stock Initialization

- Manager or authorized staff can set initial CurrentQuantity and MinimumThreshold per drug when the system is first set up or when a new drug is added to the catalog.

#### F3.2 Low-Stock Alerts

- Dashboard must visually highlight any drug where CurrentQuantity <= MinimumThreshold.
- Pharmacist and Warehouse Staff see a suggested list of drugs to include in the next procurement forecast.

### F4 – BM.01: Drug Procurement Forecast

#### F4.1 Create Forecast (Pharmacist / Warehouse Staff)

- User opens the Requisition Form.
- System pre-populates suggested drugs from low-stock alert list (optional, user can add others).
- For each drug line, user enters: Drug (from catalog), Quantity, Unit, PackagingSpec (optional), preferred Supplier (optional), and Reason (optional unless unusual spike).
- User submits the form. System saves DrugRequisition header with OverallStatus = PENDING and all DrugRequisitionItems with ApprovalStatus = PENDING.
- Manager receives an in-system notification.

#### F4.2 Approve/Reject Forecast Lines (Manager)

- Manager views list of forecasts with status PENDING.
- Manager opens a forecast and sees all line items.
- Manager can Approve or Reject each line individually.
- If rejecting, Manager must provide a RejectionReason.
- System updates each line's ApprovalStatus.
- OverallStatus is automatically recalculated: ALL_APPROVED → APPROVED; ALL_REJECTED → REJECTED; MIX → PARTIALLY_APPROVED.
- Pharmacist receives in-system notification of the outcome.

### F5 – BM.02: Purchase Order

#### F5.1 Create Purchase Order (Pharmacist)

- Pharmacist selects an approved or partially approved DrugRequisition.
- System groups APPROVED DrugRequisitionItems by their preferred Supplier.
- Pharmacist reviews and adjusts groupings if needed, then creates one Order per Supplier.
- For each order, user confirms: ExpectedDeliveryDate and any notes.
- Order is saved with Status = DRAFT.

#### F5.2 Send Order

- Pharmacist marks Order as SENT after communicating with supplier (phone/email/direct).

#### F5.3 Export Purchase Order

- System generates a printable/downloadable PDF or digital copy of BM.02.D.01.
- Form must include: hospital header, date, drug name, concentration, unit, packaging spec, quantity, supplier, and signature fields (Người dự trù).

### F6 – Drug Batch Registration

- Warehouse Staff selects a SENT Order that has arrived.
- Creates a DrugBatch for each drug lot received, entering: LotNumber, ManufactureDate, ExpirationDate, QuantityReceived, StorageCondition, InvoiceNumber, InvoiceDate. (Note: SupplierID is derived from the Order, not entered separately.)
- QuantityReceived may differ from the ordered quantity.
- Order Status is updated to RECEIVED or PARTIALLY_RECEIVED accordingly.

### F7 – BM.03: Receiving Inspection Report

#### F7.1 Create Inspection Report (Warehouse Staff)

- Staff selects a DrugBatch that has no inspection report yet.
- System auto-populates drug and batch information from DrugBatch and Drug records.
- System runs automatic validations and displays results:
  - SĐK Validity: checks RegistrationNumber format → sets SdkValid.
  - Shelf Life: checks ExpirationDate – today > 365 days → sets ShelfLifeValid. Displays remaining shelf life in days.
- Staff confirms invoice validity (InvoiceValid checkbox with notes).
- Staff enters visual quality inspection results. The form dynamically shows the relevant checklist based on DosageForm:

| DosageForm | Inspection Checklist Points |
|---|---|
| TABLET | Color (no discoloration), moisture (rattle test), blister integrity (no cracks/chips) |
| CAPSULE | Shell integrity (not torn/open), no moisture (rattle test), no powder in empty slots |
| COATED TABLET | Smooth surface (no cracks/peeling), no sticking when shaken |
| GRANULE | Moisture check (rattle test) |
| CREAM/OINTMENT | Tube intact (not dented/crushed), membrane at tube tip not torn, no leakage |
| SUPPOSITORY | Not melted, intact shape, packaging intact |
| SYRUP | Clear (no sediment), no crystallization, no layer separation |
| EYE DROPS | Packaging intact, no discoloration, no sediment |
| ORAL AMPOULE | Uniform color, clear, text readable, 'not for injection' label present |
| INJECTION / INFUSION | No color change, no phase separation, no visible particles; powder form: no caking |
| HERBAL | No mold or insects |

- Staff records StorageConditionMatch and StorageTemperature.
- Staff records VATPrice from invoice.
- Staff submits. Status → PENDING. Manager receives notification.

#### F7.2 Approve/Reject Inspection Report (Manager)

- Manager views list of InspectionReports with Status = PENDING.
- Manager reviews all entered data and validation results.
- If SdkValid or ShelfLifeValid is FALSE, a warning is displayed; Manager can override with written justification.
- Manager clicks Approve or Reject.
- If Reject, RejectionReason is mandatory.
- Warehouse Staff receives notification. If rejected, staff must return goods to supplier.

### F8 – BM.04: Warehouse Receipt

#### F8.1 Create Warehouse Receipt (Warehouse Staff)

- Staff selects an InspectionReport with Status = APPROVED.
- System auto-populates all fields from DrugBatch and InspectionReport.
- System automatically calculates RetailPrice using the 5-tier markup rule from VATPrice.
- Staff may enter optional fields: Barcode, AccountingCode, BHYTPrice.
- Staff submits. System saves WarehouseReceipt.
- System automatically: (a) creates a ReceivingLogbook entry, (b) updates InventoryStock.CurrentQuantity.

#### F8.2 Export Warehouse Receipt

- System generates a printable/downloadable PDF/digital copy of BM.04.D.01 in the official C21 format.
- Form must include all required fields and signature lines for: Người lập, Kế toán, Thủ kho, Phụ trách bộ phận nhập kho.

### F9 – BM.05: Receiving Logbook

- Logbook entries are auto-generated upon WarehouseReceipt creation. No manual input required.
- All users can view the Receiving Logbook, filterable by date range, drug name, supplier, or lot number.
- Logbook entries are immutable. No edit or delete is permitted.
- Export to Excel must be available for compliance backup purposes.

### F10 – Inventory Dashboard & Classification

- Accessible by all roles.
- Shows: drug name, category, current quantity, minimum threshold, dosage form, storage condition.
- Visual alert (red/yellow highlight) for drugs at or below minimum threshold.
- Filter by: Category (MEDICINE / SUPPLEMENT / COSMETIC), DosageForm, StorageCondition, low-stock status.
- Quick-action button: 'Create Forecast' directly from a low-stock drug row.

---

## 6. Non-Functional Requirements

### 6.1 Performance

- Validation responses (SĐK check, shelf-life calculation) must complete within 2 seconds.
- Report status updates and inventory quantity updates must complete within 2 seconds.
- System must support concurrent management of multiple product categories (medicines, supplements, cosmetics, medical supplies).

### 6.2 Security

- All pages except the login page require authentication.
- Role-based access control must be enforced server-side, not just in the UI.
- Approved fields (batch number, expiration date, purchase price) must not be editable after Manager approval – enforced at the API level.
- All API endpoints must validate user role before processing requests.

### 6.3 Data Integrity & Retention

- Drug records in completed transactions (InspectionReport, WarehouseReceipt) must never be hard-deleted from the database.
- Logbook entries are append-only; no update or delete operations are permitted on this table.
- All records must be retained for at least 1 year after the referenced drug's expiration date.
- Monthly Excel backup must be available for antibiotic and controlled substance records.

### 6.4 Compliance & Standardization

- All exported documents must use the official hospital form codes: BM.01.D.01, BM.02.D.01, BM.03.D.01, BM.04.D.01 (Form C21), BM.05.D.01.
- BM.04 must conform to Form C21 per Ministry of Finance Decision 19/2006/QĐ-BTC and Circular 185/2010/TT-BTC.
- The system must comply with GPP standards (Thông tư 02/2018/TT-BYT and 36/2018/TT-BYT).

### 6.5 Usability & Notifications

- Each dosage form must have a dedicated visual inspection checklist – not a generic text field.
- Notifications must be sent in-system immediately when: (a) Manager approves or rejects a BM.01 line, (b) Manager approves or rejects a BM.03 report.
- The dashboard must clearly indicate remaining shelf life in human-readable format (e.g. '14 months 3 days remaining').

---

## 7. End-to-End Business Flow

The following describes the complete operational cycle from low-stock detection to inventory update:

| Step | Actor | Action | System Response | Next Trigger |
|---|---|---|---|---|
| 1 | System | Detects CurrentQuantity <= MinimumThreshold | Shows low-stock alert on dashboard | Pharmacist/Staff sees alert |
| 2 | Pharmacist / Staff | Creates Procurement Forecast (BM.01) | Saves forecast with Status=PENDING; notifies Manager | Manager reviews |
| 3 | Manager | Approves/Rejects each drug line | Updates ApprovalStatus per line; recalculates OverallStatus; notifies Pharmacist | Pharmacist acts on approved lines |
| 4 | Pharmacist | Creates Purchase Orders (BM.02) grouped by supplier | Saves orders; allows export/print | Orders sent to suppliers |
| 5 | Supplier | Delivers goods to warehouse | (External event) | Staff registers batches |
| 6 | Warehouse Staff | Creates DrugBatch for each received lot | Records batch with invoice and lot info; updates Order status | Ready for inspection |
| 7 | Warehouse Staff | Creates Inspection Report (BM.03) | Auto-validates SĐK and shelf life; presents dosage-specific checklist; sets Status=PENDING; notifies Manager | Manager reviews |
| 8 | Manager | Approves or Rejects inspection report | Sets Status=APPROVED or REJECTED; notifies Staff | If approved: proceed to receipt |
| 9 | Warehouse Staff | Creates Warehouse Receipt (BM.04) | Auto-calculates retail price; saves receipt; triggers stock update and logbook entry | Inventory updated |
| 10 | System | Auto-creates Logbook Entry (BM.05) | Immutable audit record created | Cycle complete; dashboard updated |

---

## 8. Official Form Field Reference

This section documents the exact fields required on each exported form to ensure compliance with SOP 01-GPP.

### BM.01.D.01 – Drug Procurement Forecast (Dự trù thuốc)

| Column | Vietnamese Label | Required |
|---|---|---|
| No. | STT | Yes |
| Drug Name | Tên thuốc | Yes |
| Concentration/Content | Nồng độ - hàm lượng | Yes |
| Unit | Đơn vị tính | Yes |
| Packaging Spec | Quy cách | Yes |
| Quantity | Số lượng | Yes |
| Supplier | Nhà cung cấp | No |
| Reason for Forecast | Lý do dự trù | Conditional |
| Notes | Ghi chú | No |

Signature fields: Department Head (Trưởng khoa) + Person requesting (Người dự trù)

### BM.02.D.01 – Purchase Order (Đơn đặt hàng)

| Column | Vietnamese Label | Required |
|---|---|---|
| No. | STT | Yes |
| Drug Name | Tên thuốc | Yes |
| Concentration/Content | Nồng độ - hàm lượng | Yes |
| Unit | Đơn vị tính | Yes |
| Packaging Spec | Quy cách | Yes |
| Quantity | Số lượng | Yes |
| Supplier | Nhà cung cấp | Yes |

Signature field: Person ordering (Người dự trù)

### BM.03.D.01 – Receiving Inspection Report (Biên bản kiểm nhập thuốc)

| Column | Vietnamese Label | Required |
|---|---|---|
| No. | STT | Yes |
| Drug Name | Tên thuốc | Yes |
| Active Ingredient | Hoạt chất | Yes |
| Content | Hàm lượng | Yes |
| Unit | ĐVT | Yes |
| Quantity | Số lượng | Yes |
| Lot Number | Số lô | Yes |
| Expiration Date | Hạn dùng | Yes |
| Receipt Date | Ngày nhập | Yes |
| Registration No. | SĐK | Yes |
| Packaging Spec | Quy cách đóng gói | Yes |
| VAT Import Price | Giá nhập VAT | Yes |
| Manufacturer / Country | Hãng / Nước SX | Yes |
| Visual Quality Assessment | Đánh giá chất lượng cảm quan | Yes |
| Storage Temperature | Nhiệt độ BQ | Yes |

Signature fields (5 required per SOP): Chairman (Chủ tịch HĐ) + Vice Chairman + Secretary (Thư ký) + Warehouse Keeper (Thủ kho) + Members (Các ủy viên)

> **MVP Simplification:** For the initial system release, a single Manager approval may substitute for the 5-member council signature requirement. This must be documented as a known deviation from SOP and revisited in a future release.

### BM.04.D.01 – Warehouse Receipt (Phiếu nhập kho) – Form C21

Per QĐ 19/2006/QĐ-BTC and Circular 185/2010/TT-BTC. Header fields: supplier name, address, tax code; invoice number and date; warehouse location; VAT rate.

| Column | Vietnamese Label | Required |
|---|---|---|
| No. | STT | Yes |
| Barcode | Mã vạch | No |
| Code | Mã số | No |
| Name / Brand / Spec | Tên, nhãn hiệu, quy cách, phẩm chất | Yes |
| Content | Hàm lượng | Yes |
| Unit | ĐVT | Yes |
| Manufacturer / Country | Hãng SX / Nước SX | Yes |
| Actual Quantity Received | SL thực nhập | Yes |
| Import Price | Giá nhập | Yes |
| VAT Price | Giá VAT | Yes |
| Total Amount | Thành tiền | Yes |
| BHYT Price | Giá BHYT | No |
| Profit Rate | Tỷ lệ lãi | Auto |
| Retail Price | Giá bán | Auto |

Signature fields: Creator (Người lập) + Accountant (Kế toán) + Warehouse Keeper (Thủ kho) + Section Head (Phụ trách bộ phận nhập kho)

### BM.05.D.01 – Receiving Logbook (Sổ kiểm nhập thuốc/hóa chất/vật dụng y tế tiêu hao)

| Column | Vietnamese Label | Required |
|---|---|---|
| Document No. | Số chứng từ | Yes |
| Invoice Date | Ngày HĐ | Yes |
| Invoice Symbol | Ký hiệu HĐ | No |
| Invoice Number | Số HĐ | Yes |
| Voucher Number | Số phiếu | No |
| Supplier | Nhà CC | Yes |
| Drug Name / Content | Tên thuốc - hàm lượng | Yes |
| Unit | ĐVT | Yes |
| Quantity | Số lượng | Yes |
| Lot Number | Số lô | Yes |
| Expiration Date | HSD | Yes |
| Unit Price VAT | Đơn giá VAT | Yes |
| Total Amount | Thành tiền | Yes |

---

## 9. Changes from Original System Design

The following table summarizes all additions and modifications compared to the original weekly report design, based on analysis of the actual SOP 01-GPP PDF and operational flow review:

| # | Change | Type | Reason |
|---|---|---|---|
| 1 | Added InventoryStock entity with CurrentQuantity and MinimumThreshold | New Entity | Inventory must exist before BM.01. Forecasts are triggered by low stock, not from an empty warehouse. |
| 2 | Moved Inventory module to Phase 3 (before BM.01) | Order Change | BM.01 form selects drugs from existing stock – cannot function without it. |
| 3 | Split DrugRequisition into header + DrugRequisitionItem | Model Split | SOP 01 requires Manager to approve/reject each drug line individually, not the entire forecast. |
| 4 | Added OverallStatus derived field on DrugRequisition | New Field | Needed to show overall forecast state when some lines approved and others rejected. |
| 5 | Reason field on DrugRequisitionItem is conditional, not required | Rule Change | Per SOP note: routine restocking needs no reason; unusual spikes require justification. |
| 6 | Split Order into header + OrderItem | Model Split | One forecast may generate multiple orders across different suppliers. Line items needed per order. |
| 7 | Added ExpectedDeliveryDate to Order | New Field | Needed to track when goods are expected so Warehouse Staff know when to prepare for receiving. |
| 8 | Added InvoiceNumber and InvoiceDate to DrugBatch | New Fields | BM.03 requires invoice validation; these fields must be on the batch, not just the inspection report. |
| 9 | Added StorageTemperature and VATPrice to InspectionReport | New Fields | BM.03.D.01 actual form includes these columns; original design omitted them. |
| 10 | Added Barcode, AccountingCode, BHYTPrice to WarehouseReceipt | New Fields | BM.04.D.01 is Form C21 per Ministry of Finance; these columns are required in the official template. |
| 11 | Added retail price auto-calculation with 5-tier markup logic | New Feature | SOP 01 Section IV.3 defines exact markup tiers; system must enforce them automatically. |
| 12 | Expanded ReceivingLogbook with 6 additional fields | Model Update | BM.05.D.01 actual form has more columns than the original data model captured. |
| 13 | System updates InventoryStock after BM.04 approval | New Trigger | Completes the operational cycle: receive goods → inspect → enter warehouse → stock increases. |
| 14 | Orders are grouped per supplier from approved forecast lines | Flow Change | 1 forecast → multiple suppliers → multiple orders. Grouping logic must be built into BM.02 creation. |
| 15 | Dosage-form-specific inspection checklist in BM.03 | New Feature | SOP 01 Section V.2.1 defines different inspection criteria for each dosage form. |
| 16 | MVP note on 5-signature council vs single Manager approval | Compliance Note | SOP requires Hội đồng kiểm nhập (5 signatories). MVP simplifies to 1 Manager; must be flagged. |
| 17 | Removed DrugBatch.SupplierID | Field Removed | Redundant – SupplierID is derivable via DrugBatch.OrderID → Order.SupplierID. |
| 18 | Removed InventoryStock.StorageCondition | Field Removed | Redundant – StorageCondition already exists on Drug entity from manufacturer label. |
| 19 | Removed WarehouseReceipt.BatchID | Field Removed | Redundant – BatchID is derivable via WarehouseReceipt.ReportID → InspectionReport.BatchID. |
| 20 | Removed WarehouseReceipt.VATPrice | Field Removed | Redundant – VATPrice is already on InspectionReport; WarehouseReceipt reads it via ReportID. |

---

## Appendix – Glossary

| Term | Definition |
|---|---|
| SOP | Standard Operating Procedure – Quy trình thao tác chuẩn |
| GPP | Good Pharmacy Practice – Thực hành tốt nhà thuốc |
| SĐK | Số đăng ký – Drug Registration Number issued by the Ministry of Health |
| BM.01.D.01 | Drug Procurement Forecast form – Dự trù thuốc |
| BM.02.D.01 | Purchase Order form – Đơn đặt hàng |
| BM.03.D.01 | Receiving Inspection Report – Biên bản kiểm nhập thuốc |
| BM.04.D.01 | Warehouse Receipt – Phiếu nhập kho (Form C21) |
| BM.05.D.01 | Receiving Logbook – Sổ kiểm nhập thuốc |
| HSD | Hạn sử dụng – Expiration date |
| ĐVT | Đơn vị tính – Unit of measurement |
| VATPrice | Invoice price inclusive of VAT – Giá nhập VAT |
| Thặng số bán lẻ | Retail markup percentage applied to import price per SOP 01 tier rules |
| BHYT | Bảo hiểm y tế – National Health Insurance |
| Tổ cung ứng | Supply Unit – responsible for procurement planning and ordering |
| Thống kê dược | Pharmaceutical Statistics Unit – manages drug data entry into management software |
| Hội đồng kiểm nhập | Receiving Inspection Council – 5-member body that formally approves drug intake |
| MinimumThreshold | The minimum stock level below which a low-stock alert is triggered and procurement is recommended |

---

*Hanoi Medical University Hospital | Confidential*
