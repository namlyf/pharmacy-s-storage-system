# Pharmacy Inventory System - Project Instructions

## Project Overview
Hệ thống quản lý kho dược phẩm cho Bệnh viện Đại học Y Hà Nội, tuân thủ quy trình SOP 01-GPP.

## Tech Stack
- **Backend:** Java 17, Spring Boot 3.5.14
- **Database:** MySQL (pharmacy_db)
- **Security:** Spring Security (MANAGER, PHARMACIST, WAREHOUSE_STAFF)
- **Frontend:** Thymeleaf + Bootstrap 5

## Development Progress (Checklist)
- [x] Phase 1: Enums + Account + Auth + Login page
- [x] Phase 2: Drug + Supplier Master Data
- [x] Phase 3: InventoryStock (Current Quantity & Min Threshold)
- [x] Phase 4: BM.01 Procurement Forecast (Dự trù thuốc)
- [x] Phase 5: BM.02 Purchase Order (Đơn đặt hàng)
- [x] Phase 6: DrugBatch (Quản lý lô & Hạn sử dụng)
- [x] Phase 7: BM.03 Inspection Report (Biên bản kiểm nhập)
- [x] Phase 8: BM.04 Warehouse Receipt (Phiếu nhập kho)
- [x] Phase 9: BM.05 Receiving Logbook (Sổ kiểm nhập tự động)
- [x] Phase 10: Inventory Dashboard + Classification

## Coding Conventions
- Sử dụng **Lombok** (`@RequiredArgsConstructor`) cho Dependency Injection trong Controller/Service.
- Entity sử dụng **UUID** cho Primary Key.
- Phân loại thuốc (Category) tự động dựa trên Số đăng ký (Registration Number).
- Luôn cập nhật `InventoryStock` khi tạo mới hoặc cập nhật `Drug`.

## Current Status
Đã hoàn thành toàn bộ 10 Phase phát triển hệ thống Quản lý kho dược phẩm theo tài liệu BA v1.1.
- Hệ thống đã phủ kín quy trình từ Dự trù -> Đặt hàng -> Tiếp nhận lô hàng -> Kiểm nhập -> Nhập kho -> Ghi sổ tự động.
- Dashboard cung cấp cái nhìn tổng quan về tồn kho, cảnh báo hàng sắp hết và phân loại thuốc tự động.
- Các biểu mẫu BM.01 đến BM.05 đã được số hóa hoàn toàn, tuân thủ SOP 01-GPP.

Hệ thống đã sẵn sàng để bàn giao và sử dụng thử nghiệm.
