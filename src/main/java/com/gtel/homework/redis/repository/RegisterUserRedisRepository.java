package com.gtel.homework.redis.repository;

import com.gtel.homework.redis.entities.RegisterUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterUserRedisRepository extends JpaRepository<RegisterUserEntity, String> {
}
