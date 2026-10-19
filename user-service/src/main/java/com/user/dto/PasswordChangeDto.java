package com.user.dto;

import lombok.Data;

@Data
public class PasswordChangeDto {
    private String oldPassword;
    private String newPassword;
}