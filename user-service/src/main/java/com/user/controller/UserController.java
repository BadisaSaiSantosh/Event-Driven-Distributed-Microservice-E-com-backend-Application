package com.user.controller;

import com.user.dto.AuthResponseDto;
import com.user.dto.PasswordChangeDto;
import com.user.dto.UserDto;
import com.user.user_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    // Secured by API Gateway (X-User-Id header injected by Gateway JWT Filter)
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody PasswordChangeDto dto) {
        userService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.login(userDto));
    }
}