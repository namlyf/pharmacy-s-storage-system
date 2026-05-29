package com.pharmacy.inventory.config;

import com.pharmacy.inventory.enums.Role;
import com.pharmacy.inventory.model.Account;
import com.pharmacy.inventory.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            seedUser("manager", "123456", "Nguyen Lan Hieu", Role.MANAGER);
            seedUser("pharmacist", "123456", "Can Khanh Linh", Role.PHARMACIST);
            seedUser("warehouse", "123456", "Nguyen Thi Hong Hanh", Role.WAREHOUSE_STAFF);
            System.out.println(">>> Default users synchronized");
        };
    }

    private void seedUser(String username, String password, String fullName, Role role) {
        accountRepository.findByUsername(username).ifPresentOrElse(
            acc -> {
                acc.setPassword(passwordEncoder.encode(password));
                accountRepository.save(acc);
            },
            () -> {
                accountRepository.save(Account.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .fullName(fullName)
                    .role(role)
                    .build());
            }
        );
    }
}