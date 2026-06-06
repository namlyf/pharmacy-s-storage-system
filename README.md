# Pharmacy Inventory Management System (Hệ thống Quản lý Kho Dược)

## 🏥 Project Overview
Hệ thống quản lý kho dược phẩm cho Bệnh viện Đại học Y Hà Nội, được thiết kế để chuẩn hóa quy trình tiếp nhận thuốc theo tiêu chuẩn **SOP 01-GPP**. Hệ thống số hóa toàn bộ các biểu mẫu từ dự trù, đặt hàng đến kiểm nhập và ghi sổ tự động.

## 🚀 Key Features (10 Phases Completed)
Hệ thống bao gồm đầy đủ các module nghiệp vụ:
1.  **Quản lý Danh mục:** Thuốc (tự động phân loại), Nhà cung cấp, Tài khoản người dùng.
2.  **Quản lý Tồn kho:** Theo dõi số lượng thực tế, thiết lập ngưỡng dự trữ tối thiểu và cảnh báo hàng sắp hết.
3.  **Dự trù thuốc (BM.01):** Lập phiếu dự trù, hỗ trợ phê duyệt từng dòng (per-line approval) bởi Manager.
4.  **Đơn đặt hàng (BM.02):** Tự động gom nhóm thuốc theo nhà cung cấp để tạo đơn hàng.
5.  **Quản lý Lô & Hạn dùng:** Đăng ký thông tin Số lô, NSX, HSD khi hàng về.
6.  **Biên bản kiểm nhập (BM.03):** Kiểm tra tính hợp lệ của SĐK, hạn dùng (>12 tháng) và chất lượng cảm quan.
7.  **Phiếu nhập kho (BM.04):** Tự động tính giá bán lẻ theo thặng số 5 tầng (SOP 01) và in mẫu C21-HD.
8.  **Sổ kiểm nhập (BM.05):** Tự động ghi nhật ký nhập kho bất biến (audit log).
9.  **Dashboard:** Biểu đồ trực quan, thống kê phân loại thuốc và danh sách hành động khẩn cấp.

## 🛠 Tech Stack
-   **Backend:** Java 17, Spring Boot 3.5.14
-   **Security:** Spring Security (RBAC với các quyền: MANAGER, PHARMACIST, WAREHOUSE_STAFF)
-   **Database:** MySQL 8.0
-   **Frontend:** Thymeleaf, Bootstrap 5, Bootstrap Icons
-   **Build Tool:** Maven

## 💻 Getting Started

### Prerequisites
-   JDK 17 or higher
-   MySQL 8.0
-   Maven

### Installation
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/namlyf/pharmacy-s-storage-system.git
    cd pharmacy-s-storage-system
    ```

2.  **Database Setup:**
    -   Tạo database: `CREATE DATABASE pharmacy_db;`
    -   Cấu hình username/password database trong `src/main/resources/application.properties`.
    -   (Tùy chọn) Nạp dữ liệu mẫu:
        ```bash
        mysql -u root -p pharmacy_db -e "source seed_data.sql"
        ```

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

### Default Credentials
Hệ thống tự động đồng bộ mật khẩu mặc định là **`123456`** cho các tài khoản:
-   **Quản lý:** `manager`
-   **Dược sĩ:** `pharmacist`
-   **Nhân viên kho:** `warehouse`

## 📝 Standards & Conventions
-   **Soft Delete:** Sử dụng thuộc tính `isActive` thay vì xóa vật lý để bảo toàn lịch sử giao dịch.
-   **Auto-classification:** Thuốc được phân loại tự động dựa trên định dạng Số đăng ký (VN, VD, CBMP...).
-   **Pricing Logic:** Áp dụng công thức tính giá bán lẻ tự động dựa trên giá nhập có VAT.

---
*Hanoi Medical University Hospital | Pharmacy Department*
