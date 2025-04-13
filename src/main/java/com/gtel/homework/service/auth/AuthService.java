package com.gtel.homework.service.auth;

import com.gtel.homework.common.Const;
import com.gtel.homework.domains.OtpDomain;
import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.model.response.RegisterResponse;
import com.gtel.homework.redis.entities.RegisterUserEntity;
import com.gtel.homework.repository.UserRepository;
import com.gtel.homework.utils.ERROR_CODE;
import com.gtel.homework.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpDomain otpDomain;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }

    public RegisterResponse registerUser(RegisterRequest request) throws ApplicationException {
        this.validateUserRegisterRequest(request);
        log.info("[registerUser] - user register with phone {} START", request.getPhoneNumber());
        boolean isUserExisted = userRepository.existsByPhoneNumberOrEmailOrUsername(request.getPhoneNumber(), request.getEmail(), request.getUsername());
        if (isUserExisted) {
            log.info("[registerUser] request fail : user already exists with phone: {} or email: {} or username : {}", request.getPhoneNumber(), request.getEmail(), request.getUsername());
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, Const.MessageCode.USER_ALREADY_EXISTS);
        }
        RegisterUserEntity otpEntity = otpDomain.genOtpWhenUserRegister(request);
        log.info("[registerUser] - user register with phone {} DONE", request.getPhoneNumber());
        return new RegisterResponse(otpEntity);
    }

    protected void validateUserRegisterRequest(RegisterRequest request) throws ApplicationException {
        if (StringUtils.isNullOrEmpty(request.getPhoneNumber())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Phone number is invalid");
        }
        if (StringUtils.isNullOrEmpty(request.getPassword())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Password is invalid");
        }
        if (StringUtils.isNullOrEmpty(request.getUsername())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Username is invalid");
        }
        if(StringUtils.isNullOrEmpty(request.getEmail())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Email is invalid");
        }

        StringUtils.validatePassword(request.getPassword());
    }
}
