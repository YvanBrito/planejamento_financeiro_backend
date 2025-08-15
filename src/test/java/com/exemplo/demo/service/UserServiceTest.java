package com.exemplo.demo.service;

import com.exemplo.demo.dto.JwtResponse;
import com.exemplo.demo.dto.LoginRequest;
import com.exemplo.demo.model.User;
import com.exemplo.demo.repository.UserRepository;
import com.exemplo.demo.security.CustomUserDetails;
import com.exemplo.demo.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private LoginRequest validRequest;
    private User user;

    @BeforeEach
    void setUp() {
        validRequest = new LoginRequest();
        validRequest.setEmail("user@test.com");
        validRequest.setPassword("password123");

        user = new User();
        user.setEmail("user@test.com");
        user.setPassword("hashedPassword");
    }

    @Test
    void testAuthenticationSuccess() {
        // Simula o comportamento do UserRepository e do PasswordEncoder
        //when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        //when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(customUserDetails)).thenReturn("token-jwt-mocked");

        JwtResponse jwtResponse = userService.authenticate(validRequest);

        assertNotNull(jwtResponse);
        assertEquals("token-jwt-mocked", jwtResponse.getToken());
    }

    @Test
    void testAuthenticationFailure_invalidPassword() {
        // Simula o UserRepository retornando um usuário, mas o PasswordEncoder falhando
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        // Verifica se a exceção correta é lançada
        assertThrows(BadCredentialsException.class, () -> userService.authenticate(validRequest));
    }

    @Test
    void testAuthenticationFailure_userNotFound() {
        // Simula o UserRepository não encontrando o usuário
        when(userRepository.findByEmail("testuser")).thenReturn(null);

        // Verifica se a exceção correta é lançada
        assertThrows(BadCredentialsException.class, () -> userService.authenticate(validRequest));
    }
}
