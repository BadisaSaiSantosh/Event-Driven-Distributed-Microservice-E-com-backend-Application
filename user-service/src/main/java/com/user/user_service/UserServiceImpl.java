package com.user.user_service;

import com.user.converter.UserConverter;
import com.user.dto.AuthResponseDto;
import com.user.dto.UserDto;
import com.user.entity.Role;
import com.user.entity.User;
import com.user.repository.UserRepo;
import com.user.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDto login(UserDto userDto) {
        if (userDto == null || userDto.getEmail() == null || userDto.getPassword() == null) {
            throw invalidCredentials();
        }

        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(this::invalidCredentials);
        
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw invalidCredentials();
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());
        UserDto profile = UserConverter.userToDto(user);
        
        return AuthResponseDto.builder()
                .token(token)
                .user(profile)
                .build();
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserConverter.dtoToUser(userDto);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        User savedUser = userRepository.save(user);
        return UserConverter.userToDto(savedUser);
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserConverter::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserConverter.userToDto(user);
    }
}