package com.hospital.pharmacy.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hospital.pharmacy.entity.Account;
import com.hospital.pharmacy.repository.AccountRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Chỉ tạo nếu chưa có
        if (accountRepository.count() == 0) {
            Account manager = new Account();
            manager.setUsername("manager");
            manager.setPasswordHash(passwordEncoder.encode("123456"));
            manager.setFullName("Trưởng Khoa");
            manager.setRole(Account.Role.MANAGER);
            accountRepository.save(manager);

            Account pharmacist = new Account();
            pharmacist.setUsername("duocsi");
            pharmacist.setPasswordHash(passwordEncoder.encode("123456"));
            pharmacist.setFullName("Dược Sĩ");
            pharmacist.setRole(Account.Role.PHARMACIST);
            accountRepository.save(pharmacist);

            Account warehouse = new Account();
            warehouse.setUsername("kho");
            warehouse.setPasswordHash(passwordEncoder.encode("123456"));
            warehouse.setFullName("Nhân Viên Kho");
            warehouse.setRole(Account.Role.WAREHOUSE_STAFF);
            accountRepository.save(warehouse);

            System.out.println("✅ Đã tạo 3 tài khoản test!");
        }
    }
}