package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.backend.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository repo, JwtUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        System.out.println("Register-Request erhalten: " + user.getEmail());

        if (user.getEmail() == null || !user.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return ResponseEntity.badRequest().body("Ungültige E-Mail-Adresse");
        }

        if (user.getPassword() == null || user.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Passwort muss mindestens 8 Zeichen lang sein");
        }

        if (repo.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");

        repo.save(user);
        return ResponseEntity.ok("User registered");
    }



    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return repo.findByEmail(user.getEmail())
                .filter(u -> encoder.matches(user.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole())) 
                .orElse("Invalid credentials");
    }
}
