package com.gtel.homework.redis.entities;

import com.gtel.homework.model.request.RegisterRequest;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@RedisHash("register_user")
@Setter
public class RegisterUserEntity {
    @Id
    private String transactionId;

    private String otp;

    private Long otpExpiredTime;

    private Long otpResendTime;

    private Integer otpResendCount;

    private RegisterRequest data;

    private Integer otpFail;

    @TimeToLive
    private long ttl;
}
