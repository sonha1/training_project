package com.gtel.homework.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String fullName;
    private String dateOfBirth;
    private Integer gender;
    private Integer status;
    private String phoneNumber;
    private String password;
    private String country;
    private String avatarUrl;
}
