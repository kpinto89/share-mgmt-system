package org.demo.sharemgmt.service;

import org.demo.sharemgmt.domain.AppUser;
import org.demo.sharemgmt.domain.UserRole;
import org.demo.sharemgmt.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser createUser(String username, String rawPassword, String fullName, UserRole role) {
        String normalizedUsername = username.trim().toLowerCase();
        if (appUserRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new IllegalArgumentException("A user with this username already exists.");
        }
        AppUser appUser = new AppUser(
            normalizedUsername,
            passwordEncoder.encode(rawPassword),
            fullName.trim(),
            role
        );
        return appUserRepository.save(appUser);
    }
}
