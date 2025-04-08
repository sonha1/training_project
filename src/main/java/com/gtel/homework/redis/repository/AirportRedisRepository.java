package com.gtel.homework.redis.repository;

import com.gtel.homework.redis.entities.AirportRedisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRedisRepository extends JpaRepository<AirportRedisEntity, String> {
}
