
package com.driveease.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "DriveEase Backend is Running Successfully!";
    }

    @GetMapping("/api/health")
    public String health() {
        return "DriveEase API is Healthy!";
    }
}
