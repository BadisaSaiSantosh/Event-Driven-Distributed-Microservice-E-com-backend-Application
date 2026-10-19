package com.user.user_service;

import com.user.dto.AuthResponseDto;
import com.user.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    AuthResponseDto login(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(String id);
    void changePassword(String userId, String oldPassword, String newPassword);
}