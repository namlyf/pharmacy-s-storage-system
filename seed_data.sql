-- Seed data for Pharmacy Inventory System (Compatibility Version)
-- This script only seeds Suppliers, Drugs and Inventory Stocks.
-- Accounts are handled by DataSeeder.java

USE pharmacy_db;

-- 1. SEED SUPPLIERS (Only if license_number doesn't exist)
INSERT INTO suppliers (supplier_id, supplier_name, license_number, tax_code, address, contact_person, phone, email, active)
SELECT UUID(), 'Cong ty Co phan Duoc pham TW1 (CPC1)', 'GPP-123/CPC1', '0100108318', '87 Nguyen Huy Tuong, Thanh Xuan, Ha Noi', 'Nguyen Van A', '024.38544158', 'contact@cpc1.com', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE license_number = 'GPP-123/CPC1');

INSERT INTO suppliers (supplier_id, supplier_name, license_number, tax_code, address, contact_person, phone, email, active)
SELECT UUID(), 'Cong ty TNHH San xuat Duoc pham Medlac', 'GMP-456/MED', '0102693245', 'KCN Cong nghe cao Hoa Lac, Ha Noi', 'Tran Thi B', '024.33940123', 'info@medlac.com.vn', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE license_number = 'GMP-456/MED');

INSERT INTO suppliers (supplier_id, supplier_name, license_number, tax_code, address, contact_person, phone, email, active)
SELECT UUID(), 'Cong ty Co phan Duoc Hau Giang (DHG)', 'GMP-789/DHG', '1800156801', '288 Bis Nguyen Van Cu, Can Tho', 'Le Van C', '0292.3891433', 'dhgpharma@dhgpharma.com.vn', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE license_number = 'GMP-789/DHG');

INSERT INTO suppliers (supplier_id, supplier_name, license_number, tax_code, address, contact_person, phone, email, active)
SELECT UUID(), 'Cong ty TNHH Astrazeneca Viet Nam', 'GSP-101/AZ', '0315486732', 'Phuong Tan Phong, Quan 7, TP. HCM', 'John Doe', '028.37152637', 'contact@astrazeneca.com', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE license_number = 'GSP-101/AZ');


-- 2. SEED DRUGS (Check by registration_number)
-- Paracetamol
INSERT INTO drugs (drug_id, drug_name, active_ingredient, concentration, dosage_form, registration_number, category, manufacturer, country_of_origin, unit, packaging_spec, storage_condition, status)
SELECT UUID(), 'Paracetamol 500mg', 'Paracetamol', '500mg', 'TABLET', 'VD-21345-14', 'MEDICINE', 'Traphaco', 'Viet Nam', 'Vien', 'Hop 10 vi x 10 vien', 'Noi kho rao, duoi 30 do C', 'ACTIVE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM drugs WHERE registration_number = 'VD-21345-14');

-- Augmentin
INSERT INTO drugs (drug_id, drug_name, active_ingredient, concentration, dosage_form, registration_number, category, manufacturer, country_of_origin, unit, packaging_spec, storage_condition, status)
SELECT UUID(), 'Augmentin 1g', 'Amoxicillin + Acid clavulanic', '875mg/125mg', 'TABLET', 'VN-18234-14', 'MEDICINE', 'GSK', 'Phap', 'Vien', 'Hop 2 vi x 7 vien', 'Noi kho rao, tranh anh sang', 'ACTIVE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM drugs WHERE registration_number = 'VN-18234-14');

-- Insulin
INSERT INTO drugs (drug_id, drug_name, active_ingredient, concentration, dosage_form, registration_number, category, manufacturer, country_of_origin, unit, packaging_spec, storage_condition, status)
SELECT UUID(), 'Insulin Mixtard 30', 'Insulin', '100 IU/ml', 'INJECTION', 'VN-11223-10', 'MEDICINE', 'Novo Nordisk', 'Dan Mach', 'Lo', 'Hop 1 lo 10ml', 'Tu lanh 2-8 do C', 'ACTIVE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM drugs WHERE registration_number = 'VN-11223-10');

