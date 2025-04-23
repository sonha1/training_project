package com.gtel.homework.domains;

import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.redis.entities.OtpLimitEntity;
import com.gtel.homework.redis.entities.RegisterUserEntity;
import com.gtel.homework.redis.repository.OtpLimitRedisRepository;
import com.gtel.homework.redis.repository.RegisterUserRedisRepository;
import com.gtel.homework.utils.BcryptUtils;
import com.gtel.homework.utils.ERROR_CODE;
import com.gtel.homework.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OtpDomain {
    private static final SecureRandom random = new SecureRandom();
    private static final DecimalFormat formatter = new DecimalFormat("000000");

    private final OtpLimitRedisRepository otpLimitRedisRepository;
    private final RegisterUserRedisRepository registerUserRedisRepository;

    public OtpDomain(RegisterUserRedisRepository registerUserRedisRepository, OtpLimitRedisRepository otpLimitRedisRepository) {
        this.otpLimitRedisRepository = otpLimitRedisRepository;
        this.registerUserRedisRepository = registerUserRedisRepository;
    }

    public OtpLimitEntity validateLimitOtpByPhoneNumber(String phoneNumber) {
        log.info("[validateLimitOtpByPhoneNumber] START with phone {}", phoneNumber);
        Optional<OtpLimitEntity> otpLimitOpt = otpLimitRedisRepository.findById(phoneNumber);
        if (otpLimitOpt.isEmpty()) {
            OtpLimitEntity entity = new OtpLimitEntity();
            entity.setPhoneNumber(phoneNumber);
            entity.setDailyOtpCounter(0);
            long timeToLive = TimeUtils.getTimeToLiveEndOfDay();
            entity.setTtl(timeToLive);
            return entity;
        }

        OtpLimitEntity otpLimit = otpLimitOpt.get();
        if (otpLimit.getDailyOtpCounter() >= 5) {
            log.info("[validateLimitOtpByPhoneNumber] request fail : otp limit reached with phone {}", phoneNumber);
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP limit reached");
        }
        log.info("[validateLimitOtpByPhoneNumber] DONE with phone {}", phoneNumber);

        return otpLimit;
    }


    public RegisterUserEntity genOtpWhenUserRegister(RegisterRequest request) {
        log.info("[genOtpWhenUserRegister] START with phone {}", request.getPhoneNumber());
        OtpLimitEntity otpLimit = this.validateLimitOtpByPhoneNumber(request.getPhoneNumber());
        String otp = genOtp();
        String transactionId = UUID.randomUUID().toString();
        RegisterUserEntity registerUserEntity = new RegisterUserEntity();
        registerUserEntity.setTransactionId(transactionId);
        log.info("[genOtpWhenUserRegister] transactionId {} with phoneNumber {}", transactionId, request.getPhoneNumber());
        registerUserEntity.setOtp(otp);
        registerUserEntity.setData(request);
        // TODO : move to config env
        registerUserEntity.setOtpFail(0);
        registerUserEntity.setOtpExpiredTime(System.currentTimeMillis() / 1000 + 300);
        registerUserEntity.setOtpResendTime(System.currentTimeMillis() / 1000 + 60);
        registerUserEntity.setTtl(900);
        registerUserRedisRepository.save(registerUserEntity);
        otpLimit.setDailyOtpCounter(otpLimit.getDailyOtpCounter() + 1);
        otpLimitRedisRepository.save(otpLimit);
        log.info("[genOtpWhenUserRegister] DONE with phone {}", request.getPhoneNumber());
        return registerUserEntity;
    }

    public void genOtpWhenUserResend(RegisterUserEntity userEntity) {
        String phoneNumber = userEntity.getData().getPhoneNumber();
        log.info("[genOtpWhenUserResend] START with phone {}", phoneNumber);
        OtpLimitEntity otpLimit = this.validateLimitOtpByPhoneNumber(phoneNumber);
        if (System.currentTimeMillis() / 1000 - userEntity.getOtpResendTime() < 0) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP resend time not allow to send OTP!");
        }
        if (userEntity.getOtpExpiredTime() < System.currentTimeMillis() / 1000) {
            String otp = genOtp();
            userEntity.setOtp(otp);
            userEntity.setOtpExpiredTime(System.currentTimeMillis() / 1000 + 300);
        }

        userEntity.setTtl(900);
        userEntity.setOtpResendTime(System.currentTimeMillis() / 1000 + 60);
        userEntity = registerUserRedisRepository.save(userEntity);

        otpLimit.setDailyOtpCounter(otpLimit.getDailyOtpCounter() + 1);
        otpLimitRedisRepository.save(otpLimit);

        log.info("[genOtpWhenUserResend] DONE with phone {}", phoneNumber);
    }

    public void confirmOTP(RegisterUserEntity registerUser, String otp) {
        String phoneNumber = registerUser.getData().getPhoneNumber();
        log.info("[confirmOTP] START with phone {}", phoneNumber);
        if (registerUser.getOtpExpiredTime() < System.currentTimeMillis() / 1000) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP is expired time");
        }
        if (!StringUtils.equals(otp, registerUser.getOtp())) {
            registerUser.setOtpFail(registerUser.getOtpFail() + 1);
            if (registerUser.getOtpFail() >= 5) {
                this.registerUserRedisRepository.delete(registerUser);
                throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Too many incorrect OTP attempts. Delete transaction");
            }
            this.registerUserRedisRepository.save(registerUser);
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP incorrect");
        }
        log.info("[confirmOTP] START with phone {}", phoneNumber);
    }

    public void removeCacheRegister(RegisterUserEntity registerUser) {
        String phoneNumber = registerUser.getData().getPhoneNumber();
        log.info("[removeCacheRegister] START with phone {}", phoneNumber);
        this.registerUserRedisRepository.delete(registerUser);
        this.otpLimitRedisRepository.deleteById(phoneNumber);
        log.info("[removeCacheRegister] START with phone {}", phoneNumber);
    }

    protected String genOtp() {
        int otp = random.nextInt(1000000);
        return formatter.format(otp);
    }

    public Optional<RegisterUserEntity> getOtpByTransactionId(String transactionId) {
        return this.registerUserRedisRepository.findById(transactionId);
    }
}
