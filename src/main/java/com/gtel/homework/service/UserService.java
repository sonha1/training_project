package com.gtel.homework.service;

import com.gtel.homework.domains.OtpDomain;
import com.gtel.homework.entity.UserEntity;
import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.ConfirmOtpRegisterRequest;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.model.response.RegisterResponse;
import com.gtel.homework.redis.entities.RegisterUserEntity;
import com.gtel.homework.repository.UserRepository;
import com.gtel.homework.utils.ERROR_CODE;
import com.gtel.homework.utils.PhoneNumberUtils;
import com.gtel.homework.utils.StringUtils;
import com.gtel.homework.utils.USER_STATUS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService extends BaseService {
    private final OtpDomain otpDomain;
    private final UserRepository userRepository;

    public UserService(OtpDomain otpDomain, UserRepository userRepository) {
        this.otpDomain = otpDomain;
        this.userRepository = userRepository;
    }

    public RegisterResponse registerUser(RegisterRequest request) throws ApplicationException {
        this.validateUserRegisterRequest(request);
        String phoneNumber = PhoneNumberUtils.validatePhoneNumber(request.getPhoneNumber());
        log.info("[registerUser] - user register with phone {} START", phoneNumber);
        UserEntity userEntity = userRepository.findByPhoneNumber(phoneNumber);
        if (userEntity != null) {
            log.info("[registerUser] request fail : user already exists with phone {}", phoneNumber);
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "PhoneNumber is already exists");
        }

        RegisterUserEntity otpEntity = otpDomain.genOtpWhenUserRegister(phoneNumber, request.getPassword());
        log.info("[registerUser] - user register with phone {} DONE", request.getPhoneNumber());
        return new RegisterResponse(otpEntity);
    }

    public RegisterResponse resendOtp(String transactionId) throws ApplicationException {
        RegisterUserEntity registerUser = this.validateTransactionId(transactionId);
        log.info("[resendOtp] - Resend OTP with phone {} START", registerUser.getPhoneNumber());
        otpDomain.genOtpWhenUserResend(registerUser);
        log.info("[resendOtp] - Resend OTP with phone {} DONE", registerUser.getPhoneNumber());
        return new RegisterResponse(registerUser);
    }

    public void confirmRegisterOtp(ConfirmOtpRegisterRequest request) {
        this.validateConfirmRequest(request);
        RegisterUserEntity registerUser = this.validateTransactionId(request.getTransactionId());
        log.info("[confirmRegisterOtp] - Confirm OTP with phone {} START", registerUser.getPhoneNumber());
        this.otpDomain.confirmOTP(registerUser, request.getOtp());
        UserEntity user = new UserEntity();
        user.setPhoneNumber(registerUser.getPhoneNumber());
        user.setPassword(registerUser.getPassword());
        user.setStatus(USER_STATUS.ACTIVE);
        this.userRepository.save(user);
        this.otpDomain.removeCacheRegister(registerUser);
        log.info("[confirmRegisterOtp] - Confirm OTP with phone {} END", registerUser.getPhoneNumber());
    }

    protected void validateUserRegisterRequest(RegisterRequest request) throws ApplicationException {
        if (StringUtils.isNullOrEmpty(request.getPhoneNumber())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Phone number is invalid");
        }
        if (StringUtils.isNullOrEmpty(request.getPassword())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Password is invalid");
        }
        StringUtils.validatePassword(request.getPassword());
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

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ApplicationException(ERROR_CODE.USER_NOT_FOUND, "User not found"));
    }
}
