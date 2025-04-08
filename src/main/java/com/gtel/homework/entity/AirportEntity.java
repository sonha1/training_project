package com.gtel.homework.entity;

import com.gtel.homework.model.request.AirportRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "airport")
public class AirportEntity extends BaseEntity{

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

    public AirportEntity(){

    }


    public AirportEntity(AirportRequest request){
        this.airportgroupcode = request.getAirportGroupCode();
        this.name = request.getName();
        this.iata = request.getIata();
        this.language = request.getLanguage();
        this.priority = request.getPriority();
    }
}
