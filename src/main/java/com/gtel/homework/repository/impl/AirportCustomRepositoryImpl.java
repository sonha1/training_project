package com.gtel.homework.repository.impl;

import com.gtel.homework.entity.AirportEntity;
import com.gtel.homework.repository.CustomAirportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AirportCustomRepositoryImpl implements CustomAirportRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<AirportEntity> getCustomSearch(Map<String, String> params) {
        boolean isExistedCondition = false;
        StringBuffer sb = new StringBuffer("SELECT a FROM AirportEntity a WHERE ");
        String name = params.get("name");
        String airportgroupcode = params.get("airportgroupcode");
        String language = params.get("language");
        String priorityValue = params.get("priority");
        if (StringUtils.isNotBlank(name)) {
            sb.append(" a.name = " + name);
        }

        if (StringUtils.isNotBlank(airportgroupcode)) {
            if (isExistedCondition) {
                sb.append(" AND ");
            } else {
                isExistedCondition = true;
            }
            sb.append(" a.airportgroupcode = " + airportgroupcode);
        }

        if (StringUtils.isNotBlank(language)) {
            if (isExistedCondition) {
                sb.append(" AND ");
            } else {
                isExistedCondition = true;
            }
            sb.append(" a.language = " + language);
        }

        if (StringUtils.isNotBlank(priorityValue)) {
            try {
                Integer intValue = Integer.valueOf(priorityValue);
                if (isExistedCondition) {
                    sb.append(" AND ");
                } else {
                    isExistedCondition = true;
                }
                sb.append(" a.priority = " + priorityValue);
            } catch (Exception e) {
            }
        }
        if (!isExistedCondition) {
            sb.append(" 1=1 ");
        }

        return entityManager.createQuery(sb.toString(), AirportEntity.class).getResultList();
    }
}
