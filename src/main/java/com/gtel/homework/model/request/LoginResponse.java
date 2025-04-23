package com.gtel.homework.model.request;

import com.gtel.homework.dto.auth.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UserPrincipal userPrincipal;
    private String prefixToken;
}
