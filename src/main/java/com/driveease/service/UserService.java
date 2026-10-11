package com.driveease.service;

import com.driveease.model.User;
import com.driveease.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        String email = user.getEmail() == null ? "" : user.getEmail().trim().toLowerCase();
        String name = user.getName() == null ? "" : user.getName().trim();
        if (name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new IllegalArgumentException("Enter a valid email address");
        if (user.getPassword() == null || user.getPassword().length() < 8)
            throw new IllegalArgumentException("Password must contain at least 8 characters");
        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("Email is already registered");
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Never allow public registration to assign ADMIN.
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        if (email == null) return null;
        return userRepository.findByEmail(email.trim().toLowerCase()).orElse(null);
    }
}
