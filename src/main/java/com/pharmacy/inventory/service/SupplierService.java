package com.pharmacy.inventory.service;

import com.pharmacy.inventory.dto.request.SupplierRequest;
import com.pharmacy.inventory.model.Supplier;
import com.pharmacy.inventory.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllActive() {
        return supplierRepository.findByactiveTrue();
    }

    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    public Supplier getById(String id) {
        return supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found: " + id));
    }

    public Supplier create(SupplierRequest request) {
        if (supplierRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("License number already exists.");
        }

        Supplier supplier = Supplier.builder()
            .supplierName(request.getSupplierName())
            .licenseNumber(request.getLicenseNumber())
            .taxCode(request.getTaxCode())
            .address(request.getAddress())
            .contactPerson(request.getContactPerson())
            .phone(request.getPhone())
            .email(request.getEmail())
            .build();

        return supplierRepository.save(supplier);
    }

    public Supplier update(String id, SupplierRequest request) {
        Supplier supplier = getById(id);

        supplier.setSupplierName(request.getSupplierName());
        supplier.setLicenseNumber(request.getLicenseNumber());
        supplier.setTaxCode(request.getTaxCode());
        supplier.setAddress(request.getAddress());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setActive(request.isActive());

        return supplierRepository.save(supplier);
    }

    public void deactivate(String id) {
        Supplier supplier = getById(id);
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }
}




