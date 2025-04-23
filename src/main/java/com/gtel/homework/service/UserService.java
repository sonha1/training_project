package com.gtel.homework.service;

import com.gtel.homework.common.Const;
import com.gtel.homework.domains.OtpDomain;
import com.gtel.homework.dto.auth.UserPrincipal;
import com.gtel.homework.entity.User;
import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.ConfirmOtpRegisterRequest;
import com.gtel.homework.model.request.LoginRequest;
import com.gtel.homework.model.request.LoginResponse;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.model.response.RegisterResponse;
import com.gtel.homework.redis.entities.RegisterUserEntity;
import com.gtel.homework.repository.UserRepository;
import com.gtel.homework.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService extends BaseService {
    private final OtpDomain otpDomain;
    private final UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.prefix:Bearer}")
    private String prefixToken;

    public UserService(OtpDomain otpDomain, UserRepository userRepository) {
        this.otpDomain = otpDomain;
        this.userRepository = userRepository;
    }

    public RegisterResponse resendOtp(String transactionId) throws ApplicationException {
        RegisterUserEntity registerUser = this.validateTransactionId(transactionId);
        String phoneNumber = registerUser.getData().getPhoneNumber();
        log.info("[resendOtp] - Resend OTP with phone {} START", phoneNumber);
        otpDomain.genOtpWhenUserResend(registerUser);
        log.info("[resendOtp] - Resend OTP with phone {} DONE", phoneNumber);
        return new RegisterResponse(registerUser);
    }

    public void confirmRegisterOtp(ConfirmOtpRegisterRequest request) {
        this.validateConfirmRequest(request);
        RegisterUserEntity registerUser = this.validateTransactionId(request.getTransactionId());
        String phoneNumber = registerUser.getData().getPhoneNumber();
        log.info("[confirmRegisterOtp] - Confirm OTP with phone {} START", phoneNumber);
        this.otpDomain.confirmOTP(registerUser, request.getOtp());
        User user = new User(registerUser.getData());
        this.userRepository.save(user);
        this.otpDomain.removeCacheRegister(registerUser);
        log.info("[confirmRegisterOtp] - Confirm OTP with phone {} END", phoneNumber);
    }

    protected RegisterUserEntity validateTransactionId(String transactionId) {
        log.info("[validateTransactionId] START validate transactionId {}", transactionId);
        Optional<RegisterUserEntity> registerUserOpt = this.otpDomain.getOtpByTransactionId(transactionId);
        if (registerUserOpt.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Transaction id is not exists!");
        }
        log.info("[validateTransactionId] END validate transactionId {}", transactionId);
        return registerUserOpt.get();
    }

    protected void validateConfirmRequest(ConfirmOtpRegisterRequest request) {
        if (StringUtils.isNullOrEmpty(request.getTransactionId())) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Transaction id is invalid");
        }
        if (StringUtils.isNullOrEmpty(request.getOtp())) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP is invalid");
        }
    }

    public LoginResponse login(LoginRequest loginRequest) throws ApplicationException {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new ApplicationException(ERROR_CODE.USER_OR_PASS_INCORRECT);
        }

        final String token = jwtTokenUtil.generateToken((UserPrincipal) authentication.getPrincipal());

        final String rfToken = jwtTokenUtil.generateRfToken((UserPrincipal) authentication.getPrincipal());

        return new LoginResponse(token, rfToken, (UserPrincipal) authentication.getPrincipal(), prefixToken);
    }
}
