package com.gtel.homework;

import com.gtel.homework.domains.OtpDomain;
import com.gtel.homework.entity.UserEntity;
import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.model.response.RegisterResponse;
import com.gtel.homework.redis.entities.RegisterUserEntity;
import com.gtel.homework.redis.repository.OtpLimitRedisRepository;
import com.gtel.homework.redis.repository.RegisterUserRedisRepository;
import com.gtel.homework.repository.UserRepository;
import com.gtel.homework.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private OtpDomain otpDomain;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testRegisterUser_EmptyPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0391108787");
        request.setPassword("");
        Assertions.assertThrows(ApplicationException.class, () -> userService.registerUser(request));
    }

    @Test
    void testRegisterUser_InvalidPhoneNumberFormat() {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("sontk");
        request.setPassword("Sonha1234@s");
        Assertions.assertThrows(ApplicationException.class, () -> userService.registerUser(request));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(otpDomain);
    }

    @Test
    void testRegisterUser_PhoneNull() {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber(null);
        request.setPassword("Sonha1234@s");
        Assertions.assertThrows(ApplicationException.class, () -> userService.registerUser(request));
    }

    @Test
    void testRegisterUser_ValidPhone_InvalidPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0391108787");
        request.setPassword("123");
        Assertions.assertThrows(ApplicationException.class, () -> userService.registerUser(request));
    }

    @Test
    void testResendOtp_transactionId_notExists() {
        String transactionId = "db149a4d-3f01-46b3-a097-197c3ca09fa3";
        Assertions.assertThrows(ApplicationException.class, () -> userService.resendOtp(transactionId));
    }
}
