package com.gtel.homework.redis.repository;

import com.gtel.homework.redis.entities.OtpLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpLimitRedisRepository extends JpaRepository<OtpLimitEntity, String> {
}
