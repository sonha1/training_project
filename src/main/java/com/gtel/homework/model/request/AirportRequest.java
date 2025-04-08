package com.gtel.homework.model.request;

import lombok.Data;

@Data
public class AirportRequest {
    private String iata;
    private String name;
    private String airportGroupCode;
    private String language;
    private Integer priority;
    private LocationRequest location;
}
