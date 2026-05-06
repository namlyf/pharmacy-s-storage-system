# 💊 Pharmacy Storage System
> Hệ thống quản lý kho dược phẩm — Bệnh viện Đại học Y Hà Nội

---

## 📋 Giới thiệu

Hệ thống quản lý kho dược phẩm hỗ trợ quy trình nhập kho thuốc, thực phẩm chức năng và mỹ phẩm tại Bệnh viện Đại học Y Hà Nội. Hệ thống số hóa toàn bộ quy trình từ dự trù, đặt hàng, kiểm nhập đến lưu kho — thay thế hoàn toàn quy trình giấy tờ thủ công.

---

## 🛠️ Công nghệ sử dụng

| Thành phần | Công nghệ |
|-----------|-----------|
| Backend | Java 17 + Spring Boot 3.3.5 |
| Frontend | Thymeleaf + HTML/CSS |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA + Hibernate |
| Security | Spring Security |
| Build tool | Maven |
| IDE | VS Code / IntelliJ |

---

## 👥 Phân quyền người dùng

| Role | Chức năng |
|------|-----------|
| `MANAGER` | Duyệt dự trù, duyệt biên bản kiểm nhập, xem báo cáo tổng hợp |
| `PHARMACIST` | Tạo dự trù thuốc, tạo đơn đặt hàng, kiểm nhập |
| `WAREHOUSE_STAFF` | Nhập kho, quản lý lô thuốc, ghi sổ kiểm nhập |

---

## 🗂️ Cấu trúc project

```
pharmacy/
├── src/
│   ├── main/
│   │   ├── java/com/hospital/pharmacy/
│   │   │   ├── config/          # Security config
│   │   │   ├── controller/      # Xử lý request
│   │   │   ├── entity/          # Model/Entity
│   │   │   ├── repository/      # Truy vấn database
│   │   │   ├── service/         # Logic nghiệp vụ
│   │   │   └── dto/             # Data Transfer Object
│   │   └── resources/
│   │       ├── templates/       # Giao diện Thymeleaf
│   │       └── application.properties
└── pom.xml
```

---

## 🗄️ Database

Hệ thống gồm 10 bảng chính:

```
accounts → drug_requisitions → purchase_orders → order_items
                                      ↓
                               drug_batches → inspection_reports
                                                      ↓
                                             warehouse_receipts → receiving_logbooks
suppliers ────────────────────────────┘
drugs ──────────────────────────────────────┘
```

---

## 🔄 Luồng nghiệp vụ chính

```
1. Dược sĩ tạo Dự trù thuốc (BM.01)
        ↓
2. Trưởng khoa duyệt Dự trù
        ↓
3. Dược sĩ tạo Đơn đặt hàng (BM.02) gửi Nhà cung cấp
        ↓
4. Nhân viên kho lập Biên bản kiểm nhập (BM.03)
        ↓
5. Trưởng khoa duyệt Biên bản kiểm nhập
        ↓
6. Nhân viên kho tạo Phiếu nhập kho (BM.04) + Sổ kiểm nhập (BM.05)
```

---

## ⚙️ Hướng dẫn cài đặt

### Yêu cầu
- Java 17+
- MySQL 8.x
- Maven 3.9+

### Các bước cài đặt

**1. Clone project**
```bash
git clone https://github.com/namlyf/pharmacy-s-storage-system.git
cd pharmacy-s-storage-system/pharmacy
```

**2. Tạo database**
```sql
CREATE DATABASE pharmacy_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**3. Cấu hình `application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pharmacy_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh
spring.datasource.username=root
spring.datasource.password=yourpassword
```

**4. Chạy project**
```bash
mvn spring-boot:run
```

**5. Truy cập**
```
http://localhost:8080
```

---

## 📑 Danh sách biểu mẫu

| Mã | Tên biểu mẫu |
|----|--------------|
| BM.01 | Phiếu dự trù thuốc |
| BM.02 | Đơn đặt hàng |
| BM.03 | Biên bản kiểm nhập |
| BM.04 | Phiếu nhập kho |
| BM.05 | Sổ kiểm nhập |

---

