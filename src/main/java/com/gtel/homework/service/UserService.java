package com.gtel.homework.service;

import com.gtel.homework.common.Const;
import com.gtel.homework.domains.OtpDomain;
import com.gtel.homework.entity.User;
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
}