-- Amlodipin
INSERT INTO drugs (drug_id, drug_name, active_ingredient, concentration, dosage_form, registration_number, category, manufacturer, country_of_origin, unit, packaging_spec, storage_condition, status)
SELECT UUID(), 'Amlodipin 5mg', 'Amlodipin besylat', '5mg', 'TABLET', 'VD-24567-16', 'MEDICINE', 'Domesco', 'Viet Nam', 'Vien', 'Hop 3 vi x 10 vien', 'Nhiet do duoi 30 do C', 'ACTIVE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM drugs WHERE registration_number = 'VD-24567-16');

-- Enervon
INSERT INTO drugs (drug_id, drug_name, active_ingredient, concentration, dosage_form, registration_number, category, manufacturer, country_of_origin, unit, packaging_spec, storage_condition, status)
SELECT UUID(), 'Enervon', 'Vitamin C + B complex', '500mg', 'TABLET', 'VD-33445-19', 'SUPPLEMENT', 'United Pharma', 'Viet Nam', 'Vien', 'Chai 100 vien', 'Mat, kho', 'ACTIVE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM drugs WHERE registration_number = 'VD-33445-19');

-- Cetaphil
INSERT INTO drugs (drug_id, drug_name, active_ingredient, concentration, dosage_form, registration_number, category, manufacturer, country_of_origin, unit, packaging_spec, storage_condition, status)
SELECT UUID(), 'Cetaphil Gentle Cleanser', 'Glycerin, Cetearyl Alcohol', '500ml', 'OTHER', 'CBMP-001/22', 'COSMETIC', 'Galderma', 'Canada', 'Chai', 'Chai 500ml', 'Mat', 'ACTIVE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM drugs WHERE registration_number = 'CBMP-001/22');


-- 3. SEED INVENTORY STOCKS (Link to drugs above)
INSERT INTO inventory_stocks (stock_id, drug_id, current_quantity, minimum_threshold, storage_location, last_updated)
SELECT UUID(), d.drug_id, 500, 100, 'Ke A1', NOW()
FROM drugs d WHERE d.registration_number = 'VD-21345-14' 
AND NOT EXISTS (SELECT 1 FROM inventory_stocks WHERE drug_id = d.drug_id);

INSERT INTO inventory_stocks (stock_id, drug_id, current_quantity, minimum_threshold, storage_location, last_updated)
SELECT UUID(), d.drug_id, 20, 50, 'Ke B2', NOW()
FROM drugs d WHERE d.registration_number = 'VN-18234-14' 
AND NOT EXISTS (SELECT 1 FROM inventory_stocks WHERE drug_id = d.drug_id);

INSERT INTO inventory_stocks (stock_id, drug_id, current_quantity, minimum_threshold, storage_location, last_updated)
SELECT UUID(), d.drug_id, 150, 20, 'Tu lanh', NOW()
FROM drugs d WHERE d.registration_number = 'VN-11223-10' 
AND NOT EXISTS (SELECT 1 FROM inventory_stocks WHERE drug_id = d.drug_id);

INSERT INTO inventory_stocks (stock_id, drug_id, current_quantity, minimum_threshold, storage_location, last_updated)
SELECT UUID(), d.drug_id, 300, 50, 'Ke A2', NOW()
FROM drugs d WHERE d.registration_number = 'VD-24567-16' 
AND NOT EXISTS (SELECT 1 FROM inventory_stocks WHERE drug_id = d.drug_id);

INSERT INTO inventory_stocks (stock_id, drug_id, current_quantity, minimum_threshold, storage_location, last_updated)
SELECT UUID(), d.drug_id, 50, 10, 'Ke D1', NOW()
FROM drugs d WHERE d.registration_number = 'VD-33445-19' 
AND NOT EXISTS (SELECT 1 FROM inventory_stocks WHERE drug_id = d.drug_id);

INSERT INTO inventory_stocks (stock_id, drug_id, current_quantity, minimum_threshold, storage_location, last_updated)
SELECT UUID(), d.drug_id, 15, 5, 'Ke E1', NOW()
FROM drugs d WHERE d.registration_number = 'CBMP-001/22' 
AND NOT EXISTS (SELECT 1 FROM inventory_stocks WHERE drug_id = d.drug_id);

-- Finalize: Ensure all existing seeded drugs are ACTIVE
UPDATE drugs SET status = 'ACTIVE' WHERE registration_number IN ('VD-21345-14', 'VN-18234-14', 'VN-11223-10', 'VD-24567-16', 'VD-33445-19', 'CBMP-001/22');

COMMIT;
