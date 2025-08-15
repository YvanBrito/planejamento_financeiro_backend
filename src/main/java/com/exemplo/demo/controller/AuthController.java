package com.exemplo.demo.controller;

import com.exemplo.demo.dto.*;
import com.exemplo.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request.getName(), request.getEmail(), request.getPassword(), request.getRole());
            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            UserDTO userDTO = userService.authenticate(loginRequest);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
