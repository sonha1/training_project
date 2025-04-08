package com.gtel.homework.redis.entities;

import com.gtel.homework.model.response.AirportResponse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash("airport")
public class AirportRedisEntity implements Serializable {

    @Id
    private String code;

    private AirportResponse data;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private long timeToLive;
}
