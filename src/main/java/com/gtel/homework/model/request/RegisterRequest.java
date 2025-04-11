package com.gtel.homework.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String phoneNumber;
    private String password;
}
