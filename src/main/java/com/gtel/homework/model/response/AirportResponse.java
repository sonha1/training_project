package com.gtel.homework.model.response;

import com.gtel.homework.entity.AirportEntity;
import lombok.Data;

@Data
public class AirportResponse {
    private String iata;
    private String name;
    private String airportGroupCode;
    private String language;
    private Integer priority;

    public AirportResponse(){

    }

    public AirportResponse(AirportEntity entity){
        this.iata = entity.getIata();
        this.name = entity.getName();
        this.priority = entity.getPriority();
        this.language = entity.getLanguage();
        this.airportGroupCode = entity.getAirportgroupcode();
    }
}
