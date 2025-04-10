package com.gtel.homework;

import com.gtel.homework.domains.OtpDomain;
import com.gtel.homework.repository.UserRepository;
import com.gtel.homework.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OtpDomain otpDomain;

    @Test
    void testFindById(){

    }


}
