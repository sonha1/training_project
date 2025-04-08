package com.gtel.homework.repository.secification;

import com.gtel.homework.entity.AirportEntity;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class AirPortSpecification {
    public static Specification<AirportEntity> getSpec(Map<String, String> params){
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

//                String name  = params.get("name");
//
//                String airportgroupcode  = params.get("airportgroupcode");
//
//                String language  = params.get("language");
//
//                Integer priority = getParam(params , "priority");

                if (key.equals("name") && StringUtils.isNotBlank(value)){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("name"),  value));
                }

                if (key.equals("airportgroupcode") && StringUtils.isNotBlank(value)){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("airportgroupcode"),  value));
                }

                if (key.equals("language") && StringUtils.isNotBlank(value)){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("language"),  value));
                }

                if (key.equals("priority") && StringUtils.isNotBlank(value)){

                    try {
                        Integer intValue = Integer.valueOf(value);

                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("priority"),  intValue));
                    }catch (Exception e){

                    }

                }
            }

            return predicate;

        };
    }
}
