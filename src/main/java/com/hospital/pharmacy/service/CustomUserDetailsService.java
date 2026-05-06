package com.hospital.pharmacy.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hospital.pharmacy.entity.Account;
import com.hospital.pharmacy.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));

        return new User(
            account.getUsername(),
            account.getPasswordHash(),
            List.of(new SimpleGrantedAuthority(account.getRole().name()))
        );
    }
}