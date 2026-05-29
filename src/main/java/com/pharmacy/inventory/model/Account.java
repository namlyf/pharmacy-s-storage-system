package com.pharmacy.inventory.model;

import com.pharmacy.inventory.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userID;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = true, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    public Account() {}

    // Getters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public boolean isActive() { return isActive; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setUserID(String userID) { this.userID = userID; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(Role role) { this.role = role; }
    public void setActive(boolean active) { isActive = active; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Account account = new Account();

        public Builder username(String v) { account.username = v; return this; }
        public Builder password(String v) { account.password = v; return this; }
        public Builder fullName(String v) { account.fullName = v; return this; }
        public Builder role(Role v) { account.role = v; return this; }
        public Builder isActive(boolean v) { account.isActive = v; return this; }
        public Account build() { return account; }
    }
}