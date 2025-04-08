package com.gtel.homework.model.response;

import com.gtel.homework.redis.entities.RegisterUserEntity;
import lombok.Data;

@Data
public class RegisterResponse {
    private String transactionId;

    private String otp;

    private long otpExpiredTime;

    private long resendOtpTime;

    public RegisterResponse(RegisterUserEntity entity){
        this.transactionId = entity.getTransactionId();
        this.otp = entity.getOtp();
        this.otpExpiredTime = entity.getOtpExpiredTime() - System.currentTimeMillis()/1000;
        this.resendOtpTime = entity.getOtpResendTime() - System.currentTimeMillis()/1000;
    }
}
