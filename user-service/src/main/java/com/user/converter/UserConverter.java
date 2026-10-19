package com.user.converter;

import com.user.entity.User;
import org.springframework.stereotype.Component;
import com.user.dto.UserDto;

@Component
public class UserConverter {
    public static UserDto userToDto(User user) {
        if(user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .fName(user.getFirstName())
                .lName(user.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static User dtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFName());
        user.setLastName(userDto.getLName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}