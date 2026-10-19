package com.example.user_service;

import com.user.dto.UserDto;
import com.user.dto.AuthResponseDto;
import com.user.entity.User;
import com.user.entity.Role;
import com.user.repository.UserRepo;
import com.user.security.JwtUtil;
import com.user.user_service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id("u123")
                .username("john_doe")
                .email("john@example.com")
                .password("hashedpass")
                .role(Role.USER)
                .build();
    }

    @Test
    void loginReturnsTokenAndProfileForValidCredentials() {
        UserDto credentials = UserDto.builder()
                .email("john@example.com")
                .password("correct-password")
                .build();
        when(userRepository.findByEmail(credentials.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(credentials.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(mockUser.getId(), Role.USER.name())).thenReturn("signed-token");

        AuthResponseDto response = userService.login(credentials);

        assertEquals("signed-token", response.getToken());
        assertEquals(mockUser.getId(), response.getUser().getId());
        assertEquals(mockUser.getEmail(), response.getUser().getEmail());
        verify(passwordEncoder).matches(credentials.getPassword(), mockUser.getPassword());
    }

    @Test
    void loginRejectsUnknownEmail() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.login(UserDto.builder()
                        .email("missing@example.com")
                        .password("password")
                        .build()));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void loginRejectsIncorrectPassword() {
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrong-password", mockUser.getPassword())).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.login(UserDto.builder()
                        .email(mockUser.getEmail())
                        .password("wrong-password")
                        .build()));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById("u123")).thenReturn(Optional.of(mockUser));

        UserDto result = userService.getUserById("u123");

        assertNotNull(result);
        assertEquals("u123", result.getId());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById("999");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateUser_DefaultRoleAssignment() {
        UserDto input = UserDto.builder()
                .fName("Jane")
                .lName("Doe")
                .email("jane@example.com")
                .password("plain-password")
                .build();
        when(passwordEncoder.encode(input.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId("u456");
            return saved;
        });

        UserDto result = userService.createUser(input);

        assertNotNull(result);
        assertEquals("u456", result.getId());
        verify(userRepository).save(argThat(user ->
                user.getRole() == Role.USER && "encoded-password".equals(user.getPassword())));
        verify(userRepository, times(1)).save(any(User.class));
    }
}