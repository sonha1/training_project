package com.gtel.homework.repository;

import com.gtel.homework.entity.AirportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirportRepository extends JpaRepository<AirportEntity, String>   {


    @Query(value = "select a from AirportEntity a where (?1 is NUll OR a.name = ?1 ) " +
            "AND (?2 is NUll OR a.airportgroupcode = ?2 )  " +
            "AND (?3 is NUll OR a.language = ?3 ) " +
            "AND (?4 is NUll OR a.priority = ?4 )" )
    Page<AirportEntity> findByNameAndAgcodeAndLanguageAndPriority(String name, String apgCode, String language, Integer priority, Pageable pageable);
}
