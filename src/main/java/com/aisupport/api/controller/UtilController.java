package com.aisupport.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/util")
@CrossOrigin(origins = "*")
public class UtilController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hash")
    public Map<String, String> generateHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        return Map.of(
                "password", password,
                "hash", hash
        );
    }
}