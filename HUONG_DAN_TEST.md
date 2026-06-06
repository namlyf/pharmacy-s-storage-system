# HƯỚNG DẪN KIỂM THỬ HỆ THỐNG QUẢN LÝ KHO DƯỢC PHẨM
## (Tuân thủ Quy trình SOP 01-GPP)

Tài liệu này hướng dẫn chi tiết cách chạy thử nghiệm hệ thống từ đầu đến cuối (End-to-End) và danh sách các trường hợp kiểm thử (Test Cases) quan trọng.

---

### 1. THÔNG TIN TRUY CẬP HỆ THỐNG

| Vai trò | Tài khoản | Mật khẩu | Chức năng chính |
| :--- | :--- | :--- | :--- |
| **Quản lý** | `manager` | `123456` | Phê duyệt dự trù, kiểm nhập, quản lý danh mục. |
| **Dược sĩ** | `pharmacist` | `123456` | Lập dự trù thuốc (BM.01), lập đơn đặt hàng (BM.02). |
| **Nhân viên kho** | `warehouse` | `123456` | Tiếp nhận lô hàng, kiểm nhập (BM.03), nhập kho (BM.04). |

---

### 2. LUỒNG KIỂM THỬ CHI TIẾT (END-TO-END FLOW)

Thực hiện theo 6 bước sau để hoàn thành một chu trình nhập hàng:

#### Bước 1: Khởi tạo Thuốc mới (Vai trò: QUẢN LÝ)
*   **Hành động:** Thêm mới thuốc để kiểm tra tính tự động phân loại.
*   **Dữ liệu nhập:**
    *   Tên thuốc: **Kháng sinh Augmentin 1g**
    *   Số đăng ký (SĐK): **VN-12345-22** (Bắt đầu bằng `VN` -> Hệ thống tự chọn loại là **THUỐC**)
    *   Dạng bào chế: **TABLET** (Viên nén)
    *   Ngưỡng tồn kho tối thiểu: **50**
*   **Kết quả:** Thuốc tạo thành công, tồn kho hiện tại là **0** (Dashboard hiện cảnh báo đỏ).

#### Bước 2: Lập phiếu dự trù BM.01 (Vai trò: DƯỢC SĨ)
*   **Hành động:** Lập kế hoạch nhập hàng.
*   **Dữ liệu nhập:**
    *   Chọn thuốc: **Kháng sinh Augmentin 1g**
    *   Số lượng dự trù: **500** (Viên)
    *   Nhà cung cấp dự kiến: **CPC1**
*   **Kết quả:** Phiếu lưu trạng thái **Chờ duyệt**.

#### Bước 3: Phê duyệt dự trù (Vai trò: QUẢN LÝ)
*   **Thao tác:** Vào menu **Dự trù thuốc (BM.01)** -> Xem chi tiết phiếu -> Nhấn **Phê duyệt biên bản**.
*   **Kết quả:** Trạng thái chuyển thành **Đã phê duyệt**.

#### Bước 4: Tạo đơn đặt hàng BM.02 (Vai trò: DƯỢC SĨ)
*   **Thao tác:** Từ phiếu dự trù đã duyệt, nhấn **Tạo đơn hàng** -> Chuyển trạng thái sang **Gửi đơn hàng**.
*   **Kết quả:** Tạo thành công đơn hàng gửi nhà cung cấp.

#### Bước 5: Kiểm nhập lô hàng BM.03 (Vai trò: NHÂN VIÊN KHO)
*   **Hành động:** Kiểm tra chất lượng thực tế khi hàng về.
*   **Dữ liệu nhập:**
    1.  **Lô hàng:** Số lô: **LOT-AUG-2024**, Hạn dùng: **01/01/2026**, Số hóa đơn: **HD-6688**.
    2.  **Kiểm nhập:** Giá nhập VAT: **15.000đ/viên**, Nhiệt độ: **25.0**, Cảm quan: **Viên nén nguyên vẹn, nhãn mác rõ ràng.**
*   **Kết quả:** Biên bản ở trạng thái **Chờ duyệt**.

#### Bước 6: Nhập kho & Hoàn tất (Vai trò: QUẢN LÝ & KHO)
*   **Thao tác:** Manager duyệt BM.03 -> Warehouse lập **Phiếu nhập kho (BM.04)**.
*   **Xác nhận:** Giá bán lẻ tự tính: **16.050đ** (Lãi 7%). Tồn kho Dashboard cập nhật lên **500**. Sổ kiểm nhập (BM.05) có dòng mới.

---

### 3. DANH SÁCH CÁC TRƯỜNG HỢP KIỂM THỬ (TEST CASES)

| Mã TC | Nhóm | Tên Test Case | Kết quả mong đợi |
| :--- | :--- | :--- | :--- |
| **TC-01** | Bảo mật | Phân quyền Dược sĩ | Không được thấy các nút "Phê duyệt" hoặc menu "Quản lý tài khoản". |
| **TC-02** | Tự động | Phân loại Thuốc | SĐK bắt đầu bằng `VN`, `VD` -> Loại: **MEDICINE** (Thuốc). |
| **TC-03** | Tự động | Phân loại TP Chức năng | SĐK chứa `/ATTP` hoặc `/YT-` -> Loại: **SUPPLEMENT**. |
| **TC-04** | Tự động | Tính giá lẻ Tier 1 | Giá nhập < 1.000đ -> Thặng số tự động cộng **15%**. |
| **TC-05** | Tự động | Tính giá lẻ Tier 3 | Giá nhập 5.001đ - 100.000đ -> Thặng số tự động cộng **7%**. |
| **TC-06** | Nghiệp vụ | Kiểm tra Hạn dùng | Nhập HSD < 12 tháng so với ngày hiện tại -> Báo lỗi/Cảnh báo không đạt. |
| **TC-07** | Nghiệp vụ | Duyệt từng dòng | Phiếu dự trù 2 dòng, duyệt 1 dòng -> Chỉ tạo được đơn hàng cho dòng đã duyệt. |
| **TC-08** | Nghiệp vụ | Gom nhóm đơn hàng | 1 Phiếu dự trù nhiều NCC -> Tự động tách thành nhiều Đơn hàng tương ứng mỗi NCC. |
| **TC-09** | Dữ liệu | Snapshot Nhật ký | Đổi tên thuốc trong Danh mục -> Tên thuốc trong Sổ kiểm nhập (BM.05) KHÔNG được đổi theo. |
| **TC-10** | Giao diện | Checklist cảm quan | Dạng thuốc là CAPSULE (Viên nang) -> Hiện hướng dẫn kiểm tra vỏ nang, độ ẩm. |
| **TC-11** | In ấn | In mẫu C21-HD | Nhấn in BM.04 -> Bản in phải đúng định dạng Phiếu nhập kho của Bộ Tài chính. |

---

### 4. GHI CHÚ CÁC QUY TẮC NGHIỆP VỤ (SOP 01-GPP)

1.  **Ngưỡng tồn kho:** Dashboard hiện màu đỏ/vàng khi `Số lượng hiện tại <= Ngưỡng tối thiểu`.
2.  **Giá bán lẻ:** Tính trên đơn vị nhỏ nhất (ví dụ: Viên).
3.  **Sổ kiểm nhập:** Là dữ liệu pháp lý, không được phép Sửa hoặc Xóa sau khi đã nhập kho.
4.  **SĐK hợp lệ:** VN, VD, /YT-CBTC, /YT-CNTC, /ATTP-XNCB, /MP-QLD, /CBMP-QLD.
