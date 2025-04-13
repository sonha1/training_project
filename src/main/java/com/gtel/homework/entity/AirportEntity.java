package com.gtel.homework.entity;

import com.gtel.homework.model.request.AirportRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "airport")
public class AirportEntity {
    @Id
    private String iata;

    @Column(name = "name")
    private String name;

    @Column(name = "airportgroupcode")
    private String airportgroupcode;

    @Column(name = "language")
    private String language;

    @Column(name = "priority")
    private Integer priority;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AirportEntity(AirportRequest request){
        this.airportgroupcode = request.getAirportGroupCode();
        this.name = request.getName();
        this.iata = request.getIata();
        this.language = request.getLanguage();
        this.priority = request.getPriority();
    }

    public AirportEntity() {

    }
}
