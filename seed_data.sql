-- Seed data for Pharmacy Inventory System (Expanded Version)
-- This script adds more sample data if it doesn't exist.

USE pharmacy_db;

-- 1. Seed Accounts (Only if username doesn't exist)
INSERT IGNORE INTO accounts (userid, username, password, full_name, role, is_active, created_at)
VALUES 
(UUID(), 'manager', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'Nguyen Lan Hieu', 'MANAGER', 1, NOW()),
(UUID(), 'pharmacist', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'Can Khanh Linh', 'PHARMACIST', 1, NOW()),
(UUID(), 'warehouse', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'Nguyen Thi Hong Hanh', 'WAREHOUSE_STAFF', 1, NOW());

-- 2. Seed Suppliers (Only if license_number doesn't exist)
INSERT IGNORE INTO suppliers (supplierid, supplier_name, license_number, tax_code, address, contact_person, phone, email, is_active)
VALUES 
(UUID(), 'Cong ty Co phan Duoc pham TW1 (CPC1)', 'GPP-123/CPC1', '0100108318', '87 Nguyen Huy Tuong, Thanh Xuan, Ha Noi', 'Nguyen Van A', '024.38544158', 'contact@cpc1.com', 1),
(UUID(), 'Cong ty TNHH San xuat Duoc pham Medlac', 'GMP-456/MED', '0102693245', 'KCN Cong nghe cao Hoa Lac, Ha Noi', 'Tran Thi B', '024.33940123', 'info@medlac.com.vn', 1),
(UUID(), 'Cong ty Co phan Duoc Hau Giang (DHG)', 'GMP-789/DHG', '1800156801', '288 Bis Nguyen Van Cu, Can Tho', 'Le Van C', '0292.3891433', 'dhgpharma@dhgpharma.com.vn', 1),
(UUID(), 'Cong ty TNHH Astrazeneca Viet Nam', 'GSP-101/AZ', '0315486732', 'Phuong Tan Phong, Quan 7, TP. HCM', 'John Doe', '028.37152637', 'contact@astrazeneca.com', 1);

-- 3. Seed Drugs and Inventory Stocks
DELIMITER //

CREATE PROCEDURE IF NOT EXISTS SeedDrugWithStock(
    IN d_name VARCHAR(200),
    IN d_ingredient VARCHAR(200),
    IN d_conc VARCHAR(100),
    IN d_form VARCHAR(50),
    IN d_reg VARCHAR(50),
    IN d_cat VARCHAR(50),
    IN d_manu VARCHAR(200),
    IN d_origin VARCHAR(100),
    IN d_unit VARCHAR(50),
    IN d_spec VARCHAR(100),
    IN d_storage VARCHAR(200),
    IN s_qty INT,
    IN s_min INT,
    IN s_loc VARCHAR(100)
)
BEGIN
    DECLARE new_drug_id VARCHAR(255);
    IF NOT EXISTS (SELECT 1 FROM drugs WHERE registration_number = d_reg) THEN
        SET new_drug_id = UUID();
        INSERT INTO drugs (drugid, drug_name, active_ingredient, concentration, dosage_form, registration_number, category, manufacturer, country_of_origin, unit, packaging_spec, storage_condition, is_active)
        VALUES (new_drug_id, d_name, d_ingredient, d_conc, d_form, d_reg, d_cat, d_manu, d_origin, d_unit, d_spec, d_storage, 1);
        INSERT INTO inventory_stocks (stockid, drug_id, current_quantity, minimum_threshold, storage_location, last_updated)
        VALUES (UUID(), new_drug_id, s_qty, s_min, s_loc, NOW());
    END IF;
END //

DELIMITER ;

-- --- MEDICINE ---
CALL SeedDrugWithStock('Paracetamol 500mg', 'Paracetamol', '500mg', 'TABLET', 'VD-21345-14', 'MEDICINE', 'Traphaco', 'Viet Nam', 'Vien', 'Hop 10 vi x 10 vien', 'Noi kho rao, duoi 30 do C', 500, 100, 'Ke A1');
CALL SeedDrugWithStock('Augmentin 1g', 'Amoxicillin + Acid clavulanic', '875mg/125mg', 'TABLET', 'VN-18234-14', 'MEDICINE', 'GSK', 'Phap', 'Vien', 'Hop 2 vi x 7 vien', 'Noi kho rao, tranh anh sang', 20, 50, 'Ke B2');
CALL SeedDrugWithStock('Insulin Mixtard 30', 'Insulin', '100 IU/ml', 'INJECTION', 'VN-11223-10', 'MEDICINE', 'Novo Nordisk', 'Dan Mach', 'Lo', 'Hop 1 lo 10ml', 'Tu lanh 2-8 do C', 150, 20, 'Tu lanh chuyen dung');
CALL SeedDrugWithStock('Amlodipin 5mg', 'Amlodipin besylat', '5mg', 'TABLET', 'VD-24567-16', 'MEDICINE', 'Domesco', 'Viet Nam', 'Vien', 'Hop 3 vi x 10 vien', 'Nhiet do duoi 30 do C', 300, 50, 'Ke A2');
CALL SeedDrugWithStock('Metformin 850mg', 'Metformin hydrochlorid', '850mg', 'TABLET', 'VD-11224-18', 'MEDICINE', 'Stada', 'Viet Nam', 'Vien', 'Hop 5 vi x 10 vien', 'Kho rao', 450, 100, 'Ke A2');
CALL SeedDrugWithStock('Crestor 10mg', 'Rosuvastatin', '10mg', 'COATED_TABLET', 'VN-15678-12', 'MEDICINE', 'AstraZeneca', 'Anh', 'Vien', 'Hop 2 vi x 14 vien', 'Tranh anh sang', 10, 30, 'Ke A3');
CALL SeedDrugWithStock('Nexium MUPS 40mg', 'Esomeprazol', '40mg', 'TABLET', 'VN-19876-15', 'MEDICINE', 'AstraZeneca', 'Thuy Dien', 'Vien', 'Hop 2 vi x 7 vien', 'Duoi 30 do C', 80, 20, 'Ke A3');
CALL SeedDrugWithStock('Salbutamol 2mg', 'Salbutamol', '2mg', 'TABLET', 'VD-12345-20', 'MEDICINE', 'Mekophar', 'Viet Nam', 'Vien', 'Hop 10 vi x 10 vien', 'Kho rao', 1000, 200, 'Ke B1');

-- --- SUPPLEMENT ---
CALL SeedDrugWithStock('Enervon', 'Vitamin C + B complex', '500mg', 'TABLET', 'VD-33445-19', 'SUPPLEMENT', 'United Pharma', 'Viet Nam', 'Vien', 'Chai 100 vien', 'Mat, kho', 50, 10, 'Ke D1');
CALL SeedDrugWithStock('Omega 3 Fish Oil', 'DHA + EPA', '1000mg', 'CAPSULE', 'VN-11122-21', 'SUPPLEMENT', 'Nature Bounty', 'USA', 'Vien', 'Chai 200 vien', 'Tranh anh sang', 40, 5, 'Ke D1');

-- --- COSMETIC ---
CALL SeedDrugWithStock('Cetaphil Gentle Cleanser', 'Glycerin, Cetearyl Alcohol', '500ml', 'OTHER', 'CBMP-001/22', 'COSMETIC', 'Galderma', 'Canada', 'Chai', 'Chai 500ml', 'Mat', 15, 5, 'Ke E1');

-- Clean up
DROP PROCEDURE IF EXISTS SeedDrugWithStock;

COMMIT;
