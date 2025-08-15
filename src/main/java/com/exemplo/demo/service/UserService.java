package com.exemplo.demo.service;

import com.exemplo.demo.dto.JwtResponse;
import com.exemplo.demo.dto.LoginRequest;
import com.exemplo.demo.dto.UserDTO;
import com.exemplo.demo.model.*;
import com.exemplo.demo.repository.*;
import com.exemplo.demo.security.CustomUserDetails;
import com.exemplo.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDTO authenticate(LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new Exception("Usuário não encontrado"));
        return new UserDTO(user.getUsername(), user.getEmail(), jwt);
    }

    public User registerUser(String name, String email, String password, String roleStr) throws Exception {
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email já cadastrado");
        }

        Role.ERole roleName;
        if ("admin".equalsIgnoreCase(roleStr)) {
            roleName = Role.ERole.ROLE_ADMIN;
        } else {
            roleName = Role.ERole.ROLE_USER;
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new Exception("Role não encontrada"));

        User user = User.builder()
                .username(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(new HashSet<>())
                .build();

        user.getRoles().add(role);

        return userRepository.save(user);
    }
}
