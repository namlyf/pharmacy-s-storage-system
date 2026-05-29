package com.pharmacy.inventory.service;

import com.pharmacy.inventory.model.Account;
import com.pharmacy.inventory.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
            account.getUsername(),
            account.getPassword(),
            account.isActive(),
            true, true, true,
            List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
        );
    }
}