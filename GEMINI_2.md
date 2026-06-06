# Pharmacy Inventory System - Cập nhật Phiên bản 2.0 (Tháng 6/2026)

Tài liệu này tóm tắt các thay đổi về chức năng và quy trình đã được thực hiện để tối ưu hóa hệ thống quản lý kho dược phẩm theo yêu cầu thực tế.

## 1. Hệ thống Phân quyền (RBAC)
- **MANAGER:** Vai trò duy nhất có quyền Phê duyệt/Từ chối các bản Dự trù (BM.01) và Biên bản kiểm nhập tổng (BM.03).
- **PHARMACIST:** Quản lý danh mục thuốc, nhà cung cấp và lập Đơn đặt hàng (BM.02).
- **WAREHOUSE_STAFF:** Đăng ký lô hàng, thực hiện kiểm nhập thực tế và lập Phiếu nhập kho (BM.04).
- **Giao diện:** Đã ẩn/hiện các nút bấm chức năng dựa trên Role của người dùng để đảm bảo đúng quy trình nghiệp vụ.

## 2. Quản lý Đơn đặt hàng (BM.02)
- **Chức năng Xóa:** Cho phép xóa các đơn hàng ở trạng thái `DRAFT` (Nháp) trực tiếp từ danh sách hoặc trang chi tiết.
- **Truy xuất nguồn gốc:**
    - Bổ sung cột **Mã dự trù** liên kết trực tiếp với BM.01 gốc.
    - Bổ sung **Thời gian tạo** chi tiết (Giờ:Phút Ngày/Tháng/Năm) để theo dõi đợt gom hàng.
- **Quản lý đăng ký lô:** Trang chi tiết đơn hàng tự động nhận diện thuốc nào đã được đăng ký lô hàng và hiển thị nhãn "Đã đăng ký lô", ngăn chặn việc nhập trùng.

## 3. Quy trình Kiểm nhập tập trung (BM.03) - Tái cấu trúc lớn
Hệ thống chuyển sang mô hình **Master-Detail** để giảm bớt gánh nặng phê duyệt cho Quản lý.

- **Working List (Danh sách chờ):** 
    - Nhân viên kho khi nhận thuốc sẽ nhặt các lô hàng vào một danh sách chờ tạm thời.
    - Cho phép nhập nhanh Giá VAT và Kết quả cảm quan cho từng lô trong danh sách này.
- **Biên bản tổng:** 
    - Toàn bộ danh sách chờ sẽ được gom lại thành **một Biên bản kiểm nhập duy nhất** khi nhân viên kho nhấn "Gửi biên bản tổng".
    - Manager chỉ cần duyệt một lần cho toàn bộ danh sách thuốc trong biên bản.
- **Cơ chế Từ chối (Reject):** 
    - Nếu biên bản bị từ chối, các lô hàng sẽ được giải phóng hoàn toàn khỏi biên bản đó.
    - Nút "Thêm vào biên bản" sẽ xuất hiện lại trong trang Quản lý lô hàng để thực hiện kiểm nhập lại.

## 4. Quản lý Nhập kho (BM.04)
- **Mẫu C21:** Đã cập nhật mẫu in Phiếu nhập kho (C21-HD) để lấy dữ liệu chính xác từ cấu trúc danh sách thuốc mới.

## 5. Cấu trúc Cơ sở dữ liệu mới
- **Bảng `orders`:** Thêm cột `created_at` (DATETIME).
- **Bảng `inspection_reports`:** Refactor thành bảng Header (không chứa thông tin thuốc cụ thể).
- **Bảng `inspection_items`:** Bảng mới chứa chi tiết kiểm nhập của từng lô thuốc trong một biên bản tổng.

---
*Tài liệu này bổ sung cho file GEMINI.md hiện có.*
