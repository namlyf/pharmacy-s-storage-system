package com.hospital.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hospital.pharmacy.entity.Account;
import com.hospital.pharmacy.entity.DrugRequisition;
import com.hospital.pharmacy.repository.DrugRequisitionRepository;

@Service
public class DrugRequisitionService {

    private final DrugRequisitionRepository requisitionRepository;

    public DrugRequisitionService(DrugRequisitionRepository requisitionRepository) {
        this.requisitionRepository = requisitionRepository;
    }

    // Lấy tất cả dự trù
    public List<DrugRequisition> getAll() {
        return requisitionRepository.findAll();
    }

    // Lấy dự trù theo người tạo
    public List<DrugRequisition> getByCreatedBy(Account account) {
        return requisitionRepository.findByCreatedBy(account);
    }

    // Lấy dự trù theo trạng thái
    public List<DrugRequisition> getByStatus(DrugRequisition.Status status) {
        return requisitionRepository.findByStatus(status);
    }

    // Tìm theo ID
    public Optional<DrugRequisition> getById(Long id) {
        return requisitionRepository.findById(id);
    }

    // Lưu dự trù
    public DrugRequisition save(DrugRequisition requisition) {
        return requisitionRepository.save(requisition);
    }

    // Duyệt dự trù
    public void approve(Long id) {
        requisitionRepository.findById(id).ifPresent(r -> {
            r.setStatus(DrugRequisition.Status.APPROVED);
            requisitionRepository.save(r);
        });
    }

    // Từ chối dự trù
    public void reject(Long id) {
        requisitionRepository.findById(id).ifPresent(r -> {
            r.setStatus(DrugRequisition.Status.REJECTED);
            requisitionRepository.save(r);
        });
    }

    // Xóa dự trù
    public void delete(Long id) {
        requisitionRepository.deleteById(id);
    }
}