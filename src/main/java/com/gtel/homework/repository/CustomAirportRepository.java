package com.gtel.homework.repository;

import com.gtel.homework.entity.AirportEntity;

import java.util.List;
import java.util.Map;

public interface CustomAirportRepository {

    List<AirportEntity> getCustomSearch(Map<String, String> params);
}
