package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.DrugRequest;
import com.pharmacy.inventory.enums.Category;
import com.pharmacy.inventory.model.Drug;
import com.pharmacy.inventory.repository.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugRepository drugRepository;
    private final InventoryStockService inventoryStockService;

    public InventoryStockService getInventoryStockService() {
        return inventoryStockService;
    }

    // ── Auto-classify từ RegistrationNumber ──────────────
    public Category classifyFromRegistrationNumber(String regNumber) {
        if (regNumber == null) return Category.MEDICINE;
        String upper = regNumber.toUpperCase();

        if (upper.startsWith("VN") || upper.startsWith("VD")) {
            return Category.MEDICINE;
        } else if (upper.contains("/YT-CBTC") || upper.contains("/YT-CNTC")
                || upper.contains("/ATTP-XNCB")) {
            return Category.SUPPLEMENT;
        } else if (upper.contains("/MP-QLD") || upper.contains("/CBMP-QLD")) {
            return Category.COSMETIC;
        }
        // Default nếu không nhận ra format
        return Category.MEDICINE;
    }

    // ── CRUD ─────────────────────────────────────────────
    public List<Drug> getAllActive() {
        return drugRepository.findByStatus(com.pharmacy.inventory.enums.DrugStatus.ACTIVE);
    }

    public List<Drug> getAll() {
        return drugRepository.findAll();
    }

    public Drug getById(String id) {
        return drugRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin thuốc: " + id));
    }

    public Drug create(DrugRequest request) {
        if (drugRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new RuntimeException("Số đăng ký này đã tồn tại trong hệ thống.");
        }

        Category category = classifyFromRegistrationNumber(request.getRegistrationNumber());

        Drug drug = Drug.builder()
            .drugName(request.getDrugName())
            .activeIngredient(request.getActiveIngredient())
            .concentration(request.getConcentration())
            .dosageForm(request.getDosageForm())
            .registrationNumber(request.getRegistrationNumber())
            .category(category)
            .manufacturer(request.getManufacturer())
            .countryOfOrigin(request.getCountryOfOrigin())
            .unit(request.getUnit())
            .packagingSpec(request.getPackagingSpec())
            .storageCondition(request.getStorageCondition())
            .status(com.pharmacy.inventory.enums.DrugStatus.DRAFT)
            .build();

        Drug savedDrug = drugRepository.save(drug);
        inventoryStockService.initializeStock(savedDrug, request.getMinimumThreshold());
        return savedDrug;
    }

    public Drug update(String id, DrugRequest request) {
        Drug drug = getById(id);
        Category category = classifyFromRegistrationNumber(request.getRegistrationNumber());

        drug.setDrugName(request.getDrugName());
        drug.setActiveIngredient(request.getActiveIngredient());
        drug.setConcentration(request.getConcentration());
        drug.setDosageForm(request.getDosageForm());
        drug.setRegistrationNumber(request.getRegistrationNumber());
        drug.setCategory(category);
        drug.setManufacturer(request.getManufacturer());
        drug.setCountryOfOrigin(request.getCountryOfOrigin());
        drug.setUnit(request.getUnit());
        drug.setPackagingSpec(request.getPackagingSpec());
        drug.setStorageCondition(request.getStorageCondition());

        Drug updatedDrug = drugRepository.save(drug);
        inventoryStockService.updateThreshold(id, request.getMinimumThreshold());
        return updatedDrug;
    }

    public void deactivate(String id) {
        Drug drug = getById(id);
        drug.setStatus(com.pharmacy.inventory.enums.DrugStatus.INACTIVE);
        drugRepository.save(drug);
    }

    public void activate(String id) {
        Drug drug = getById(id);
        drug.setStatus(com.pharmacy.inventory.enums.DrugStatus.ACTIVE);
        drugRepository.save(drug);
    }

    public List<Drug> search(String keyword) {
        return drugRepository.findByDrugNameContainingIgnoreCase(keyword);
    }
}




