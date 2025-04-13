package com.gtel.homework.api;

import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.ConfirmOtpRegisterRequest;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.model.response.RegisterResponse;
import com.gtel.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/otp/resend/{transactionId}")
    public RegisterResponse resendOtp(@PathVariable("transactionId") String transactionId) {
        return userService.resendOtp(transactionId);
    }

    @PostMapping("/otp/confirm")
    public void confirmRegisterOtp(@RequestBody ConfirmOtpRegisterRequest request) {
        this.userService.confirmRegisterOtp(request);
    }
}
