package com.gtel.homework.api;


import com.gtel.homework.dto.auth.UserPrincipal;
import com.gtel.homework.entity.User;
import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.LoginRequest;
import com.gtel.homework.model.request.LoginResponse;
import com.gtel.homework.service.UserService;
import com.gtel.homework.utils.ERROR_CODE;
import com.gtel.homework.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Operation(description = "Đăng nhập")
    @PostMapping("")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
