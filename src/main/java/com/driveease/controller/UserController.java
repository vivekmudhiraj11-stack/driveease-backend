
package com.driveease.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public Map<String, String> getProfile(Authentication authentication) {
        return Map.of(
                "message", "Profile accessed successfully",
                "email", authentication.getName());
    }
}
