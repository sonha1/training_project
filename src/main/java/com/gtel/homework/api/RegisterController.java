package com.gtel.homework.api;

import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.model.response.RegisterResponse;
import com.gtel.homework.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/register")
public class RegisterController {
    @Autowired
    private AuthService authService;

    @PostMapping("")
    @Operation(description = "api register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) throws ApplicationException {
        return authService.registerUser(registerRequest);
    }
}
